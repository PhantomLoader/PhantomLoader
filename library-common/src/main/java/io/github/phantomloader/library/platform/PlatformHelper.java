package io.github.phantomloader.library.platform;

import java.util.NoSuchElementException;
import java.util.ServiceLoader;

public class PlatformHelper {

	private static final Platform PLATFORM = ServiceLoader.load(Platform.class)
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException("No platform has been defined in META-INF/services. Make sure you are using the correct version of the library mod for your mod loader."));

	public static String name() {
		return PLATFORM.platformName();
	}

	public static boolean isModLoaded(String mod) {
		return PLATFORM.isModLoaded(mod);
	}

	public static boolean isDevelopmentEnvironment() {
		return PLATFORM.isDevelopmentEnvironment();
	}
}
