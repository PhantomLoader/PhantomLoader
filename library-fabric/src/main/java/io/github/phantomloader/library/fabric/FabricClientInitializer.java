package io.github.phantomloader.library.fabric;

import io.github.phantomloader.library.fabric.registry.FabricRegistry;
import io.github.phantomloader.library.fabric.renderers.BlockEntityItemRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.function.Supplier;

/**
 * <p>
 *     Fabric client mod initializer.
 * </p>
 *
 * @author Nico
 */
public class FabricClientInitializer implements ClientModInitializer {

	/** Registered entity blocks */
	private static final HashSet<Block> ENTITY_BLOCKS = new HashSet<>();

	/**
	 * <p>
	 *     Registers a Fabric block entity renderer.
	 *     Called from {@link FabricRegistry#registerBlockItem(String, Supplier)}.
	 * </p>
	 *
	 * @param block The entity block.
	 */
	public static void registerEntityBlockRenderer(Block block) {
		ENTITY_BLOCKS.add(block);
	}

	@Override
	public void onInitializeClient() {
		ENTITY_BLOCKS.forEach(block -> BuiltinItemRendererRegistry.INSTANCE.register(block, new BlockEntityItemRenderer(block)));
	}
}
