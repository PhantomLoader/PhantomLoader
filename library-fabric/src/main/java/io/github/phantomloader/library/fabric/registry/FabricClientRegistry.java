package io.github.phantomloader.library.fabric.registry;

import io.github.phantomloader.library.registry.ClientRegistry;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * <p>
 *     Fabric implementation of the client registry.
 * </p>
 *
 * @author Nico
 */
public class FabricClientRegistry implements ClientRegistry {

	@Override
	public void addItemsToCreativeTab(ResourceKey<CreativeModeTab> resourceKey, Collection<Supplier<? extends ItemLike>> items) {
		ItemGroupEvents.modifyEntriesEvent(resourceKey).register(listener -> items.forEach(item -> listener.accept(item.get())));
	}

	@Override
	public <T extends Entity> void registerEntityRenderer(Supplier<EntityType<T>> entity, EntityRendererProvider<T> renderer) {
		EntityRendererRegistry.register(entity.get(), renderer);
	}

	@Override
	public <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> blockEntity, BlockEntityRendererProvider<T> renderer) {
		BlockEntityRenderers.register(blockEntity.get(), renderer);
	}

	@Override
	public void registerRenderType(Supplier<? extends Block> block, RenderType renderType) {
		BlockRenderLayerMap.INSTANCE.putBlock(block.get(), renderType);
	}
}
