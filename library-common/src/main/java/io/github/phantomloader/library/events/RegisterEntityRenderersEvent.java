package io.github.phantomloader.library.events;

import io.github.phantomloader.library.registry.ModRegistry;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

/**
 * <p>
 *     Interface used to wrap an event to register an entity's renderer.
 * </p>
 *
 * @author Nico
 */
public interface RegisterEntityRenderersEvent {

    /**
     * <p>
     *     This method must be called from an event handler to register a block entity renderer.
     *     See {@link ClientEventHandler#registerEntityRenderers(RegisterEntityRenderersEvent)} for details.
     * </p>
     *
     * @param entity A supplier returning the registered entity type, the one returned by {@link ModRegistry#registerEntity(String, EntityType.Builder)}.
     * @param renderer A function returning the entity renderer. If you have created a class that extends {@link EntityRenderer}, this should be that class' constructor passed as a method reference.
     * @param <T> The entity type
     */
    <T extends Entity> void register(EntityType<? extends T> entity, EntityRendererProvider<T> renderer);
}
