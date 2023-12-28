package io.github.phantomloader.example.integration;

import io.github.phantomloader.library.ModEntryPoint;
import io.github.phantomloader.library.integration.FabricCustomEntryPoint;

public class IntegrationHelper {

	@ModEntryPoint
	@FabricCustomEntryPoint(name = "terrablender", interfaceName = "terrablender.api.TerraBlenderApi")
	public static void addTerraBlenderRegions() {

	}
}
