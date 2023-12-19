package io.github.phantomloader.library.fabric;

import io.github.phantomloader.library.ModRegistry;
import io.github.phantomloader.library.RegistryProvider;

public class FabricRegistryProvider implements RegistryProvider {

	@Override
	public ModRegistry instantiate(String mod) {
		return new FabricRegistry(mod);
	}
}
