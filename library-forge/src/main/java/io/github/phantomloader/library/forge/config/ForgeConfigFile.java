package io.github.phantomloader.library.forge.config;

import io.github.phantomloader.library.config.ConfigFile;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.function.Supplier;

public class ForgeConfigFile implements ConfigFile {

	private final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

	@Override
	public void beginCategory(String category) {
		this.builder.push(category);
	}

	@Override
	public Supplier<Boolean> define(String key, boolean defaultValue, String... comment) {
		if(comment != null && comment.length > 0) {
			this.builder.comment(comment);
		}
		return this.builder.define(key, defaultValue);
	}

	@Override
	public Supplier<Integer> define(String key, int defaultValue, String... comment) {
		if(comment != null && comment.length > 0) {
			this.builder.comment(comment);
		}
		return this.builder.define(key, defaultValue);
	}

	@Override
	public Supplier<Integer> define(String key, int defaultValue, int max, int min, String... comment) {
		if(comment != null && comment.length > 0) {
			this.builder.comment(comment);
		}
		return this.builder.defineInRange(key, defaultValue, min, max);
	}

	@Override
	public Supplier<Double> define(String key, double defaultValue, String... comment) {
		if(comment != null && comment.length > 0) {
			this.builder.comment(comment);
		}
		return this.builder.define(key, defaultValue);
	}

	@Override
	public Supplier<Double> define(String key, double defaultValue, double max, double min, String... comment) {
		if(comment != null && comment.length > 0) {
			this.builder.comment(comment);
		}
		return this.builder.defineInRange(key, defaultValue, min, max);
	}

	@Override
	public Supplier<Long> define(String key, long defaultValue, String... comment) {
		if(comment != null && comment.length > 0) {
			this.builder.comment(comment);
		}
		return this.builder.define(key, defaultValue);
	}

	@Override
	public Supplier<Long> define(String key, long defaultValue, long max, long min, String... comment) {
		if(comment != null && comment.length > 0) {
			this.builder.comment(comment);
		}
		return this.builder.defineInRange(key, defaultValue, min, max);
	}

	@Override
	public Supplier<String> define(String key, String defaultValue, String... comment) {
		if(comment != null && comment.length > 0) {
			this.builder.comment(comment);
		}
		return this.builder.define(key, defaultValue);
	}

	@Override
	public void endCategory() {
		this.builder.pop();
	}

	@Override
	public void register(String mod) {
		// TODO: Differentiate between client and common
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
	}
}
