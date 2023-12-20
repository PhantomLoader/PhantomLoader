package io.github.phantomloader.library.fabric;

import io.github.phantomloader.library.CreativeTabs;
import io.github.phantomloader.library.fabric.renderers.BlockEntityItemRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;

public class FabricClientInitializer implements ClientModInitializer {

	private static final HashSet<Block> ENTITY_BLOCKS = new HashSet<>();

	public static void registerEntityBlockRenderer(Block block) {
		ENTITY_BLOCKS.add(block);
	}

	@Override
	public void onInitializeClient() {
		ENTITY_BLOCKS.forEach(block -> BuiltinItemRendererRegistry.INSTANCE.register(block, new BlockEntityItemRenderer(block)));
		CreativeTabs.allTabs().forEach(resourceKey -> ItemGroupEvents.modifyEntriesEvent(resourceKey).register(listener -> CreativeTabs.accept(resourceKey, supplier -> listener.accept(supplier.get()))));
	}
}
