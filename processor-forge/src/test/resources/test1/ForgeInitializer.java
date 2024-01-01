package com.example.test.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("test")
public class ForgeInitializer {

	public ForgeInitializer() {
		com.example.test.TestMod.initialize();
		MinecraftForge.EVENT_BUS.register(this);
	}
}