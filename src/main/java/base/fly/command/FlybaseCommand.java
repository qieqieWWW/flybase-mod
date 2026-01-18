package base.fly.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

import base.fly.config.FlybaseConfig;

public class FlybaseCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(
			CommandManager.literal("flybase") // 需要管理员权限
				.then(CommandManager.literal("space")
					.then(CommandManager.argument("x", IntegerArgumentType.integer(1, 1000))
						.executes(FlybaseCommand::setSpace1D)
						.then(CommandManager.argument("y", IntegerArgumentType.integer(1, 1000))
							.then(CommandManager.argument("z", IntegerArgumentType.integer(1, 1000))
								.executes(FlybaseCommand::setSpace3D)
							)
						)
					)
				)
				.then(CommandManager.literal("slow_falling_time")
					.then(CommandManager.argument("seconds", IntegerArgumentType.integer(1, 100))
						.executes(FlybaseCommand::setSlowFallingTime)
					)
				)
				.then(CommandManager.literal("info")
					.executes(FlybaseCommand::showInfo)
				)
		);
	}

	private static int setSpace1D(CommandContext<ServerCommandSource> context) {
		int space = IntegerArgumentType.getInteger(context, "x");
		FlybaseConfig.spaceX = space;
		FlybaseConfig.spaceY = space;
		FlybaseConfig.spaceZ = space;
		FlybaseConfig.save();
		
		context.getSource().sendFeedback(() -> 
			Text.literal("§6飞行基站§r 飞行空间已设置为 " + space + "x" + space + "x" + space),
			true
		);
		return 1;
	}

	private static int setSpace3D(CommandContext<ServerCommandSource> context) {
		int x = IntegerArgumentType.getInteger(context, "x");
		int y = IntegerArgumentType.getInteger(context, "y");
		int z = IntegerArgumentType.getInteger(context, "z");
		
		FlybaseConfig.spaceX = x;
		FlybaseConfig.spaceY = y;
		FlybaseConfig.spaceZ = z;
		FlybaseConfig.save();
		
		context.getSource().sendFeedback(() -> 
			Text.literal("§6飞行基站§r 飞行空间已设置为 " + x + "x" + y + "x" + z),
			true
		);
		return 1;
	}

	private static int setSlowFallingTime(CommandContext<ServerCommandSource> context) {
		int seconds = IntegerArgumentType.getInteger(context, "seconds");
		FlybaseConfig.slowFallingTime = seconds;
		FlybaseConfig.save();
		
		context.getSource().sendFeedback(() -> 
			Text.literal("§6飞行基站§r 缓降效果时长已设置为 " + seconds + " 秒"),
			true
		);
		return 1;
	}

	private static int showInfo(CommandContext<ServerCommandSource> context) {
		context.getSource().sendFeedback(() -> 
			Text.literal("§6飞行基站配置信息:§r\n" +
				"飞行空间: " + FlybaseConfig.spaceX + "x" + FlybaseConfig.spaceY + "x" + FlybaseConfig.spaceZ + "\n" +
				"缓降效果时长: " + FlybaseConfig.slowFallingTime + " 秒"),
			false
		);
		return 1;
	}
}
