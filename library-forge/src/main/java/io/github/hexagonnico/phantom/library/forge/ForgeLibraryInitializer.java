package io.github.hexagonnico.phantom.library.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("phantom")
public class ForgeLibraryInitializer {

	public ForgeLibraryInitializer() {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
