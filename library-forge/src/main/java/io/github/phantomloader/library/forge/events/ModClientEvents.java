package io.github.phantomloader.library.forge.events;

import io.github.phantomloader.library.CreativeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = "phantom", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

	@SubscribeEvent
	public static void creativeTabEvent(BuildCreativeModeTabContentsEvent event) {
		CreativeTabs.accept(event.getTabKey(), event::accept);
	}
}
