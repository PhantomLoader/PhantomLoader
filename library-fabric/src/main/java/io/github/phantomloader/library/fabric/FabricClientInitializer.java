package io.github.phantomloader.library.fabric;

import io.github.phantomloader.library.events.ClientEventHandler;
import io.github.phantomloader.library.events.RegisterBlockEntityRenderersEvent;
import io.github.phantomloader.library.events.RegisterEntityRenderersEvent;
import io.github.phantomloader.library.events.RegisterParticlesEvent;
import io.github.phantomloader.library.fabric.renderers.BlockEntityItemRenderer;
import io.github.phantomloader.library.utils.CreativeTabsUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * <p>
 *     Fabric client initializer used by the Phantom Library mod.
 * </p>
 *
 * @author Nico
 */
public class FabricClientInitializer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ServiceLoader.load(ClientEventHandler.class).forEach(handler -> {
            CreativeTabsUtils.allTabKeys().forEach(resourceKey -> handler.addItemsToCreativeTab(resourceKey, item -> ItemGroupEvents.modifyEntriesEvent(resourceKey).register(listener -> listener.accept(item.get()))));
            handler.registerBlockEntityRenderers(new RegisterBlockEntityRenderersEventFabric());
            handler.registerEntityRenderers(new RegisterEntityRenderersEventFabric());
            handler.registerBlockRenderType((block, renderType) -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), renderType));
            handler.registerParticles(new RegisterParticlesEventFabric());
        });
    }

    /**
     * <p>
     *     Implementation of {@link RegisterBlockEntityRenderersEvent}.
     * </p>
     *
     * @author Nico
     */
    public static class RegisterBlockEntityRenderersEventFabric implements RegisterBlockEntityRenderersEvent {

        @Override
        public <T extends BlockEntity> void register(BlockEntityType<? extends T> blockEntity, BlockEntityRendererProvider<T> renderer) {
            BlockEntityRenderers.register(blockEntity, renderer);
        }

        @Override
        public void registerItemRenderer(Supplier<? extends Block> block) {
            BuiltinItemRendererRegistry.INSTANCE.register(block.get(), new BlockEntityItemRenderer(block.get()));
        }
    }

    /**
     * <p>
     *     Implementation of {@link RegisterEntityRenderersEvent}.
     * </p>
     *
     * @author Nico
     */
    public static class RegisterEntityRenderersEventFabric implements RegisterEntityRenderersEvent {

        @Override
        public <T extends Entity> void register(EntityType<? extends T> entity, EntityRendererProvider<T> renderer) {
            EntityRendererRegistry.register(entity, renderer);
        }
    }

    /**
     * <p>
     *     Implementation of {@link RegisterParticlesEvent}.
     * </p>
     *
     * @author Nico
     */
    public static class RegisterParticlesEventFabric implements RegisterParticlesEvent {

        @Override
        public <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
            ParticleFactoryRegistry.getInstance().register(type, provider);
        }
    }
}
