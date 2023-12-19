package io.github.phantomloader.library.forge;

import io.github.phantomloader.library.ModRegistry;
import io.github.phantomloader.library.RegistryProvider;

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
