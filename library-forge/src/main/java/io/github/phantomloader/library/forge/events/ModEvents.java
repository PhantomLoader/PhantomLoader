package io.github.phantomloader.library.forge.events;

import io.github.phantomloader.library.utils.ModEntityAttributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = "underground_jungle", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

	@SubscribeEvent
	public static void createAttributes(EntityAttributeCreationEvent event) {
		ModEntityAttributes.registerAttributes(event::put);
	}
}
