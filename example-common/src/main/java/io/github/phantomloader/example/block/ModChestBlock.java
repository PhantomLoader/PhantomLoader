package io.github.phantomloader.example.block;

import io.github.phantomloader.example.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ModChestBlock extends ChestBlock {

	@SuppressWarnings({"Convert2MethodRef"})
	public ModChestBlock(Properties properties) {
		// Method reference cannot be used because of circular dependency
		super(properties, () -> ExampleMod.CHEST_BLOCK_ENTITY.get());
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		return new ModChestBlockEntity(blockPos, blockState);
	}
}
