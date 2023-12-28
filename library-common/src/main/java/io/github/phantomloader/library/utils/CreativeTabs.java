package io.github.phantomloader.library.utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>
 *     Static class used to add items to creative tabs.
 * </p>
 * <p>
 *     Item suppliers are first stored as static fields, then the mod loader should call {@link CreativeTabs#accept(ResourceKey, Consumer)} to actually add the items during client initialization.
 * </p>
 *
 * @author Nico
 */
public class CreativeTabs {

	/** Set of items to add. */
	private static final HashMap<ResourceKey<CreativeModeTab>, HashSet<Supplier<? extends ItemLike>>> ITEMS_MAP = new HashMap<>();

	/**
	 * <p>
	 *     Adds an item to a vanilla creative tab with the given key.
	 * </p>
	 *
	 * @param key The creative tab's key. Keys can be seen in {@link net.minecraft.world.item.CreativeModeTabs}.
	 * @param item A supplier returning the registered item to add, the one returned by {@link io.github.phantomloader.library.registry.ModRegistry#registerItem(String)}.
	 */
	public static void addToTab(String key, Supplier<? extends ItemLike> item) {
		addToTab(ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(key)), item);
	}

	/**
	 * <p>
	 *     Adds an item to the creative tab with the given key.
	 * </p>
	 *
	 * @param mod Mod id of the mod the tab belongs to.
	 * @param key The creative tab's registry key, the one passed to {@link io.github.phantomloader.library.registry.ModRegistry#registerCreativeTab(String, Supplier, String)}.
	 * @param item A supplier returning the registered item to add, the one returned by {@link io.github.phantomloader.library.registry.ModRegistry#registerItem(String)}.
	 */
	public static void addToTab(String mod, String key, Supplier<? extends ItemLike> item) {
		addToTab(ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(mod, key)), item);
	}

	/**
	 * <p>
	 *     Adds an item to the given creative tab.
	 * </p>
	 *
	 * @param resourceKey The creative tab's resource key.
	 * @param item A supplier returning the registered item to add, the one returned by {@link io.github.phantomloader.library.registry.ModRegistry#registerItem(String)}.
	 */
	public static void addToTab(ResourceKey<CreativeModeTab> resourceKey, Supplier<? extends ItemLike> item) {
		addToTab(resourceKey, Set.of(item));
	}

	/**
	 * <p>
	 *     Adds a {@link Collection} of items to a vanilla creative tab with the given key.
	 * </p>
	 *
	 * @param key The creative tab's key. Keys can be seen in {@link net.minecraft.world.item.CreativeModeTabs}.
	 * @param items A collection of item suppliers, the ones returned by {@link io.github.phantomloader.library.registry.ModRegistry#registerItem(String)}. Can be created using {@link Set#of(Object[])} or {@link java.util.List#of(Object[])}.
	 */
	public static void addToTab(String key, Collection<Supplier<? extends ItemLike>> items) {
		addToTab(ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(key)), items);
	}

	/**
	 * <p>
	 *     Adds a {@link Collection} of items to the creative tab with the given key.
	 * </p>
	 *
	 * @param mod Mod id of the mod the tab belongs to.
	 * @param key The creative tab's registry key, the one passed to {@link io.github.phantomloader.library.registry.ModRegistry#registerCreativeTab(String, Supplier, String)}.
	 * @param items A collection of item suppliers, the ones returned by {@link io.github.phantomloader.library.registry.ModRegistry#registerItem(String)}. Can be created using {@link Set#of(Object[])} or {@link java.util.List#of(Object[])}.
	 */
	public static void addToTab(String mod, String key, Collection<Supplier<? extends ItemLike>> items) {
		addToTab(ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(mod, key)), items);
	}

	/**
	 * <p>
	 *     Adds a {@link Collection} of items to the given creative tab.
	 * </p>
	 *
	 * @param resourceKey The creative tab's resource key.
	 * @param items A collection of item suppliers, the ones returned by {@link io.github.phantomloader.library.registry.ModRegistry#registerItem(String)}. Can be created using {@link Set#of(Object[])} or {@link java.util.List#of(Object[])}.
	 */
	public static void addToTab(ResourceKey<CreativeModeTab> resourceKey, Collection<Supplier<? extends ItemLike>> items) {
		if(ITEMS_MAP.containsKey(resourceKey)) {
			ITEMS_MAP.get(resourceKey).addAll(items);
		} else {
			ITEMS_MAP.put(resourceKey, new HashSet<>(items));
		}
	}

	/**
	 * <p>
	 *     Registers the items to be added to their respect creative tab.
	 *     Called from the mod loader's client initialization.
	 * </p>
	 *
	 * @param resourceKey The creative tab's resource key.
	 * @param consumer The mod loader's registration function.
	 */
	public static void accept(ResourceKey<CreativeModeTab> resourceKey, Consumer<Supplier<? extends ItemLike>> consumer) {
		if(ITEMS_MAP.containsKey(resourceKey)) {
			ITEMS_MAP.get(resourceKey).forEach(consumer);
		}
	}
}
