package io.github.phantomloader.library.fabric.registry;

import io.github.phantomloader.library.registry.ModRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * <p>
 *     Fabric implementation of {@link ModRegistry}.
 * </p>
 * <p>
 *     Note that registries should not be instantiated directly, one should call {@link ModRegistry#instantiate(String)} from the common module instead.
 * </p>
 *
 * @author Nico
 */
public class FabricRegistry extends ModRegistry {

    /**
     * <p>
     *     Creates a {@code FabricRegistry}.
     * </p>
     * <p>
     *     Note that registries should not be instantiated directly, one should call {@link ModRegistry#instantiate(String)} from the common module instead.
     * </p>
     *
     * @param mod Id of the mod that instantiated this registry.
     */
    public FabricRegistry(String mod) {
        super(mod);
    }

    /**
     * <p>
     *     Helper function used to register objects.
     * </p>
     *
     * @param registry Which registry to use.
     * @param name Name of the object to register.
     * @param object A supplier returning the object to register.
     * @return A supplier returning the registered object.
     * @param <V> Type of the registry.
     * @param <T> Type of the object.
     */
    private <V, T extends V> Supplier<T> register(Registry<V> registry, String name, Supplier<T> object) {
        T registered = Registry.register(registry, new ResourceLocation(this.mod, name), object.get());
        return () -> registered;
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        return this.register(BuiltInRegistries.ITEM, name, item);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        return this.register(BuiltInRegistries.BLOCK, name, block);
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BiFunction<BlockPos, BlockState, T> blockEntity, Set<Supplier<? extends Block>> blocks) {
        // Block entity types must be created here because BlockEntityType.BlockEntitySupplier has private access in the common module
        return this.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, () -> FabricBlockEntityTypeBuilder.create(blockEntity::apply, blocks.stream().map(Supplier::get).toArray(Block[]::new)).build());
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) {
        return this.register(BuiltInRegistries.ENTITY_TYPE, name, () -> builder.build(name));
    }

    @Override
    public <T extends MobEffect> Supplier<T> registerEffect(String name, Supplier<T> effect) {
        return this.register(BuiltInRegistries.MOB_EFFECT, name, effect);
    }

    @Override
    public <T extends Enchantment> Supplier<T> registerEnchantment(String name, Supplier<T> enchantment) {
        return this.register(BuiltInRegistries.ENCHANTMENT, name, enchantment);
    }

    @Override
    public <T extends LootItemFunctionType> Supplier<T> registerLootItemFunction(String name, Supplier<T> lootItemFunction) {
        return this.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, name, lootItemFunction);
    }

    @Override
    public <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature) {
        return this.register(BuiltInRegistries.FEATURE, name, feature);
    }

    @Override
    public <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenu(String name, TriFunction<Integer, Inventory, FriendlyByteBuf, T> menu) {
        return this.register(BuiltInRegistries.MENU, name, () -> new ExtendedScreenHandlerType<>(menu::apply));
    }

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticles(String name, Supplier<T> particles) {
        return this.register(BuiltInRegistries.PARTICLE_TYPE, name, particles);
    }

    @Override
    public <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String name, Supplier<T> recipeSerializer) {
        return this.register(BuiltInRegistries.RECIPE_SERIALIZER, name, recipeSerializer);
    }

    @Override
    public <T extends RecipeType<?>> Supplier<T> registerRecipeType(String name, Supplier<T> recipeType) {
        return this.register(BuiltInRegistries.RECIPE_TYPE, name, recipeType);
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSound(String name, Supplier<T> sound) {
        return this.register(BuiltInRegistries.SOUND_EVENT, name, sound);
    }

    @Override
    public <T extends Fluid> Supplier<T> registerFluid(String name, Supplier<T> fluid) {
        return this.register(BuiltInRegistries.FLUID, name, fluid);
    }

    @Override
    public void register() {

    }
}
