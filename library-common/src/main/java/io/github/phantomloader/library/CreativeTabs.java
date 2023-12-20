package io.github.phantomloader.library;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeTabs {

	public static final ResourceKey<CreativeModeTab> BUILDING_BLOCKS = createKey("building_blocks");
	public static final ResourceKey<CreativeModeTab> COLORED_BLOCKS = createKey("colored_blocks");
	public static final ResourceKey<CreativeModeTab> NATURAL_BLOCKS = createKey("natural_blocks");
	public static final ResourceKey<CreativeModeTab> FUNCTIONAL_BLOCKS = createKey("functional_blocks");
	public static final ResourceKey<CreativeModeTab> REDSTONE_BLOCKS = createKey("redstone_blocks");
	public static final ResourceKey<CreativeModeTab> TOOLS_AND_UTILITIES = createKey("tools_and_utilities");
	public static final ResourceKey<CreativeModeTab> COMBAT = createKey("combat");
	public static final ResourceKey<CreativeModeTab> FOOD_AND_DRINKS = createKey("food_and_drinks");
	public static final ResourceKey<CreativeModeTab> INGREDIENTS = createKey("ingredients");
	public static final ResourceKey<CreativeModeTab> SPAWN_EGGS = createKey("spawn_eggs");
	public static final ResourceKey<CreativeModeTab> OP_BLOCKS = createKey("op_blocks");

	private static ResourceKey<CreativeModeTab> createKey(String key) {
		return ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(key));
	}

	private static final HashMap<ResourceKey<CreativeModeTab>, HashSet<Supplier<? extends ItemLike>>> ITEMS_MAP = new HashMap<>();

	public static void addItemToCreativeTab(ResourceKey<CreativeModeTab> resourceKey, Supplier<? extends ItemLike> item) {
		addItemsToCreativeTab(resourceKey, Set.of(item));
	}

	public static void addItemsToCreativeTab(ResourceKey<CreativeModeTab> resourceKey, Set<Supplier<? extends ItemLike>> items) {
		if(ITEMS_MAP.containsKey(resourceKey)) {
			ITEMS_MAP.get(resourceKey).addAll(items);
		} else {
			ITEMS_MAP.put(resourceKey, new HashSet<>(items));
		}
	}

	public static void accept(ResourceKey<CreativeModeTab> resourceKey, Consumer<Supplier<? extends ItemLike>> consumer) {
		if(ITEMS_MAP.containsKey(resourceKey)) {
			ITEMS_MAP.get(resourceKey).forEach(consumer);
		}
	}

	public static Set<ResourceKey<CreativeModeTab>> allTabs() {
		return Set.of(BUILDING_BLOCKS, COLORED_BLOCKS, NATURAL_BLOCKS, FUNCTIONAL_BLOCKS, REDSTONE_BLOCKS, TOOLS_AND_UTILITIES, COMBAT, FOOD_AND_DRINKS, INGREDIENTS, SPAWN_EGGS, OP_BLOCKS);
	}
}
