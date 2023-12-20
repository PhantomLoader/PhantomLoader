package io.github.phantomloader.library;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * <p>
 *     Static class used to register entity attributes.
 * </p>
 * <p>
 *     A {@link LivingEntity} needs its attributes registered to be spawned in the game.
 *     This class can store an {@link AttributeSupplier} for every entity with {@link AttributesRegistry#registerAttributes(Supplier, AttributeSupplier)}.
 *     The Phantom library will then query the stored values for each mod loader to register attributes.
 * </p>
 */
public class AttributesRegistry {

	/** Entity-attributes map */
	private static final HashMap<Supplier<EntityType<? extends LivingEntity>>, AttributeSupplier> ENTITIES = new HashMap<>();

	/**
	 * <p>
	 *     Registers the given attributes for the given entity.
	 * </p>
	 *
	 * @param entity A supplier returning the registered entity type, the one returned by {@link ModRegistry#registerEntity(String, EntityType.Builder)}.
	 * @param builder An entity attribute supplier builder.
	 */
	public static void registerAttributes(Supplier<EntityType<? extends LivingEntity>> entity, AttributeSupplier.Builder builder) {
		registerAttributes(entity, builder.build());
	}

	/**
	 * <p>
	 *     Registers the given attributes for the given entity.
	 * </p>
	 *
	 * @param entity A supplier returning the registered entity type, the one returned by {@link ModRegistry#registerEntity(String, EntityType.Builder)}.
	 * @param attributes A supplier returning the entity's attributes.
	 */
	public static void registerAttributes(Supplier<EntityType<? extends LivingEntity>> entity, AttributeSupplier attributes) {
		ENTITIES.put(entity, attributes);
	}

	/**
	 * <p>
	 *     Queries all attributes added with {@link AttributesRegistry#registerAttributes(Supplier, AttributeSupplier)} and registers them.
	 *     Called from the loader-specific modules of the Phantom library.
	 * </p>
	 *
	 * @param consumer Registry function
	 */
	public static void registerAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> consumer) {
		ENTITIES.forEach((entity, attributes) -> consumer.accept(entity.get(), attributes));
	}
}
