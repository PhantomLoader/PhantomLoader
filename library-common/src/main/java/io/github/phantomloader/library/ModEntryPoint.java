package io.github.phantomloader.library;

import io.github.phantomloader.library.integration.FabricCustomEntryPoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * <p>
 *     Annotation used by Phantom Loader that denotes the entry point of a mod.
 *     The annotation processor will generate classes for Forge and Fabric that will call every method with this annotation.
 *     Those methods must be public static for them to be accessible from the mod loader's initializer class.
 * </p>
 * <ul>
 *     <li>In Forge, only one class is generated and these methods will be called from different fml setup events.</li>
 *     <li>In Fabric, one class for each side will be generated.</li>
 * </ul>
 * <p>
 *     Additionally, one may specify some entry points to only be called in a specific mod loader or only on the client or only on the server.
 * </p>
 * <p>
 *     Example usage:
 *     <pre>
 *         {@code @ModEntryPoint}
 *         public static void initialize() {
 *             REGISTRY.register();
 *         }
 *     </pre>
 *     <pre>
 *         {@code @ModEntryPoint(side = ModEntryPoint.Side.CLIENT)}
 *         public static void initializeClient() {
 *             ....
 *         }
 *     </pre>
 * </p>
 * <p>
 *     For custom entry points see {@link FabricCustomEntryPoint}.
 * </p>
 *
 * @author Nico
 */
@Target(ElementType.METHOD)
public @interface ModEntryPoint {

    /**
     * <p>
     *     Specifies that the annotated method may only be used by a specific mod loader.
     *     By default, entry point methods are called from both mod loaders.
     * </p>
     * <p>
     *     Default: {@link Loader#COMMON}.
     * </p>
     *
     * @return Whether this method should only be called in Forge, in Fabric, or both.
     */
    Loader modLoader() default Loader.COMMON;

    /**
     * <p>
     *     Specifies on which side this method may be called.
     *     By default, entry point methods are called in the common initializer.
     * </p>
     * <p>
     *     It is advised to keep client and common entry point methods in separate classes to avoid loading classes that are not needed on one side.
     * </p>
     * <p>
     *     Default: {@link Side#COMMON}.
     * </p>
     *
     * @return Whether this method should only be called on the client, on the server, or in the common setup
     */
    Side side() default Side.COMMON;

    /**
     * <p>
     *     Enum used to specify a mod loader with {@link ModEntryPoint#modLoader()}.
     * </p>
     *
     * @author Nico
     */
    enum Loader {
        /** Indicates both mod loaders */
        COMMON,
        /** Indicates Forge only */
        FORGE,
        /** Indicates Fabric only */
        FABRIC
    }

    /**
     * <p>
     *     Enum used to specify a side with {@link ModEntryPoint#side()}.
     * </p>
     *
     * @author Nico
     */
    enum Side {
        /**
         * <p>
         *     Indicates the common setup.
         * </p>
         * <ul>
         *     <li>In Forge, indicates the constructor of the class with the {@code net.minecraftforge.fml.common.Mod} annotation.</li>
         *     <li>In Fabric, indicates a class that implements the {@code net.fabricmc.api.ModInitializer} interface.</li>
         * </ul>
         */
        COMMON,
        /**
         * <p>
         *     Indicates the client setup.
         * </p>
         * <ul>
         *     <li>In Forge, indicates the {@code net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent} event.</li>
         *     <li>In Fabric, indicates a class that implements the {@code net.fabricmc.api.ClientModInitializer} interface.</li>
         * </ul>
         */
        CLIENT,
        /**
         * <p>
         *     Indicates the dedicated server setup.
         * </p>
         * <ul>
         *     <li>In Forge, indicates the {@code net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent} event.</li>
         *     <li>In Fabric, indicates a class that implements the {@code net.fabricmc.api.DedicatedServerModInitializer} interface.</li>
         * </ul>
         */
        SERVER
    }
}
