package io.github.phantomloader.library.fabric;

import io.github.phantomloader.library.fabric.registry.FabricRegistry;
import io.github.phantomloader.library.fabric.renderers.BlockEntityItemRenderer;
import io.github.phantomloader.library.utils.CreativeTabs;
import io.github.phantomloader.library.utils.ModRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
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
		this.creativeTabs();
		ModRenderers.registerEntityRenderers(EntityRendererRegistry::register);
		ModRenderers.registerBlockEntityRenderers(BlockEntityRenderers::register);
	}

	/**
	 * Private function used to put items in vanilla creative tabs.
	 */
	private void creativeTabs() {
		this.creativeTab(CreativeModeTabs.BUILDING_BLOCKS);
		this.creativeTab(CreativeModeTabs.COLORED_BLOCKS);
		this.creativeTab(CreativeModeTabs.NATURAL_BLOCKS);
		this.creativeTab(CreativeModeTabs.FUNCTIONAL_BLOCKS);
		this.creativeTab(CreativeModeTabs.REDSTONE_BLOCKS);
		this.creativeTab(CreativeModeTabs.TOOLS_AND_UTILITIES);
		this.creativeTab(CreativeModeTabs.COMBAT);
		this.creativeTab(CreativeModeTabs.FOOD_AND_DRINKS);
		this.creativeTab(CreativeModeTabs.INGREDIENTS);
		this.creativeTab(CreativeModeTabs.SPAWN_EGGS);
	}

	/**
	 * Private function used to put items in a vanilla creative tab.
	 */
	private void creativeTab(ResourceKey<CreativeModeTab> resourceKey) {
		ItemGroupEvents.modifyEntriesEvent(resourceKey).register(listener -> CreativeTabs.accept(resourceKey, item -> listener.accept(item.get())));
	}
}
