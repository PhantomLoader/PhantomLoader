package io.github.hexagonnico.phantom.library;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface ModRegistry {

	<T extends Item> Supplier<T> registerItem(String name, Supplier<T> item);

	<T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block);

	default Supplier<BlockItem> registerBlockItem(String name, Supplier<? extends Block> block) {
		return this.registerItem(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}

	default <T extends Block> Supplier<T> registerBlockAndItem(String name, Supplier<T> block) {
		Supplier<T> registered = this.registerBlock(name, block);
		this.registerBlockItem(name, registered);
		return registered;
	}

	void register();
}
