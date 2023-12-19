package io.github.phantomloader.library.fabric;

import io.github.phantomloader.library.ModRegistry;
import io.github.phantomloader.library.RegistryProvider;

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
