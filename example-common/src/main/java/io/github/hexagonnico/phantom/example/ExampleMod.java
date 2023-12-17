package io.github.hexagonnico.phantom.example;

import io.github.hexagonnico.phantom.library.Mod;
import io.github.hexagonnico.phantom.library.ModRegistry;
import io.github.hexagonnico.phantom.library.RegistryManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class ExampleMod {

	public static final String ID = "example_mod";

	private static final ModRegistry REGISTRY = RegistryManager.create(ID);

	public static final Supplier<Item> TEST_ITEM = REGISTRY.registerItem("test_item");

	public static final Supplier<Block> TEST_BLOCK = REGISTRY.registerBlockAndItem("test_block");

	@Mod(
			id = ID,
			name = "Example Mod",
			description = "This is the description of an auto-generated mod.",
			authors = {"HexagonNico"},
			credits = "Credits go here. You're welcome.",
			homepage = "https://example.com/homepage",
			sources = "https://github.com",
			issues = "https://example.com/issueTracker",
			license = "CC BY-NC-ND 4.0"
	)
	public static void initialize() {
		REGISTRY.register();
	}
}
