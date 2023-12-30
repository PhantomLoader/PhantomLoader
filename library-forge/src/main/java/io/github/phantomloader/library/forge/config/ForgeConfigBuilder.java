package io.github.phantomloader.library.forge.config;

import io.github.phantomloader.library.ModEntryPoint;
import io.github.phantomloader.library.config.ConfigBuilder;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.function.Supplier;

/**
 * <p>
 *     Forge implementation of a {@link ConfigBuilder}.
 * </p>
 *
 * @author Nico
 */
public class ForgeConfigBuilder implements ConfigBuilder {

	/** Forge's config builder */
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
	public Supplier<Integer> define(String key, int defaultValue, int min, int max, String... comment) {
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
	public Supplier<Double> define(String key, double defaultValue, double min, double max, String... comment) {
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
	public Supplier<Long> define(String key, long defaultValue, long min, long max, String... comment) {
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
	public void register(String mod, ModEntryPoint.Side side) {
		ModLoadingContext.get().registerConfig(typeFromSide(side), builder.build());
	}

	/**
	 * <p>
	 *     Converts {@link ModEntryPoint.Side} to {@link ModConfig.Type}.
	 * </p>
	 *
	 * @param side Side.
	 * @return Mod config type.
	 */
	private static ModConfig.Type typeFromSide(ModEntryPoint.Side side) {
		return switch (side) {
			case COMMON -> ModConfig.Type.COMMON;
			case CLIENT -> ModConfig.Type.CLIENT;
			case SERVER -> ModConfig.Type.SERVER;
		};
	}
}
