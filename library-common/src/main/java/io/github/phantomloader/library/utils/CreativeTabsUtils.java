package io.github.phantomloader.library.utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.stream.Stream;

/**
 * <p>
 *     Utility class used to store {@link CreativeModeTab} keys.
 * </p>
 *
 * @author Nico
 */
public class CreativeTabsUtils {

    /** Building blocks creative tab */
    public static final ResourceKey<CreativeModeTab> BUILDING_BLOCKS = createKey("building_blocks");
    /** Colored blocks creative tab */
    public static final ResourceKey<CreativeModeTab> COLORED_BLOCKS = createKey("colored_blocks");
    /** Natural block creative tab */
    public static final ResourceKey<CreativeModeTab> NATURAL_BLOCKS = createKey("natural_blocks");
    /** Functional blocks creative tab */
    public static final ResourceKey<CreativeModeTab> FUNCTIONAL_BLOCKS = createKey("functional_blocks");
    /** Redstone blocks creative tab */
    public static final ResourceKey<CreativeModeTab> REDSTONE_BLOCKS = createKey("redstone_blocks");
    /** Tools and utilities creative tab */
    public static final ResourceKey<CreativeModeTab> TOOLS_AND_UTILITIES = createKey("tools_and_utilities");
    /** Combat creative tab */
    public static final ResourceKey<CreativeModeTab> COMBAT = createKey("combat");
    /** Food and potions creative tab */
    public static final ResourceKey<CreativeModeTab> FOOD_AND_DRINKS = createKey("food_and_drinks");
    /** Materials creative tab */
    public static final ResourceKey<CreativeModeTab> INGREDIENTS = createKey("ingredients");
    /** Spawn eggs creative tab */
    public static final ResourceKey<CreativeModeTab> SPAWN_EGGS = createKey("spawn_eggs");

    /**
     * <p>
     *     Creates a {@link ResourceKey} for a vanilla creative mode tab.
     * </p>
     *
     * @param name Tab key name.
     * @return The requested resource key.
     */
    public static ResourceKey<CreativeModeTab> createKey(String name) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(name));
    }

    /**
     * <p>
     *     Creates a {@link ResourceKey} for a modded creative mode tab.
     * </p>
     *
     * @param modId The mod id of the mod.
     * @param name Tab key name.
     * @return The requested resource key.
     */
    public static ResourceKey<CreativeModeTab> createKey(String modId, String name) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(modId, name));
    }

    /**
     * <p>
     *     Returns a {@link Stream} of all the vanilla creative tabs listed in {@link CreativeTabsUtils}.
     * </p>
     *
     * @return A {@code Stream} of creative tab resource keys.
     */
    public static Stream<ResourceKey<CreativeModeTab>> allTabKeys() {
        return Stream.of(BUILDING_BLOCKS, COLORED_BLOCKS, NATURAL_BLOCKS, FUNCTIONAL_BLOCKS, REDSTONE_BLOCKS, TOOLS_AND_UTILITIES, COMBAT, FOOD_AND_DRINKS, INGREDIENTS, SPAWN_EGGS);
    }
}
