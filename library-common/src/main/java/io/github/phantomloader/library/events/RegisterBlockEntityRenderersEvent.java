package io.github.phantomloader.library.events;

import io.github.phantomloader.library.registry.ModRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
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
     * @param blockEntity A supplier returning the registered block entity type, the one returned by {@link ModRegistry#registerBlockEntity(String, BiFunction, Supplier)}.
     * @param renderer A function returning the block entity renderer. If you have created a class that extends {@link BlockEntityRenderer}, this should be that class' constructor passed as a method reference.
     * @param <T> The block entity type
     */
    <T extends BlockEntity> void register(BlockEntityType<? extends T> blockEntity, BlockEntityRendererProvider<T> renderer);

    /**
     * <p>
     *     This method must be called from an event handler to register a render for a {@link net.minecraft.world.item.BlockItem} that uses a block entity.
     *     Tells the mod loader to use the same renderer as the block entity to render the item.
     * </p>
     * <p>
     *     Note that this method is only implemented in Fabric. In Forge, item renderers are registered by Forge's {@code IClientItemExtensions} hook.
     * </p>
     *
     * @param block A supplier returning the relevant registered block, the one returned by {@link ModRegistry#registerBlock(String)}.
     */
    default void registerItemRenderer(Supplier<? extends Block> block) {

    }
}
