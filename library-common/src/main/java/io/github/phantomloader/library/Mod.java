package io.github.phantomloader.library;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * <p>
 *     Annotation used by Phantom Loader to generate loader specific code.
 * </p>
 * <p>
 *     A public static method in the main mod class in the common module must be annotated with the {@code @Mod}
 *     annotation to make the annotation processor generate the main mod class.
 * </p>
 * <ul>
 *     <li> For Forge, this is a class annotated with the {@code @net.minecraftforge.fml.common.Mod} annotation. </li>
 *     <li> For Fabric, this is a class that implements the {@code net.fabricmc.api.ModInitializer} interface. </li>
 * </ul>
 * <p>
 *     The generated classes will be put in a subpackage of the main mod class' packaged called either {@code forge}
 *     or {@code fabric}.
 * </p>
 * <p>
 *     The annotated method will be the first one to be called when the mod is created and should be used for
 *     initialization and should call {@link ModRegistry#register()}.
 * </p>
 *
 * @author Nico
 */
@Target(ElementType.METHOD)
public @interface Mod {

	/**
	 * <p>
	 *     The unique identifier of your mod.
	 * </p>
	 * <p>
	 *     Required to be lowercase and at most 64 characters long. Cannot be an empty or blank string.
	 * </p>
	 *
	 * @return The mod id of your mod
	 */
	String id();

	/**
	 * <p>
	 *     Your mod's version. Should follow the software versioning conventions to allow mod loaders to properly
	 *     detect your mod's compatibility with its dependencies and with Minecraft.
	 * </p>
	 * <p>
	 *     Will default to {@code ${file.jarVersion}} in Forge and to {@code ${version}} in Fabric if left empty.
	 * </p>
	 *
	 * @return The version of your mod
	 */
	String version() default "";

	/**
	 * <p>
	 *     Your mod's display name. Can be any string.
	 * </p>
	 * <p>
	 *     Will be used in the mod info file, {@code META-INF/mods.toml} in Forge and {@code fabric.mod.json} in Fabric.
	 * </p>
	 *
	 * @return The display name of your mod
	 */
	String name() default "";

	/**
	 * <p>
	 *     Your mod's description.
	 * </p>
	 * <p>
	 *     Will be used in the mod info file, {@code META-INF/mods.toml} in Forge and {@code fabric.mod.json} in Fabric.
	 * </p>
	 *
	 * @return The description of your mod
	 */
	String description() default "";

	/**
	 * <p>
	 *     An array containing the names of the authors of your mod.
	 * </p>
	 * <p>
	 *     Will be used in the mod info file, {@code META-INF/mods.toml} in Forge and {@code fabric.mod.json} in Fabric.
	 * </p>
	 *
	 * @return An array containing the names of the authors of your mod
	 */
	String[] authors() default {};

	/**
	 * <p>
	 *     Additional credits for your mod can go here.
	 * </p>
	 * <p>
	 *     Will be used in Forge's {@code META-INF/mods.toml} file.
	 * </p>
	 *
	 * @return Additional credits for your mod
	 */
	String credits() default "";

	/**
	 * <p>
	 *     The homepage or display URL of your mod. Should be a URL pointing to your project's website or repository.
	 * </p>
	 * <p>
	 *     This will be your display URL in Forge's {@code META-INF/mods.toml} file and your homepage URL in Fabric's
	 *     {@code fabric.mod.json} file.
	 * </p>
	 * <p>
	 *     In Forge, this will default to {@link Mod#sources()} if left empty.
	 * </p>
	 *
	 * @return The homepage URL of your mod
	 */
	String homepage() default "";

	/**
	 * <p>
	 *     A URL pointing to your mod's repository.
	 * </p>
	 * <p>
	 *     Will be used in Fabric's {@code fabric.mod.json} file as your mod's sources contact.
	 * </p>
	 * <p>
	 *     Will also be used as Forge's display URL if {@link Mod#homepage()} is left empty.
	 * </p>
	 *
	 * @return A URL pointing to your mod's repository
	 */
	String sources() default "";

	/**
	 * <p>
	 *     A URL pointing to your mod's issue tracker.
	 * </p>
	 * <p>
	 *     Will be used in the mod info file, {@code META-INF/mods.toml} in Forge and {@code fabric.mod.json} in Fabric.
	 * </p>
	 *
	 * @return A URL pointing to your mod's issue tracker
	 */
	String issueTracker() default "";

	/**
	 * <p>
	 *     Your mod's license. Can be any string.
	 * </p>
	 * <p>
	 *     Will be used in the mod info file, {@code META-INF/mods.toml} in Forge and {@code fabric.mod.json} in Fabric.
	 * </p>
	 *
	 * @return The license of your mod
	 */
	String license() default "All rights reserved";

	/**
	 * <p>
	 *     A file path pointing to your mod's logo image in the {@code resources} folder.
	 * </p>
	 * <p>
	 *     Will be used in Forge's {@code META-INF/mods.toml} file.
	 * </p>
	 *
	 * @return A file path pointing to your mod's logo image
	 */
	String logo() default "logo.png";

	/**
	 * <p>
	 *     A file path pointing to your mod's image icon in the {@code resources} folder.
	 * </p>
	 * <p>
	 *     Will be used in Fabric's {@code fabric.mod.json} file.
	 * </p>
	 *
	 * @return A file path pointing to your mod's image icon
	 */
	String icon() default "icon.png";
}
