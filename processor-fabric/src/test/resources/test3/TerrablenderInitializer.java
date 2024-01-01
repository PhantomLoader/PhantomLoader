package com.example.test.fabric.integration;

public class TerrablenderInitializer implements terrablender.api.TerraBlenderApi {

    @Override
    public void onTerraBlenderInitialized() {
        com.example.test.TestMod.addTerraBlenderRegions();
    }
}