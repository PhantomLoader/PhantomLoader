package com.example.test.fabric;

public class FabricClientInitializer implements net.fabricmc.api.ClientModInitializer {

    @Override
    public void onInitializeClient() {
        com.example.test.TestMod.initializeClient();
    }
}