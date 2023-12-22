package io.github.phantomloader.library.platform;

public interface Platform {

	String platformName();

	boolean isModLoaded(String mod);

	boolean isDevelopmentEnvironment();
}
