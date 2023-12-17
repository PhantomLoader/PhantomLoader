package absloader.example;

import absloader.library.Mod;
import absloader.library.ModRegistry;
import absloader.library.RegistryManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class ExampleMod {

	public static final String ID = "example_mod";

	private static final ModRegistry REGISTRY = RegistryManager.create(ID);

	public static final Supplier<Item> TEST_ITEM = REGISTRY.registerItem("test_item", () -> new Item(new Item.Properties()));

	public static final Supplier<Block> TEST_BLOCK = REGISTRY.registerBlockAndItem("test_block", () -> new Block(BlockBehaviour.Properties.of()));

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
