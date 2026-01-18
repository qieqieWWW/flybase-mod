package base.fly.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import base.fly.Flybase;
import base.fly.block.ModBlocks;

public class ModItemGroups {
    public static final ItemGroup FLYBASE_GROUP = Registry.register(
        Registries.ITEM_GROUP,
        Identifier.of(Flybase.MOD_ID, "flybase"),
        FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.flybase"))
            .icon(() -> new ItemStack(ModBlocks.FLYING_BASE_BLOCK))
            .entries((context, entries) -> {
                entries.add(ModBlocks.FLYING_BASE_BLOCK);
            })
            .build()
    );

    public static void initialize() {
        // 触发类加载
    }
}