package io.github.phantomloader.library.forge.events;

import io.github.phantomloader.library.events.ModEventHandler;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ServiceLoader;

@Mod.EventBusSubscriber(modid = "phantom", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    /** Event handlers loaded using service loader */
    private static final ServiceLoader<ModEventHandler> HANDLERS = ServiceLoader.load(ModEventHandler.class);

    /**
     * <p>
     *     Forge event used to register entity attributes.
     * </p>
     *
     * @param event Forge event.
     */
    @SubscribeEvent
    public static void onCreateAttributesEvent(EntityAttributeCreationEvent event) {
        for(ModEventHandler handler : HANDLERS) {
            handler.registerEntityAttributes((entity, attributes) -> event.put(entity, attributes.build()));
        }
    }
}
