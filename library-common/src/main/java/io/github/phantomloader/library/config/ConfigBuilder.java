package io.github.phantomloader.library.config;

import java.util.ServiceLoader;
import java.util.function.Supplier;

public interface ConfigBuilder {

	static ConfigBuilder instantiate() {
		// TODO: Better error handling
		return ServiceLoader.load(ConfigBuilder.class).findFirst().orElseThrow();
	}

	void beginCategory(String category);

	Supplier<Boolean> define(String key, boolean defaultValue, String... comment);

	Supplier<Integer> define(String key, int defaultValue, String... comment);

	default Supplier<Integer> define(String key, int defaultValue, int min, int max, String... comment) {
		return this.define(key, defaultValue, comment);
	}

	Supplier<Double> define(String key, double defaultValue, String... comment);

	default Supplier<Double> define(String key, double defaultValue, double min, double max, String... comment) {
		return this.define(key, defaultValue, comment);
	}

	Supplier<Long> define(String key, long defaultValue, String... comment);

	default Supplier<Long> define(String key, long defaultValue, long min, long max, String... comment) {
		return this.define(key, defaultValue, comment);
	}

	Supplier<String> define(String key, String defaultValue, String... comment);

	void endCategory();

	void register(String mod);
}
