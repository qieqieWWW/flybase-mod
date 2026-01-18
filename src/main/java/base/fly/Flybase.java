package base.fly;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import base.fly.block.ModBlockEntities;
import base.fly.block.ModBlocks;
import base.fly.command.FlybaseCommand;
import base.fly.config.FlybaseConfig;
import base.fly.ability.FlyingAbilityManager;
import base.fly.item.ModItems;
import base.fly.item.ModItemGroups;

public class Flybase implements ModInitializer {
	public static final String MOD_ID = "flybase";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("初始化飞行基站模组...");

		// 加载配置
		FlybaseConfig.load();

		// 初始化方块和物品
		ModBlocks.initialize();
		ModBlocks.registerItems();
		ModBlockEntities.initialize();
		ModItems.initialize();
		ModItemGroups.initialize();

		// 注册命令
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			FlybaseCommand.register(dispatcher);
		});

		// 注册服务器刻事件
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			// 每个服务器刻遍历所有维度与玩家，更新飞行能力
			for (ServerWorld world : server.getWorlds()) {
				for (var player : world.getPlayers()) {
					FlyingAbilityManager.updatePlayerAbility(player, world);
				}
			}
		});

		// 注册服务器刻事件以更新缓降效果
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			FlyingAbilityManager.updateSlowFallingEffects();
		});

		LOGGER.info("飞行基站模组初始化完成！");
	}
}