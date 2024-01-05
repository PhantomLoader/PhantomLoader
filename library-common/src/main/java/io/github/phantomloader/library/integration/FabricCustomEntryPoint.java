package io.github.phantomloader.library.integration;

import io.github.phantomloader.library.ModEntryPoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * <p>
 *     Annotation to use along {@link ModEntryPoint} to specify additional data for the Fabric entry point.
 * </p>
 * <p>
 *     Some library mods may use custom entry point other than {@code "main"}, {@code "client"}, or {@code "server"} for Fabric.
 *     This annotation tells Phantom Loader to generate an additional class with the given data to be used as an entry point.
 * </p>
 * <pre>
 *     {@code @ModEntryPoint}
 *     {@code @FabricCustomEntryPoint(name = "terrablender", interfaceName = "terrablender.api.TerraBlenderApi")}
 *     public static void addTerraBlenderRegions() {
 *         ...
 *     }
 * </pre>
 * <p>
 *     Note that the value of {@link ModEntryPoint#side()} will be ignored if this annotation is used.
 * </p>
 *
 * @author Nico
 */
@Target(ElementType.METHOD)
public @interface FabricCustomEntryPoint {

    /**
     * <p>
     *     The name of the entry point that will be used in the {@code entrypoints} section of the {@code fabric.mod.json} file.
     * </p>
     *
     * @return The name of the entry point.
     */
    String name();

    /**
     * <p>
     *     The fully qualified name of the interface that will be implemented by the generated class.
     *     For example, {@code "terrablender.api.TerraBlenderApi"}.
     * </p>
     * <p>
     *     The generated class will only implement one of the methods of this interface, therefore such interface must only have one method with no arguments like the one in {@code net.fabricmc.api.ModInitializer}.
     * </p>
     *
     * @return The fully qualified name of the interface that will be implemented by the generated class.
     */
    String interfaceName();
}
