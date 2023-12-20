package io.github.phantomloader.library.fabric;

import io.github.phantomloader.library.AttributesRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

/**
 * Fabric mod initializer.
 *
 * @author Nico
 */
public class FabricInitializer implements ModInitializer {

	@Override
	public void onInitialize() {
		AttributesRegistry.registerAttributes(FabricDefaultAttributeRegistry::register);
	}
}
