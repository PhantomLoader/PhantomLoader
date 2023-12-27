package io.github.phantomloader.example;

import io.github.phantomloader.library.ModInitializer;
import io.github.phantomloader.library.config.ConfigBuilder;

import java.util.function.Supplier;

public class ExampleConfig {

	private static final ConfigBuilder BUILDER = ConfigBuilder.instantiate();

	public static final Supplier<Boolean> BOOLEAN_OPTION;
	public static final Supplier<Integer> INT_OPTION;

	public static final Supplier<Integer> INT_RANGE;
	public static final Supplier<Double> DOUBLE_RANGE;

	public static final Supplier<String> STRING_OPTION;

	static {
		BUILDER.beginCategory("category");
		BOOLEAN_OPTION = BUILDER.define("testOption", true, "This is an example config option");
		INT_OPTION = BUILDER.define("count", 0, "This is an example int option");
		BUILDER.endCategory();
		BUILDER.beginCategory("ranges");
		INT_RANGE = BUILDER.define("rangeOne", 0, 0, 64, "This is an example int option with range");
		DOUBLE_RANGE = BUILDER.define("rangeTwo", 0.5, 0.0, 1.0, "This is an example double option with range");
		BUILDER.endCategory();
		STRING_OPTION = BUILDER.define("string", "Hello", "This is a string option outside of any section");
	}

	@ModInitializer
	public static void register() {
		BUILDER.register("example");
	}
}
