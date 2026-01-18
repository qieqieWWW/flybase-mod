package base.fly.block;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import base.fly.Flybase;
import base.fly.block.entity.FlyingBaseBlockEntity;

public class ModBlockEntities {
	public static final BlockEntityType<FlyingBaseBlockEntity> FLYING_BASE_BLOCK_ENTITY =
		Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
			Identifier.of(Flybase.MOD_ID, "flying_base"),
			FabricBlockEntityTypeBuilder.create(FlyingBaseBlockEntity::new, ModBlocks.FLYING_BASE_BLOCK).build()
		);

	public static void initialize() {
		// 触发类加载
	}
}
