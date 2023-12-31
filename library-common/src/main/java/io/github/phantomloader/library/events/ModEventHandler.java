package io.github.phantomloader.library.events;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.BiConsumer;

/**
 * <p>
 *     Interface to be implemented to implement a mod event handler.
 *     The class that implements this interface must be registered as a service in {@code META-INF/services/io.github.phantomloader.library.events.ModEventHandler}.
 *     See {@link java.util.ServiceLoader} for more details.
 * </p>
 * <ul>
 *     <li>In Forge, these methods are called from a mod event handler.</li>
 *     <li>In Fabric, these methods are called from a {@code ModInitializer}.</li>
 * </ul>
 *
 * @author Nico
 */
public interface ModEventHandler {

    /**
     * <p>
     *     Method called when entity attributes can be registered.
     *     Instances of {@link LivingEntity} needs their attributes to be registered to be spawned.
     *     Attributes are typically created from a static method in the entity's class that returns a {@link AttributeSupplier.Builder}.
     * </p>
     * <pre>
     *     {@code @Override}
     *     public void registerEntityAttributes(BiConsumer event) {
     *         event.accept(ExampleMod.MY_ENTITY, MyEntity.createAttributes());
     *     }
     * </pre>
     *
     * @param event Use {@code event.accept(ExampleMod.MY_ITEM)} to add an item to this creative tab.
     */
    default void registerEntityAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> event) {

    }
}
