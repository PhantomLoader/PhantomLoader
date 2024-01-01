package io.github.phantomloader.library.fabric.config;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.phantomloader.library.ModEntryPoint;
import io.github.phantomloader.library.config.ConfigBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 *     Fabric implementation of a {@link ConfigBuilder}.
 * </p>
 *
 * @author Nico
 */
public class FabricConfigBuilder implements ConfigBuilder {

    /** Logger used to log errors in case the config file could not be read/written*/
    private static final Logger LOGGER = Logger.getLogger("phantom");

    /** Config json representation */
    private final HashMap<String, Object> config = new HashMap<>();
    /** Stack used to implement categories */
    private final Stack<HashMap<String, Object>> currentCategory = new Stack<>();

    /**
     * <p>
     *     Constructs a new {@code FabricConfigBuilder}.
     *     Sets the current category to the root json object.
     * </p>
     */
    public FabricConfigBuilder() {
        this.currentCategory.push(this.config);
    }

    @Override
    public void beginCategory(String category) {
        HashMap<String, Object> map = new HashMap<>();
        this.currentCategory.peek().put(category, map);
        this.currentCategory.push(map);
    }

    @Override
    public Supplier<Boolean> define(String key, boolean defaultValue, String... comment) {
        HashMap<String, Object> category = this.currentCategory.peek();
        category.put(key, defaultValue);
        return () -> (Boolean) category.get(key);
    }

    @Override
    public Supplier<Integer> define(String key, int defaultValue, String... comment) {
        HashMap<String, Object> category = this.currentCategory.peek();
        category.put(key, defaultValue);
        return () -> (Integer) category.get(key);
    }

    @Override
    public Supplier<Integer> define(String key, int defaultValue, int min, int max, String... comment) {
        this.define(key, defaultValue, comment);
        HashMap<String, Object> category = this.currentCategory.peek();
        return () -> Math.max(min, Math.min(max, (Integer) category.get(key)));
    }

    @Override
    public Supplier<Double> define(String key, double defaultValue, String... comment) {
        HashMap<String, Object> category = this.currentCategory.peek();
        category.put(key, defaultValue);
        return () -> (Double) category.get(key);
    }

    @Override
    public Supplier<Double> define(String key, double defaultValue, double min, double max, String... comment) {
        this.define(key, defaultValue, comment);
        HashMap<String, Object> category = this.currentCategory.peek();
        return () -> Math.max(min, Math.min(max, (Double) category.get(key)));
    }

    @Override
    public Supplier<Long> define(String key, long defaultValue, String... comment) {
        HashMap<String, Object> category = this.currentCategory.peek();
        category.put(key, defaultValue);
        return () -> (Long) category.get(key);
    }

    @Override
    public Supplier<Long> define(String key, long defaultValue, long min, long max, String... comment) {
        this.define(key, defaultValue, comment);
        HashMap<String, Object> category = this.currentCategory.peek();
        return () -> Math.max(min, Math.min(max, (Long) category.get(key)));
    }

    @Override
    public Supplier<String> define(String key, String defaultValue, String... comment) {
        HashMap<String, Object> category = this.currentCategory.peek();
        category.put(key, defaultValue);
        return () -> category.get(key).toString();
    }

    @Override
    public void endCategory() {
        if(!this.currentCategory.isEmpty()) {
            this.currentCategory.pop();
        }
    }

    @Override
    public void register(String mod, ModEntryPoint.Side side) {
        Path configFile = Path.of(FabricLoader.getInstance().getConfigDir().toString(), mod + "-" + side.name().toLowerCase() + ".json");
        if(Files.exists(configFile)) {
            try(Reader reader = Files.newBufferedReader(configFile)) {
                Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
                HashMap<String, Object> loaded = new GsonBuilder().create().fromJson(reader, type);
                this.config.putAll(loaded);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Could not read config file " + configFile, e);
            }
        } else {
            try(Writer writer = Files.newBufferedWriter(configFile)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(this.config, writer);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Could not create config file " + configFile, e);
            }
        }
    }
}
