package io.github.phantomloader.library.fabric.config;

import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import io.github.phantomloader.library.config.ConfigFile;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.function.Supplier;

public class FabricConfigFile implements ConfigFile {

	private HashMap<String, Object> config = new HashMap<>();

	@Override
	public void beginCategory(String category) {
		// TODO: Add categories
	}

	@Override
	public Supplier<Boolean> define(String key, boolean defaultValue, String... comment) {
		this.config.put(key, defaultValue);
		return () -> (Boolean) this.config.get(key);
	}

	@Override
	public Supplier<Integer> define(String key, int defaultValue, String... comment) {
		this.config.put(key, defaultValue);
		return () -> (Integer) this.config.get(key);
	}

	@Override
	public Supplier<Double> define(String key, double defaultValue, String... comment) {
		this.config.put(key, defaultValue);
		return () -> (Double) this.config.get(key);
	}

	@Override
	public Supplier<Long> define(String key, long defaultValue, String... comment) {
		this.config.put(key, defaultValue);
		return () -> (Long) this.config.get(key);
	}

	@Override
	public Supplier<String> define(String key, String defaultValue, String... comment) {
		this.config.put(key, defaultValue);
		return () -> this.config.get(key).toString();
	}

	@Override
	public void endCategory() {

	}

	@Override
	public void register(String mod) {
		Path configFile = Path.of(FabricLoader.getInstance().getConfigDir().toString(), mod + ".json");
		if(Files.exists(configFile)) {
			try(Reader reader = Files.newBufferedReader(configFile)) {
				Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
				this.config = new GsonBuilder().create().fromJson(reader, type);
			} catch (IOException e) {
				// TODO: Better exception handling
				throw new UncheckedIOException(e);
			}
		} else {
			try(Writer writer = Files.newBufferedWriter(configFile)) {
				new GsonBuilder().setPrettyPrinting().create().toJson(this.config, writer);
			} catch (IOException e) {
				// TODO: Better exception handling
				throw new UncheckedIOException(e);
			}
		}
	}
}
