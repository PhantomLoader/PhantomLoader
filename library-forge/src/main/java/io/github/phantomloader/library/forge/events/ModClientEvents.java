package io.github.phantomloader.library.forge.events;

import io.github.phantomloader.library.events.ClientEventHandler;
import io.github.phantomloader.library.events.RegisterBlockEntityRenderersEvent;
import io.github.phantomloader.library.events.RegisterEntityRenderersEvent;
import io.github.phantomloader.library.events.RegisterParticlesEvent;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ServiceLoader;

/**
 * <p>
 *     Forge event handler for client events.
 * </p>
 *
 * @author Nico
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "phantom", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvents {

    /** Event handlers loaded using service loader */
    private static final ServiceLoader<ClientEventHandler> HANDLERS = ServiceLoader.load(ClientEventHandler.class);

    /**
     * <p>
     *     Forge event handler.
     * </p>
     *
     * @param event Forge event.
     */
    @SubscribeEvent
    public static void creativeTabEvent(BuildCreativeModeTabContentsEvent event) {
        for(ClientEventHandler handler : HANDLERS) {
            handler.addItemsToCreativeTab(event.getTabKey(), event::accept);
        }
    }

    /**
     * <p>
     *     Forge event handler.
     * </p>
     *
     * @param event Forge event.
     */
    @SubscribeEvent
    public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
        for(ClientEventHandler handler : HANDLERS) {
            handler.registerBlockEntityRenderers(new RegisterBlockEntityRenderersEventForge(event));
            handler.registerEntityRenderers(new RegisterEntityRenderersEventForge(event));
        }
    }

    /**
     * <p>
     *     Forge event handler.
     * </p>
     *
     * @param event Forge event.
     */
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        for(ClientEventHandler handler : HANDLERS) {
            handler.registerParticles(new RegisterParticlesEventForge(event));
        }
    }

    /**
     * <p>
     *     Implementation of {@link RegisterBlockEntityRenderersEvent}.
     *     Wraps a {@link EntityRenderersEvent.RegisterRenderers} for it to be used in the common module.
     * </p>
     *
     * @author Nico
     */
    public static class RegisterBlockEntityRenderersEventForge implements RegisterBlockEntityRenderersEvent {

        /** Forge event */
        private final EntityRenderersEvent.RegisterRenderers forgeEvent;

        /**
         * <p>
         *     Constructs an event.
         * </p>
         *
         * @param forgeEvent Forge event.
         */
        public RegisterBlockEntityRenderersEventForge(EntityRenderersEvent.RegisterRenderers forgeEvent) {
            this.forgeEvent = forgeEvent;
        }

        @Override
        public <T extends BlockEntity> void register(BlockEntityType<? extends T> blockEntity, BlockEntityRendererProvider<T> renderer) {
            this.forgeEvent.registerBlockEntityRenderer(blockEntity, renderer);
        }
    }

    /**
     * <p>
     *     Implementation of {@link RegisterEntityRenderersEvent}.
     *     Wraps a {@link EntityRenderersEvent.RegisterRenderers} for it to be used in the common module.
     * </p>
     *
     * @author Nico
     */
    public static class RegisterEntityRenderersEventForge implements RegisterEntityRenderersEvent {

        /** Forge event */
        private final EntityRenderersEvent.RegisterRenderers forgeEvent;

        /**
         * <p>
         *     Constructs an event.
         * </p>
         *
         * @param forgeEvent Forge event.
         */
        public RegisterEntityRenderersEventForge(EntityRenderersEvent.RegisterRenderers forgeEvent) {
            this.forgeEvent = forgeEvent;
        }

        @Override
        public <T extends Entity> void register(EntityType<? extends T> entity, EntityRendererProvider<T> renderer) {
            this.forgeEvent.registerEntityRenderer(entity, renderer);
        }
    }

    /**
     * <p>
     *     Implementation of {@link RegisterParticlesEvent}.
     *     Wraps a {@link RegisterParticleProvidersEvent} for it to be used in the common module.
     * </p>
     *
     * @author Nico
     */
    public static class RegisterParticlesEventForge implements RegisterParticlesEvent {

        /** Forge event */
        private final RegisterParticleProvidersEvent forgeEvent;

        /**
         * <p>
         *     Constructs an event.
         * </p>
         *
         * @param forgeEvent Forge event.
         */
        public RegisterParticlesEventForge(RegisterParticleProvidersEvent forgeEvent) {
            this.forgeEvent = forgeEvent;
        }

        @Override
        public <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
            this.forgeEvent.registerSpecial(type, provider);
        }
    }
}
