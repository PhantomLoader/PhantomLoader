package io.github.phantomloader.library.fabric.registry;

import io.github.phantomloader.library.registry.ModRegistry;
import io.github.phantomloader.library.registry.RegistryProvider;

/**
 * <p>
 *     Fabric implementation of {@link RegistryProvider} that returns an instance of {@link FabricRegistry}.
 * </p>
 *
 * @author Nico
 */
public class FabricRegistryProvider implements RegistryProvider {

	@Override
	public ModRegistry instantiate(String mod) {
		return new FabricRegistry(mod);
	}
}
