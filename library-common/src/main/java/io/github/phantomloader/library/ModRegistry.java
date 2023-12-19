package io.github.phantomloader.library;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ModRegistry {

	<T extends Item> Supplier<T> registerItem(String name, Supplier<T> item);

	default Supplier<Item> registerItem(String name, Item.Properties properties) {
		return this.registerItem(name, () -> new Item(properties));
	}

	default Supplier<Item> registerItem(String name) {
		return this.registerItem(name, new Item.Properties());
	}

	<T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block);

	default Supplier<Block> registerBlock(String name, BlockBehaviour.Properties properties) {
		return this.registerBlock(name, () -> new Block(properties));
	}

	default Supplier<Block> registerBlock(String name) {
		return this.registerBlock(name, BlockBehaviour.Properties.of());
	}

	default Supplier<BlockItem> registerBlockItem(String name, Supplier<? extends Block> block) {
		return this.registerItem(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}

	default <T extends Block> Supplier<T> registerBlockAndItem(String name, Supplier<T> block) {
		Supplier<T> registered = this.registerBlock(name, block);
		this.registerBlockItem(name, registered);
		return registered;
	}

	default Supplier<Block> registerBlockAndItem(String name, BlockBehaviour.Properties properties) {
		return this.registerBlockAndItem(name, () -> new Block(properties));
	}

	// FIXME: This using this method in forge causes a crash for unknown reasons
//	default Supplier<Block> registerBlockAndItem(String name) {
//		return this.registerBlockAndItem(name, BlockBehaviour.Properties.of());
//	}

	default Supplier<Block> registerBlockVariant(String name, Supplier<? extends Block> base) {
		return this.registerBlock(name, () -> new Block(BlockBehaviour.Properties.copy(base.get())));
	}

	default <T extends Block> Supplier<T> registerBlockVariant(String name, Function<BlockBehaviour.Properties, T> constructor, Supplier<? extends Block> base) {
		return this.registerBlock(name, () -> constructor.apply(BlockBehaviour.Properties.copy(base.get())));
	}

	default Supplier<Block> registerBlockVariantAndItem(String name, Supplier<? extends Block> base) {
		return this.registerBlockAndItem(name, () -> new Block(BlockBehaviour.Properties.copy(base.get())));
	}

	default <T extends Block> Supplier<T> registerBlockVariantAndItem(String name, Function<BlockBehaviour.Properties, T> constructor, Supplier<? extends Block> base) {
		return this.registerBlockAndItem(name, () -> constructor.apply(BlockBehaviour.Properties.copy(base.get())));
	}

	<T extends BlockEntity> Supplier<BlockEntityType<? extends T>> registerBlockEntity(String name, Supplier<? extends Block> block, BiFunction<BlockPos, BlockState, T> blockEntity);

	<T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder);

	default Supplier<SpawnEggItem> registerSpawnEgg(String name, Supplier<EntityType<? extends Mob>> entity, int primaryColor, int secondaryColor) {
		return this.registerItem(name, () -> new SpawnEggItem(entity.get(), primaryColor, secondaryColor, new Item.Properties()));
	}

	<T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature);

	void register();
}
