package io.github.phantomloader.library.utils;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

import java.lang.reflect.Field;

/**
 * <p>
 *     Utility class used to store a reference to creative mod tabs.
 *     Needed because they have private access in {@link CreativeModeTabs}.
 * </p>
 *
 * @author Nico
 */
public class CreativeTabsUtils {

    /** Building blocks creative tab */
    public static final CreativeModeTab BUILDING_BLOCKS = getTab("BUILDING_BLOCKS");
    /** Colored blocks creative tab */
    public static final CreativeModeTab COLORED_BLOCKS = getTab("COLORED_BLOCKS");
    /** Natural blocks creative tab */
    public static final CreativeModeTab NATURAL_BLOCKS = getTab("NATURAL_BLOCKS");
    /** Functional blocks creative tab */
    public static final CreativeModeTab FUNCTIONAL_BLOCKS = getTab("FUNCTIONAL_BLOCKS");
    /** Redstone blocks creative tab */
    public static final CreativeModeTab REDSTONE_BLOCKS = getTab("REDSTONE_BLOCKS");
    /** Tools and items creative tab */
    public static final CreativeModeTab TOOLS_AND_UTILITIES = getTab("TOOLS_AND_UTILITIES");
    /** Combat creative tab */
    public static final CreativeModeTab COMBAT = getTab("COMBAT");
    /** Food and potions creative tab */
    public static final CreativeModeTab FOOD_AND_DRINKS = getTab("FOOD_AND_DRINKS");
    /** Materials creative tab */
    public static final CreativeModeTab INGREDIENTS = getTab("INGREDIENTS");
    /** Spawn eggs creative tab */
    public static final CreativeModeTab SPAWN_EGGS = getTab("SPAWN_EGGS");

    /**
     * <p>
     *     Gets the creative tab using reflection.
     * </p>
     *
     * @param variableName Variable name
     * @return The creative tab
     */
    private static CreativeModeTab getTab(String variableName) {
        try {
            Field field = CreativeModeTabs.class.getDeclaredField(variableName);
            return (CreativeModeTab) field.get(null);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
