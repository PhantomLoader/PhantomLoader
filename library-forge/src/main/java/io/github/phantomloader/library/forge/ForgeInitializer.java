package io.github.phantomloader.library.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("phantom")
public class ForgeInitializer {

	public ForgeInitializer() {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
