package base.fly.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.explosion.Explosion;
import com.mojang.serialization.MapCodec;

import base.fly.block.entity.FlyingBaseBlockEntity;

public class FlyingBaseBlock extends BlockWithEntity {
	public static final MapCodec<FlyingBaseBlock> CODEC = createCodec(FlyingBaseBlock::new);

	public FlyingBaseBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FlyingBaseBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (!level.isClient() && blockEntityType == ModBlockEntities.FLYING_BASE_BLOCK_ENTITY) {
			return (lvl, pos, st, blockEntity) -> {
				if (blockEntity instanceof FlyingBaseBlockEntity) {
					FlyingBaseBlockEntity.tick(lvl, pos, st, blockEntity);
				}
			};
		}
		return null;
	}

	@Override
	protected ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!level.isClient() && player.isCreative()) {
			// 创造模式玩家可以和方块交互
		}
		return ActionResult.SUCCESS;
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
		if (!moved && state.getBlock() != world.getBlockState(pos).getBlock()) {
			// 保障掉落：使用标准掉落路径（依赖 loot 表），若未配置也至少掉落自身
			BlockEntity be = world.getBlockEntity(pos);
			Block.dropStacks(state, world, pos, be);
		}
		super.onStateReplaced(state, world, pos, moved);
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
		if (!world.isClient()) {
			// 先尝试按 loot 表掉落（含丝触/时运等）
			Block.dropStacks(state, (ServerWorld) world, pos, blockEntity, player, tool);
			// 再兜底直接掉落自身，避免任何 loot 路径失效
			Block.dropStack(world, pos, ModBlocks.FLYING_BASE_BLOCK.asItem().getDefaultStack());
		}
		super.afterBreak(world, player, pos, state, blockEntity, tool);
	}
}
