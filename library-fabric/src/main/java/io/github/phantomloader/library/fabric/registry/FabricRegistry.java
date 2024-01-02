package io.github.phantomloader.library.fabric.registry;

import io.github.phantomloader.library.fabric.renderers.BlockEntityItemRenderer;
import io.github.phantomloader.library.registry.ModRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * <p>
 *     Fabric implementation of {@link ModRegistry}.
 * </p>
 * <p>
 *     Note that registries should not be instantiated directly, one should call {@link ModRegistry#instantiate(String)}
 *     from the common module instead.
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
     *     Note that registries should not be instantiated directly, one should call {@link ModRegistry#instantiate(String)}
     *     from the common module instead.
     * </p>
     *
     * @param mod Id of the mod that instantiated this registry.
     */
    public FabricRegistry(String mod) {
        super(mod);
    }

    /**
     * Helper function used to create a {@link ResourceLocation}.
     *
     * @param name Identifier name.
     * @return A {@code ResourceLocation} identifier.
     */
    private ResourceLocation identifier(String name) {
        return new ResourceLocation(this.mod, name);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        T registered = Registry.register(BuiltInRegistries.ITEM, this.identifier(name), item.get());
        return () -> registered;
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        T registered = Registry.register(BuiltInRegistries.BLOCK, this.identifier(name), block.get());
        return () -> registered;
    }

    @Override
    public Supplier<BlockItem> registerBlockItem(String name, Supplier<? extends Block> block) {
        return super.registerBlockItem(name, () -> {
            Block instance = block.get();
            if(instance instanceof EntityBlock) {
                // TODO: This should only happen on the client
                BuiltinItemRendererRegistry.INSTANCE.register(instance, new BlockEntityItemRenderer(instance));
            }
            return instance;
        });
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BiFunction<BlockPos, BlockState, T> blockEntity, Set<Supplier<? extends Block>> blocks) {
        // Block entity types must be created here because BlockEntityType.BlockEntitySupplier has private access in the common module
        BlockEntityType<T> registered = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, this.identifier(name), FabricBlockEntityTypeBuilder.create(blockEntity::apply, blocks.stream().map(Supplier::get).toArray(Block[]::new)).build());
        return () -> registered;
    }

    @Override
    public Supplier<CreativeModeTab> registerCreativeTab(String name, Supplier<? extends ItemLike> icon, Component title, Collection<Supplier<? extends ItemLike>> items) {
        CreativeModeTab registered = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, this.identifier(name), CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(title)
                .icon(() -> new ItemStack(icon.get()))
                .displayItems((params, output) -> items.forEach(item -> output.accept(item.get())))
                .build()
        );
        return () -> registered;
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) {
        EntityType<T> registered = Registry.register(BuiltInRegistries.ENTITY_TYPE, this.identifier(name), builder.build(name));
        return () -> registered;
    }

    @Override
    public <T extends MobEffect> Supplier<T> registerEffect(String name, Supplier<T> effect) {
        T registered = Registry.register(BuiltInRegistries.MOB_EFFECT, this.identifier(name), effect.get());
        return () -> registered;
    }

    @Override
    public <T extends Enchantment> Supplier<T> registerEnchantment(String name, Supplier<T> enchantment) {
        T registered = Registry.register(BuiltInRegistries.ENCHANTMENT, this.identifier(name), enchantment.get());
        return () -> registered;
    }

    @Override
    public <T extends LootItemFunctionType> Supplier<T> registerLootItemFunction(String name, Supplier<T> lootItemFunction) {
        T registered = Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, this.identifier(name), lootItemFunction.get());
        return () -> registered;
    }

    @Override
    public <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature) {
        T registered = Registry.register(BuiltInRegistries.FEATURE, this.identifier(name), feature.get());
        return () -> registered;
    }

    @Override
    public <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenu(String name, TriFunction<Integer, Inventory, FriendlyByteBuf, T> menu) {
        throw new NotImplementedException(); // TODO: https://fabricmc.net/wiki/tutorial:screenhandler
    }

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticles(String name, Supplier<T> particles) {
        T registered = Registry.register(BuiltInRegistries.PARTICLE_TYPE, this.identifier(name), particles.get());
        return () -> registered;
    }

    @Override
    public <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String name, Supplier<T> recipeSerializer) {
        T registered = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, this.identifier(name), recipeSerializer.get());
        return () -> registered;
    }

    @Override
    public <T extends RecipeType<?>> Supplier<T> registerRecipeType(String name, Supplier<T> recipeType) {
        T registered = Registry.register(BuiltInRegistries.RECIPE_TYPE, this.identifier(name), recipeType.get());
        return () -> registered;
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSound(String name, Supplier<T> sound) {
        T registered = Registry.register(BuiltInRegistries.SOUND_EVENT, this.identifier(name), sound.get());
        return () -> registered;
    }

    @Override
    public void register() {

    }
}
