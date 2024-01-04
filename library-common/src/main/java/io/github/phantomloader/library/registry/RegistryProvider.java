package io.github.phantomloader.library.registry;

import java.util.ServiceLoader;

/**
 * <p>
 *     Interface loaded using {@link ServiceLoader} to allow loader-specific modules to create their {@link ModRegistry}.
 * </p>
 *
 * @see ModRegistry#instantiate(String)
 *
 * @author Nico
 */
public interface RegistryProvider {

    /**
     * <p>
     *     Instantiates a {@link ModRegistry}.
     *     Loader-specific implementations of this interface should return the corresponding registry.
     * </p>
     *
     * @param mod Your mod id.
     * @return An instance of {@code ModRegistry} for the current loader.
     */
    ModRegistry instantiate(String mod);
}
