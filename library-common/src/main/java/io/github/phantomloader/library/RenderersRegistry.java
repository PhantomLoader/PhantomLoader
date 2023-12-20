package io.github.phantomloader.library;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * <p>
 *     Static class used to store {@link Entity} and {@link BlockEntity} renderers.
 * </p>
 * <p>
 *     Renderers may be added by calling {@link RenderersRegistry#registerEntityRenderer(Supplier, EntityRendererProvider)} and {@link RenderersRegistry#registerBlockEntityRenderer(Supplier, BlockEntityRendererProvider)} from the common module.
 *     The Phantom library will then query those values for each mod loader to register renderers.
 * </p>
 *
 * @author Nico
 */
public class RenderersRegistry {

	/** Entities to register */
	private static final HashMap<Supplier<?>, EntityRendererProvider<?>> ENTITIES = new HashMap<>();
	/** Block entities to register */
	private static final HashMap<Supplier<?>, BlockEntityRendererProvider<?>> BLOCK_ENTITIES = new HashMap<>();

	/**
	 * <p>
	 *     Registers an {@link net.minecraft.client.renderer.entity.EntityRenderer}.
	 * </p>
	 *
	 * @param entity A supplier returning the registered entity type, the one returned by {@link ModRegistry#registerEntity(String, EntityType.Builder)}.
	 * @param renderer The entity renderer's constructor passed as a method reference.
	 * @param <T> The entity's class.
	 */
	public static <T extends Entity> void registerEntityRenderer(Supplier<EntityType<T>> entity, EntityRendererProvider<T> renderer) {
		ENTITIES.put(entity, renderer);
	}

	/**
	 * <p>
	 *     Registers a {@link net.minecraft.client.renderer.blockentity.BlockEntityRenderer}.
	 * </p>
	 *
	 * @param blockEntity A supplier returning the registered block entity type, the one returned by {@link ModRegistry#registerBlockEntity(String, BiFunction, Supplier)}.
	 * @param renderer The block entity renderer's constructor passed as a method reference.
	 * @param <T> The block entity's class.
	 */
	public static <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> blockEntity, BlockEntityRendererProvider<T> renderer) {
		BLOCK_ENTITIES.put(blockEntity, renderer);
	}

	/**
	 * <p>
	 *     Queries all entity renderers added with {@link RenderersRegistry#registerEntityRenderer(Supplier, EntityRendererProvider)} and registers them.
	 *     Called from the loader-specific modules of the Phantom library.
	 * </p>
	 *
	 * @param consumer Registry function.
	 * @param <T> The entity's class.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Entity> void registerEntityRenderers(BiConsumer<EntityType<T>, EntityRendererProvider<T>> consumer) {
		ENTITIES.forEach((entity, renderer) -> consumer.accept((EntityType<T>) entity.get(), (EntityRendererProvider<T>) renderer));
	}

	/**
	 * <p>
	 *     Queries all block entity renderers added with {@link RenderersRegistry#registerBlockEntityRenderer(Supplier, BlockEntityRendererProvider)} and registers them.
	 *     Called from the loader-specific modules of the Phantom library.
	 * </p>
	 *
	 * @param consumer Registry function.
	 * @param <T> The block entity's class.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BlockEntity> void registerBlockEntityRenderers(BiConsumer<BlockEntityType<? extends T>, BlockEntityRendererProvider<T>> consumer) {
		BLOCK_ENTITIES.forEach((blockEntity, renderer) -> consumer.accept((BlockEntityType<? extends T>) blockEntity.get(), (BlockEntityRendererProvider<T>) renderer));
	}
}
