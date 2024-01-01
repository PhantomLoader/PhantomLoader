package io.github.phantomloader.library.forge.registry;

import io.github.phantomloader.library.registry.ModRegistry;
import io.github.phantomloader.library.registry.RegistryProvider;

/**
 * <p>
 *     Forge implementation of {@link RegistryProvider} that returns an instance of {@link ForgeRegistry}.
 * </p>
 *
 * @author Nico
 */
public class ForgeRegistryProvider implements RegistryProvider {

    @Override
    public ModRegistry instantiate(String mod) {
        return new ForgeRegistry(mod);
    }
}
