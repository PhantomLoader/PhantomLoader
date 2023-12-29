package io.github.phantomloader.example;

import io.github.phantomloader.example.block.ModChestBlock;
import io.github.phantomloader.example.block.ModChestBlockEntity;
import io.github.phantomloader.library.ModEntryPoint;
import io.github.phantomloader.library.registry.ClientRegistry;
import io.github.phantomloader.library.registry.ModRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;
import java.util.function.Supplier;

public class ExampleMod {

	private static final ModRegistry REGISTRY = ModRegistry.instantiate("example");

	public static final Supplier<Item> TEST_ITEM = REGISTRY.registerItem("test_item");

	public static final Supplier<Block> TEST_BLOCK = REGISTRY.registerBlockAndItem("test_block");

	public static final Supplier<ModChestBlock> CHEST_1 = REGISTRY.registerBlockVariantAndItem("chest_1", ModChestBlock::new, () -> Blocks.CHEST);
	public static final Supplier<ModChestBlock> CHEST_2 = REGISTRY.registerBlockVariantAndItem("chest_2", ModChestBlock::new, () -> Blocks.CHEST);

	public static final Supplier<BlockEntityType<? extends ModChestBlockEntity>> CHEST_BLOCK_ENTITY = REGISTRY.registerBlockEntity("chest", ModChestBlockEntity::new, Set.of(CHEST_1, CHEST_2));

	public static final Supplier<CreativeModeTab> CREATIVE_MODE_TAB = REGISTRY.registerCreativeTab("john", TEST_ITEM, "John", Set.of(TEST_ITEM));

	@ModEntryPoint
	public static void initialize() {
		REGISTRY.register();
	}

	@ModEntryPoint(side = ModEntryPoint.Side.CLIENT)
	public static void initializeClient() {
		ClientRegistry.INSTANCE.addItemToCreativeTab("example", "john", TEST_BLOCK);
		ClientRegistry.INSTANCE.addItemsToCreativeTab("functional_blocks", Set.of(CHEST_1, CHEST_2));
	}
}
