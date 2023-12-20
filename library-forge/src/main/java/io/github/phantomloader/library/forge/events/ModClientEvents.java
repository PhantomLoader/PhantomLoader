package io.github.phantomloader.library.forge.events;

import io.github.phantomloader.library.CreativeTabs;
import io.github.phantomloader.library.RenderersRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * <p>
 *     Forge event handler used for client events.
 * </p>
 *
 * @author Nico
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = "phantom", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

	/**
	 * <p>
	 *     Forge event handler used to add items to creative mode tabs.
	 * </p>
	 *
	 * @param event Forge event
	 */
	@SubscribeEvent
	public static void creativeTabEvent(BuildCreativeModeTabContentsEvent event) {
		CreativeTabs.accept(event.getTabKey(), event::accept);
	}

	/**
	 * <p>
	 *     Forge event handler used to register entity and block entity renderers.
	 * </p>
	 *
	 * @param event Forge event
	 */
	@SubscribeEvent
	public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
		RenderersRegistry.registerEntityRenderers(event::registerEntityRenderer);
		RenderersRegistry.registerBlockEntityRenderers(event::registerBlockEntityRenderer);
	}
}
