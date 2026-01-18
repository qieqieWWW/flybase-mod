package base.fly.block;

import java.util.Optional;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import base.fly.Flybase;

public class ModBlocks {
	@SuppressWarnings("null")
	public static final Block FLYING_BASE_BLOCK = Registry.register(
		Registries.BLOCK,
		Identifier.of(Flybase.MOD_ID, "flying_base"),
		new FlyingBaseBlock(
			AbstractBlock.Settings
				.copy(Blocks.STONE)
				.registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Flybase.MOD_ID, "flying_base")))
				// 约 2 秒的挖掘时长（以铁镐为基准，实际受效率/急速等影响）
				.strength(3.0f, 6.0f)
				.requiresTool()
		)
	);

	public static final void registerItems() {
		registerBlockItem("flying_base", FLYING_BASE_BLOCK);
	}

	private static Item registerBlockItem(String name, Block block) {
		Identifier id = Identifier.of(Flybase.MOD_ID, name);
		@SuppressWarnings("null")
		Item item = Registry.register(Registries.ITEM, id,
			new BlockItem(block, new Item.Settings()
				.registryKey(RegistryKey.of(RegistryKeys.ITEM, id))
			)
		);
		return item;
	}

	public static void initialize() {
		// 触发类加载
	}
}
