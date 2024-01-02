package io.github.phantomloader.library.config;

import io.github.phantomloader.library.ModEntryPoint;

import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * <p>
 *     Interface used to create config files that can be used by any loader.
 * </p>
 * <p>
 *     A {@code ConfigBuilder} can be instantiated with {@link ConfigBuilder#instantiate()}.
 *     The method will return an instance of a {@code ConfigBuilder} for the current mod loader.
 * </p>
 * <ul>
 *     <li>In Forge, this uses Forge's built-in config system.</li>
 *     <li>In Fabric, this will create a json file in the config folder.</li>
 * </ul>
 * <p>
 *     Config files are normally created from static initializers, so that options can be retrieved from anywhere in the code.
 *     After defining the config options, the config must be registered by calling {@link ConfigBuilder#register(String, ModEntryPoint.Side)}.
 * </p>
 *
 * @author Nico
 */
public interface ConfigBuilder {

    /**
     * <p>
     *     Returns an instance of {@link ConfigBuilder} for the current mod loader.
     *     The object returned can be used to define the properties of a config file.
     * </p>
     * <p>
     *     Config files are normally created from static initializers, so that options can be retrieved from anywhere in the code.
     *     After defining the config options, the config must be registered by calling {@link ConfigBuilder#register(String, ModEntryPoint.Side)}.
     * </p>
     *
     * @return An instance of {@code ConfigBuilder} for the current mod loader.
     * @throws NoSuchElementException If no {@code ConfigBuilder} has been defined in {@code META-INF/services}.
     */
    static ConfigBuilder instantiate() {
        return ServiceLoader.load(ConfigBuilder.class)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No config builder has been defined in META-INF/services. Make sure you are using the correct version of the library mod for your mod loader."));
    }

    /**
     * <p>
     *     Defines a config category. All options defined after this call will be in that category.
     * </p>
     *
     * @param category Name of the category
     */
    void beginCategory(String category);

    /**
     * <p>
     *     Defines a boolean config option.
     * </p>
     *
     * @param key The option's key
     * @param defaultValue The option's default value
     * @param comment Comment to append before the option
     * @return A supplier returning the value of the defined config option
     */
    Supplier<Boolean> define(String key, boolean defaultValue, String... comment);

    /**
     * <p>
     *     Defines an integer config option.
     * </p>
     *
     * @param key The option's key
     * @param defaultValue The option's default value
     * @param comment Comment to append before the option
     * @return A supplier returning the value of the defined config option
     */
    Supplier<Integer> define(String key, int defaultValue, String... comment);

    /**
     * <p>
     *     Defines an integer config option with a range.
     * </p>
     *
     * @param key The option's key
     * @param defaultValue The option's default value
     * @param min The minimum value
     * @param max The maximum value
     * @param comment Comment to append before the option
     * @return A supplier returning the value of the defined config option
     */
    default Supplier<Integer> define(String key, int defaultValue, int min, int max, String... comment) {
        return this.define(key, defaultValue, comment);
    }

    /**
     * <p>
     *     Defines a double config option.
     * </p>
     *
     * @param key The option's key
     * @param defaultValue The option's default value
     * @param comment Comment to append before the option
     * @return A supplier returning the value of the defined config option
     */
    Supplier<Double> define(String key, double defaultValue, String... comment);

    /**
     * <p>
     *     Defines a double config option with a range.
     * </p>
     *
     * @param key The option's key
     * @param defaultValue The option's default value
     * @param min The minimum value
     * @param max The maximum value
     * @param comment Comment to append before the option
     * @return A supplier returning the value of the defined config option
     */
    default Supplier<Double> define(String key, double defaultValue, double min, double max, String... comment) {
        return this.define(key, defaultValue, comment);
    }

    /**
     * <p>
     *     Defines a long config option.
     * </p>
     *
     * @param key The option's key
     * @param defaultValue The option's default value
     * @param comment Comment to append before the option
     * @return A supplier returning the value of the defined config option
     */
    Supplier<Long> define(String key, long defaultValue, String... comment);

    /**
     * <p>
     *     Defines a long config option with a range.
     * </p>
     *
     * @param key The option's key
     * @param defaultValue The option's default value
     * @param min The minimum value
     * @param max The maximum value
     * @param comment Comment to append before the option
     * @return A supplier returning the value of the defined config option
     */
    default Supplier<Long> define(String key, long defaultValue, long min, long max, String... comment) {
        return this.define(key, defaultValue, comment);
    }

    /**
     * <p>
     *     Defines a string config option.
     * </p>
     *
     * @param key The option's key
     * @param defaultValue The option's default value
     * @param comment Comment to append before the option
     * @return A supplier returning the value of the defined config option
     */
    Supplier<String> define(String key, String defaultValue, String... comment);

    /**
     * <p>
     *     Ends a category. All options defined after this call will belong to the previous category.
     * </p>
     */
    void endCategory();

    /**
     * <p>
     *     Registers, creates, or loads this config file.
     * </p>
     * <ul>
     *     <li>In Forge, this registers the config builder in Forge's built-in config system.</li>
     *     <li>In Fabric, this loads a json file with the given name or creates one if it does not exist.</li>
     * </ul>
     *
     * @param mod Name of the config file used in Fabric
     * @param side Whether this is a client, common, or server config file.
     */
    void register(String mod, ModEntryPoint.Side side);
}
