package base.fly.block.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;

import base.fly.block.ModBlockEntities;
import base.fly.config.FlybaseConfig;
import base.fly.ability.FlyingAbilityManager;

public class FlyingBaseBlockEntity extends BlockEntity {
	private static final Logger LOGGER = LoggerFactory.getLogger("flybase/block");
	private boolean powered = false;

	public FlyingBaseBlockEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.FLYING_BASE_BLOCK_ENTITY, pos, blockState);
	}

	public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T entity) {
		if (world == null || world.isClient()) return;
		if (!(entity instanceof FlyingBaseBlockEntity)) return;

		FlyingBaseBlockEntity flyingBase = (FlyingBaseBlockEntity) entity;
		int redstoneLevel = world.getReceivedRedstonePower(pos);
		boolean powered = redstoneLevel > 0;
		
		if (powered != flyingBase.isPowered()) {
			flyingBase.setPowered(powered);
			LOGGER.info("Flying Base at {} powered: {}", pos, powered);

			if (powered) {
				FlyingAbilityManager.onBasePowered(world, pos);
			} else {
				FlyingAbilityManager.onBaseUnpowered(world, pos);
			}
		}
	}

	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean newPowered) {
		this.powered = newPowered;
	}

	public int getSpaceX() {
		return FlybaseConfig.spaceX;
	}

	public int getSpaceY() {
		return FlybaseConfig.spaceY;
	}

	public int getSpaceZ() {
		return FlybaseConfig.spaceZ;
	}
}
