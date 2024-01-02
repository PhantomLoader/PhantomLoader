package io.github.phantomloader.library.events;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface ModEventHandler {

    void registerEntityAttributes(BiConsumer<Supplier<EntityType<? extends LivingEntity>>, AttributeSupplier.Builder> event);
}
