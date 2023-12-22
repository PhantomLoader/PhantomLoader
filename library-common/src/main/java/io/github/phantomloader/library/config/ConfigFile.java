package io.github.phantomloader.library.config;

import java.util.function.Supplier;

public interface ConfigFile {

	void beginCategory(String category);

	Supplier<Boolean> define(String key, boolean defaultValue, String... comment);

	Supplier<Integer> define(String key, int defaultValue, String... comment);

	default Supplier<Integer> define(String key, int defaultValue, int max, int min, String... comment) {
		return this.define(key, defaultValue, comment);
	}

	Supplier<Double> define(String key, double defaultValue, String... comment);

	default Supplier<Double> define(String key, double defaultValue, double max, double min, String... comment) {
		return this.define(key, defaultValue, comment);
	}

	Supplier<Long> define(String key, long defaultValue, String... comment);

	default Supplier<Long> define(String key, long defaultValue, long max, long min, String... comment) {
		return this.define(key, defaultValue, comment);
	}

	Supplier<String> define(String key, String defaultValue, String... comment);

	void endCategory();

	void register(String mod);
}
