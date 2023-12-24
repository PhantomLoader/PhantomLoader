package io.github.phantomloader.library.platform;

/**
 * <p>
 *     Interface used to implement platform-specific methods that are needed in the common module.
 *     Such methods can be accessed anywhere using the {@link PlatformHelper} class.
 * </p>
 *
 * @author Nico
 */
public interface Platform {

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
	 * @see PlatformHelper#name()
	 */
	String platformName();

	/**
	 * <p>
	 *     Checks if a mod with the given mod id is currently loaded.
	 *     Useful for creating mods that have optional dependencies.
	 * </p>
	 *
	 * @param mod Id of the mod to look for.
	 * @return True if a mod with the given id is loaded, otherwise false.
	 * @see PlatformHelper#isModLoaded(String)
	 */
	boolean isModLoaded(String mod);

	/**
	 * <p>
	 *     Checks if the game is currently running from the development environment.
	 * </p>
	 *
	 * @return True if the game is running from an IDE, otherwise false.
	 * @see PlatformHelper#isDevelopmentEnvironment()
	 */
	boolean isDevelopmentEnvironment();
}
