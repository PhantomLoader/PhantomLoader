package io.github.phantomloader.library.fabric;

import io.github.phantomloader.library.events.ModEventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import java.util.ServiceLoader;

/**
 * <p>
 *     Fabric initializer used by the Phantom Library mod.
 * </p>
 *
 * @author Nico
 */
public class FabricInitializer implements ModInitializer {

    @Override
    public void onInitialize() {
        for(ModEventHandler handler : ServiceLoader.load(ModEventHandler.class)) {
            handler.registerEntityAttributes(FabricDefaultAttributeRegistry::register);
        }
    }
}
