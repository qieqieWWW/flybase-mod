package base.fly.ability;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;

import base.fly.block.entity.FlyingBaseBlockEntity;
import base.fly.config.FlybaseConfig;

public class FlyingAbilityManager {
	private static final Logger LOGGER = LoggerFactory.getLogger("flybase/ability");
	private static final Map<UUID, BlockPos> playerActiveFlyBase = new HashMap<>();
	private static final Map<UUID, Integer> playerSlowFallingDuration = new HashMap<>();
	private static final Map<RegistryKey<World>, Set<BlockPos>> activeBases = new HashMap<>();

	public static void updatePlayerAbility(PlayerEntity player, World level) {
		UUID playerUUID = player.getUuid();
		BlockPos playerPos = player.getBlockPos();
		BlockPos currentBase = null;
        
		Set<BlockPos> bases = activeBases.getOrDefault(level.getRegistryKey(), Set.of());
		for (BlockPos basePos : bases) {
			if (level.getBlockEntity(basePos) instanceof FlyingBaseBlockEntity base && base.isPowered()) {
				if (isPlayerInRange(playerPos, basePos, base)) {
					currentBase = basePos;
					break;
				}
			}
		}

		BlockPos previousBase = playerActiveFlyBase.get(playerUUID);

		// 如果玩家进入新的飞行基站范围
		if (currentBase != null && previousBase == null) {
			enableFlying(player);
			playerActiveFlyBase.put(playerUUID, currentBase);
			FlyingAbilityManager.LOGGER.info("Player {} enabled flight at base {}", player.getName().getString(), currentBase);
		}
		// 如果玩家离开飞行基站范围
		else if (currentBase == null && previousBase != null) {
			disableFlying(player);
			playerActiveFlyBase.remove(playerUUID);
			// 应用缓降效果
			applySlowFallingEffect(player);
			playerSlowFallingDuration.put(playerUUID, FlybaseConfig.slowFallingTime * 20); // 转换为刻
			FlyingAbilityManager.LOGGER.info("Player {} disabled flight, applying slow falling", player.getName().getString());
		}
	}

	public static void updateSlowFallingEffects() {
		playerSlowFallingDuration.entrySet().removeIf(entry -> {
			entry.setValue(entry.getValue() - 1);
			return entry.getValue() <= 0;
		});
	}

	private static void enableFlying(PlayerEntity player) {
		if (!player.isCreative() && !player.isSpectator()) {
			player.getAbilities().allowFlying = true;
			player.getAbilities().flying = true;
			player.sendAbilitiesUpdate();
		}
	}

	private static void disableFlying(PlayerEntity player) {
		if (!player.isCreative() && !player.isSpectator()) {
			player.getAbilities().allowFlying = false;
			player.getAbilities().flying = false;
			player.sendAbilitiesUpdate();
		}
	}

	private static void applySlowFallingEffect(PlayerEntity player) {
		player.addStatusEffect(new StatusEffectInstance(
			StatusEffects.SLOW_FALLING,
			FlybaseConfig.slowFallingTime * 20, // 持续时间（刻）
			0, // 效果等级
			false, // 是否显示粒子
			true // 是否显示图标
		));
	}

	private static boolean isPlayerInRange(BlockPos playerPos, BlockPos basePos, FlyingBaseBlockEntity base) {
		int dx = Math.abs(playerPos.getX() - basePos.getX());
		int dy = Math.abs(playerPos.getY() - basePos.getY());
		int dz = Math.abs(playerPos.getZ() - basePos.getZ());

		return dx <= base.getSpaceX() && dy <= base.getSpaceY() && dz <= base.getSpaceZ();
	}

	public static void removePlayer(UUID playerUUID) {
		playerActiveFlyBase.remove(playerUUID);
		playerSlowFallingDuration.remove(playerUUID);
	}

	public static void onBasePowered(World level, BlockPos pos) {
		activeBases.computeIfAbsent(level.getRegistryKey(), k -> new HashSet<>()).add(pos);
	}

	public static void onBaseUnpowered(World level, BlockPos pos) {
		Set<BlockPos> set = activeBases.get(level.getRegistryKey());
		if (set != null) {
			set.remove(pos);
			if (set.isEmpty()) {
				activeBases.remove(level.getRegistryKey());
			}
		}
	}
}
