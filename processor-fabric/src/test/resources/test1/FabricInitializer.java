package com.example.test.fabric;

public class FabricInitializer implements net.fabricmc.api.ModInitializer {

	@Override
	public void onInitialize() {
		com.example.test.TestMod.initialize();
	}
}