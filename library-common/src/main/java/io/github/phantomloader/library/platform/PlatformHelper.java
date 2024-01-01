package io.github.phantomloader.library.platform;

import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * <p>
 *     Helper class containing platform-specific methods that are needed in the common module.
 * </p>
 * <p>
 *     Note that the correct version of the Phantom library for the current loader must be present for this class to work, otherwise a {@link NoSuchElementException} will be thrown from the initializer.
 *     Alternatively, one must create a class that implements the {@link Platform} interface and declare it as a service.
 * </p>
 *
 * @author Nico
 */
public class PlatformHelper {

    /** Instance of the current platform */
    private static final Platform PLATFORM = ServiceLoader.load(Platform.class)
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("No platform has been defined in META-INF/services. Make sure you are using the correct version of the library mod for your mod loader."));

    /**
     * <p>
     *     Returns a string containing the name of the current platform.
     * </p>
     * <ul>
     *     <li>For Forge, this is the string "Forge".</li>
     *     <li>For Fabric, this is the string "Fabric".</li>
     * </ul>
     *
     * @return A string containing the name of the current platform.
     */
    public static String name() {
        return PLATFORM.platformName();
    }

    /**
     * <p>
     *     Checks if a mod with the given mod id is currently loaded.
     *     Useful for creating mods that have optional dependencies.
     * </p>
     *
     * @param mod Id of the mod to look for.
     * @return True if a mod with the given id is loaded, otherwise false.
     */
    public static boolean isModLoaded(String mod) {
        return PLATFORM.isModLoaded(mod);
    }

    /**
     * <p>
     *     Checks if the game is currently running from the development environment.
     * </p>
     *
     * @return True if the game is running from an IDE, otherwise false.
     */
    public static boolean isDevelopmentEnvironment() {
        return PLATFORM.isDevelopmentEnvironment();
    }
}
