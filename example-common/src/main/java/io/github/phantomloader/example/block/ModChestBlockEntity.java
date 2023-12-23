package io.github.phantomloader.example.block;

import io.github.phantomloader.example.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModChestBlockEntity extends ChestBlockEntity {

	public ModChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ExampleMod.CHEST_BLOCK_ENTITY.get(), blockPos, blockState);
	}
}
