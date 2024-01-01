package com.example.test;

import io.github.phantomloader.library.ModEntryPoint;

public class TestMod {

    @ModEntryPoint
    public static void initialize() {

    }

    @ModEntryPoint(side = ModEntryPoint.Side.CLIENT)
    public static void initializeClient() {

    }
}