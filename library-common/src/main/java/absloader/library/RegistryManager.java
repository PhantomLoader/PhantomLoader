package absloader.library;

import java.util.ServiceLoader;

public final class RegistryManager {

	private static String modId = "";

	public static ModRegistry create(String modId) {
		RegistryManager.modId = modId;
		return ServiceLoader.load(ModRegistry.class).findFirst().orElseThrow();
	}

	public static String modId() {
		if(modId.isEmpty() || modId.isBlank()) {
			throw new IllegalStateException("No registry was created using RegistryManager#create");
		}
		return modId;
	}
}
