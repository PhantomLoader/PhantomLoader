package io.github.phantomloader.library.events;

import io.github.phantomloader.library.registry.ModRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Supplier;

/**
 * <p>
 *     Interface used to wrap an event to register a particle renderer.
 * </p>
 *
 * @author Nico
 */
public interface RegisterParticlesEvent {

    /**
     * <p>
     *     This method must be called from an event handler to register a particle renderer.
     *     See {@link ClientEventHandler#registerParticles(RegisterParticlesEvent)} for details.
     * </p>
     *
     * @param type A supplier returning the registered particles, the one returned by {@link ModRegistry#registerParticles(String, Supplier)}.
     * @param provider A function returning the particle renderer. If you have created a class that extends {@link ParticleOptions}, this should be that class' constructor passed as a method reference.
     * @param <T> The particles type
     */
    <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider);
}
