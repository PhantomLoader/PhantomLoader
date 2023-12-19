package io.github.phantomloader.library.forge;

import io.github.phantomloader.library.ModRegistry;
import io.github.phantomloader.library.RegistryProvider;

public class ForgeRegistryProvider implements RegistryProvider {

	@Override
	public ModRegistry instantiate(String mod) {
		return new ForgeRegistry(mod);
	}
}
