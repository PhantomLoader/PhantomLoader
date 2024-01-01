package io.github.phantomloader.library.events;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * <p>
 *     Interface used to wrap an event to register a block entity's renderer.
 * </p>
 *
 * @author Nico
 */
public interface RegisterBlockEntityRenderersEvent {

    /**
     * <p>
     *     This method must be called from an event handler to register a block entity renderer.
     *     See {@link ClientEventHandler#registerBlockEntityRenderers(RegisterBlockEntityRenderersEvent)} for details.
     * </p>
     *
     * @param blockEntity A supplier returning the registered block entity type, the one returned by {@link io.github.phantomloader.library.registry.ModRegistry#registerBlockEntity(String, BiFunction, Supplier)}.
     * @param renderer A function returning the block entity renderer. If you have created a class that extends {@link net.minecraft.client.renderer.blockentity.BlockEntityRenderer}, this should be that class' constructor passed as a method reference.
     * @param <T> The block entity type
     */
    <T extends BlockEntity> void register(Supplier<BlockEntityType<T>> blockEntity, BlockEntityRendererProvider<T> renderer);
}
