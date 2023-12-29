package io.github.phantomloader.library.registry;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * <p>
 *     Interface used to abstract the client registry.
 *     Every mod loader should provide an implementation of this interface.
 * </p>
 * <p>
 *     The correct loader can be obtained by referencing the {@link ClientRegistry#INSTANCE}.
 * </p>
 *
 * @author Nico
 */
public interface ClientRegistry {

	/**
	 * <p>
	 *     Instance of the {@link ClientRegistry} for the current mod loader.
	 * </p>
	 */
	ClientRegistry INSTANCE = ServiceLoader.load(ClientRegistry.class)
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException("No client registry has been defined in META-INF/services. Make sure you are using the correct version of the library mod for your mod loader."));

	/**
	 * <p>
	 *     Adds the given item to the vanilla creative tab with the given identifier.
	 *     Identifiers can be seen in {@link net.minecraft.world.item.CreativeModeTabs}.
	 * </p>
	 *
	 * @param identifier Identifier of the creative tab.
	 * @param item A supplier returning the item to add, the one returned by {@link ModRegistry#registerItem(String)}.
	 */
	default void addItemToCreativeTab(String identifier, Supplier<? extends ItemLike> item) {
		this.addItemsToCreativeTab(identifier, Set.of(item));
	}

	/**
	 * <p>
	 *     Adds the given item to a creative tab with the given identifier for the mod with the given id.
	 * </p>
	 *
	 * @param mod Mod id of the creative tab's mod.
	 * @param identifier Identifier of the creative tab.
	 * @param item A supplier returning the item to add, the one returned by {@link ModRegistry#registerItem(String)}.
	 */
	default void addItemToCreativeTab(String mod, String identifier, Supplier<? extends ItemLike> item) {
		this.addItemsToCreativeTab(mod, identifier, Set.of(item));
	}

	/**
	 * <p>
	 *     Adds the given items to the vanilla creative tab with the given identifier.
	 *     Identifiers can be seen in {@link net.minecraft.world.item.CreativeModeTabs}.
	 * </p>
	 *
	 * @param identifier Identifier of the creative tab.
	 * @param items A collection if suppliers returning the items to add, the ones returned by {@link ModRegistry#registerItem(String)}.
	 */
	default void addItemsToCreativeTab(String identifier, Collection<Supplier<? extends ItemLike>> items) {
		this.addItemsToCreativeTab(ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(identifier)), items);
	}

	/**
	 * <p>
	 *     Adds the given items to a creative tab with the given identifier for the mod with the given id.
	 *     Identifiers can be seen in {@link net.minecraft.world.item.CreativeModeTabs}.
	 * </p>
	 *
	 * @param mod Mod id of the creative tab's mod.
	 * @param identifier Identifier of the creative tab.
	 * @param items A collection if suppliers returning the items to add, the ones returned by {@link ModRegistry#registerItem(String)}.
	 */
	default void addItemsToCreativeTab(String mod, String identifier, Collection<Supplier<? extends ItemLike>> items) {
		this.addItemsToCreativeTab(ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(mod, identifier)), items);
	}

	/**
	 * <p>
	 *     Adds the given items to a creative tab with the given {@link ResourceKey}.
	 *     Identifiers can be seen in {@link net.minecraft.world.item.CreativeModeTabs}.
	 * </p>
	 *
	 * @param resourceKey Resource key of the creative tab.
	 * @param items A collection if suppliers returning the items to add, the ones returned by {@link ModRegistry#registerItem(String)}.
	 */
	void addItemsToCreativeTab(ResourceKey<CreativeModeTab> resourceKey, Collection<Supplier<? extends ItemLike>> items);

	/**
	 * <p>
	 *     Registers an entity renderer for the given entity type.
	 * </p>
	 *
	 * @param entity A supplier returning the registered entity type, the one returned by {@link ModRegistry#registerEntity(String, EntityType.Builder)}.
	 * @param renderer The constructor of the entity renderer passed as a method reference.
	 * @param <T> The entity's class.
	 */
	<T extends Entity> void registerEntityRenderer(Supplier<EntityType<T>> entity, EntityRendererProvider<T> renderer);

	/**
	 * <p>
	 *     Registers a block entity renderer for the given block entity type.
	 * </p>
	 *
	 * @param blockEntity A supplier returning the registered block entity type, the one returned by {@link ModRegistry#registerBlockEntity(String, BiFunction, Set)}.
	 * @param renderer The constructor of the block entity renderer passed as a method reference.
	 * @param <T> The block entity's class.
	 */
	<T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> blockEntity, BlockEntityRendererProvider<T> renderer);
}
