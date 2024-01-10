package io.github.phantomloader.library.events;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>
 *     Interface to be implemented to implement a client event handler.
 *     The class that implements this interface must be registered as a service in {@code META-INF/services/io.github.phantomloader.library.events.ClientEventHandler}.
 *     See {@link java.util.ServiceLoader} for more details.
 * </p>
 * <ul>
 *     <li>In Forge, these methods are called from a client event handler.</li>
 *     <li>In Fabric, these methods are called from a {@code ClientModInitializer}.</li>
 * </ul>
 *
 * @author Nico
 */
public interface ClientEventHandler {

    /**
     * <p>
     *     Method called when items can be added to a creative tab.
     *     Items can be added by calling {@link Consumer#accept(Object)} on the given consumer if the given {@link CreativeModeTab} matches the desired one.
     * </p>
     * <pre>
     *     {@code @Override}
     *     public void addItemsToCreativeTab(CreativeModeTab creativeTab, Consumer event) {
     *         if(resourceKey.equals(CreativeTabsUtils.BUILDING_BLOCKS) {
     *             event.accept(ExampleMod.MY_BLOCK);
     *         }
     *     }
     * </pre>
     *
     * @param creativeTab The creative tab.
     * @param event Use {@code event.accept(ExampleMod.MY_ITEM)} to add an item to this creative tab.
     */
    default void addItemsToCreativeTab(CreativeModeTab creativeTab, Consumer<Supplier<? extends ItemLike>> event) {

    }

    /**
     * <p>
     *     Method called when a block entity renderer can be registered.
     *     Renderers can be added by calling {@link RegisterBlockEntityRenderersEvent#register(BlockEntityType, BlockEntityRendererProvider)}.
     * </p>
     * <pre>
     *     {@code @Override}
     *     public void registerBlockEntityRenderers(RegisterBlockEntityRenderersEvent event) {
     *         event.register(ExampleMod.CHEST_BLOCK_ENTITY, ChestRenderer::new);
     *     }
     * </pre>
     *
     * @param event Wraps a Forge or a Fabric event.
     */
    default void registerBlockEntityRenderers(RegisterBlockEntityRenderersEvent event) {

    }

    /**
     * <p>
     *     Method called when an entity renderer can be registered.
     *     Renderers can be added by calling {@link RegisterEntityRenderersEvent#register(EntityType, EntityRendererProvider)}.
     * </p>
     * <pre>
     *     {@code @Override}
     *     public void registerEntityRenderers(RegisterEntityRenderersEvent event) {
     *         event.register(ExampleMod.MY_ENTITY, MyEntityRenderer::new);
     *     }
     * </pre>
     *
     * @param event Wraps a Forge or a Fabric event.
     */
    default void registerEntityRenderers(RegisterEntityRenderersEvent event) {

    }

    /**
     * <p>
     *     Method called when a block can be added to a render layer type.
     *     This can be used to implement translucent or transparent blocks.
     * </p>
     * <pre>
     *     {@code @Override}
     *     public void registerBlockRenderType(BiConsumer consumer) {
     *         consumer.accept(ExampleMod.CUSTOM_VINES, RenderType.cutout());
     *     }
     * </pre>
     * <p>
     *     Note that this method is only called in Fabric.
     *     For Forge, an additional {@code "render_type"} field should be added to the block's model json file.
     * </p>
     *
     * @param event Use {@code event.accept(ExampleMod.MY_BLOCK, RenderType....)} to register a block's render layer.
     */
    default void registerBlockRenderType(BiConsumer<Supplier<? extends Block>, RenderType> event) {

    }

    /**
     * <p>
     *     Method call when a particle renderer can be registered.
     * </p>
     *
     * @param event Wraps a Forge or a Fabric event.
     */
    default void registerParticles(RegisterParticlesEvent event) {

    }
}
