package com.example.test;

import io.github.phantomloader.library.ModEntryPoint;
import io.github.phantomloader.library.integration.FabricCustomEntryPoint;

public class TestMod {

    @ModEntryPoint
    public static void initialize() {

    }

    @ModEntryPoint
    @FabricCustomEntryPoint(name = "terrablender", interfaceName = "terrablender.api.TerraBlenderApi")
    public static void addTerraBlenderRegions() {

    }
}