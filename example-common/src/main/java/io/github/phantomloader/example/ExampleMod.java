package io.github.phantomloader.example;

import io.github.phantomloader.library.ModRegistry;
import io.github.phantomloader.library.Mod;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class ExampleMod {

	public static final String ID = "example_mod";

	private static final ModRegistry REGISTRY = ModRegistry.instantiate(ID);

	public static final Supplier<Item> TEST_ITEM = REGISTRY.registerItem("test_item");

	public static final Supplier<Block> TEST_BLOCK = REGISTRY.registerBlockAndItem("test_block", BlockBehaviour.Properties.of());

	@Mod(
			id = ID,
			name = "Example Mod",
			description = "This is the description of an auto-generated mod.",
			authors = {"HexagonNico"},
			credits = "Credits go here. You're welcome.",
			homepage = "https://example.com",
			sources = "https://github.com",
			license = "CC BY-NC-ND 4.0"
	)
	public static void initialize() {
		REGISTRY.register();
	}
}
