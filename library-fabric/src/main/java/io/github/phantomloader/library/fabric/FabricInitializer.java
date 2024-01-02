package io.github.phantomloader.library.fabric;

import io.github.phantomloader.library.events.ModEventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import java.util.ServiceLoader;

public class FabricInitializer implements ModInitializer {

    @Override
    public void onInitialize() {
        for(ModEventHandler handler : ServiceLoader.load(ModEventHandler.class)) {
            handler.registerEntityAttributes((entity, attributes) -> FabricDefaultAttributeRegistry.register(entity.get(), attributes));
        }
    }
}
