package io.github.phantomloader.library.forge.registry;

import io.github.phantomloader.library.forge.items.BlockEntityItem;
import io.github.phantomloader.library.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.function.TriFunction;

import java.util.HashMap;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * <p>
 *     Forge implementation of {@link ModRegistry}.
 * </p>
 * <p>
 *     Note that registries should not be instantiated directly, one should call {@link ModRegistry#instantiate(String)} from the common module instead.
 * </p>
 *
 * @author Nico
 */
public class ForgeRegistry extends ModRegistry {

    /**
     * <p>
     *     Map used to make sure no registry gets created twice and no unused registries get created.
     * </p>
     */
    private final HashMap<ResourceKey<?>, DeferredRegister<?>> registerMap = new HashMap<>();

    /**
     * <p>
     *     Creates a {@code ForgeRegistry}.
     * </p>
     * <p>
     *     Note that registries should not be instantiated directly, one should call {@link ModRegistry#instantiate(String)} from the common module instead.
     * </p>
     *
     * @param mod Id of the mod that instantiated this registry.
     */
    public ForgeRegistry(String mod) {
        super(mod);
    }

    /**
     * <p>
     *     Creates a {@link DeferredRegister} for the given registry or return an already existing one.
     * </p>
     *
     * @param type Registry type.
     * @return The requested {@code DeferredRegister}.
     * @param <T> Registry object type.
     */
    private <T> DeferredRegister<T> getRegister(IForgeRegistry<T> type) {
        return this.getRegister(type.getRegistryKey());
    }

    /**
     * <p>
     *     Creates a {@link DeferredRegister} for the given registry or return an already existing one.
     * </p>
     *
     * @param type Registry type.
     * @return The requested {@code DeferredRegister}.
     * @param <T> Registry object type.
     */
    @SuppressWarnings("unchecked")
    private <T> DeferredRegister<T> getRegister(ResourceKey<Registry<T>> type) {
        if(this.registerMap.containsKey(type)) {
            return (DeferredRegister<T>) this.registerMap.get(type);
        }
        DeferredRegister<T> register = DeferredRegister.create(type, this.mod);
        this.registerMap.put(type, register);
        return register;
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        return this.getRegister(ForgeRegistries.ITEMS).register(name, item);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        return this.getRegister(ForgeRegistries.BLOCKS).register(name, block);
    }

    @Override
    public Supplier<BlockItem> registerBlockItem(String name, Supplier<? extends Block> blockSupplier) {
        return this.registerItem(name, () -> {
            Block block = blockSupplier.get();
            if(block instanceof EntityBlock) {
                return new BlockEntityItem(block, new Item.Properties());
            }
            return new BlockItem(block, new Item.Properties());
        });
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BiFunction<BlockPos, BlockState, T> blockEntity, Set<Supplier<? extends Block>> blocks) {
        // Block entity types must be created here because BlockEntityType.BlockEntitySupplier has private access in the common module
        return this.getRegister(ForgeRegistries.BLOCK_ENTITY_TYPES).register(name, () -> BlockEntityType.Builder.of(blockEntity::apply, blocks.stream().map(Supplier::get).toArray(Block[]::new)).build(null));
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) {
        return this.getRegister(ForgeRegistries.ENTITY_TYPES).register(name, () -> builder.build(name));
    }

    @Override
    public Supplier<SpawnEggItem> registerSpawnEgg(String name, Supplier<EntityType<? extends Mob>> entity, int primaryColor, int secondaryColor) {
        return this.registerItem(name, () -> new ForgeSpawnEggItem(entity, primaryColor, secondaryColor, new Item.Properties()));
    }

    @Override
    public <T extends MobEffect> Supplier<T> registerEffect(String name, Supplier<T> effect) {
        return this.getRegister(ForgeRegistries.MOB_EFFECTS).register(name, effect);
    }

    @Override
    public <T extends Enchantment> Supplier<T> registerEnchantment(String name, Supplier<T> enchantment) {
        return this.getRegister(ForgeRegistries.ENCHANTMENTS).register(name, enchantment);
    }

    @Override
    public <T extends LootItemFunctionType> Supplier<T> registerLootItemFunction(String name, Supplier<T> lootItemFunction) {
        return this.getRegister(Registries.LOOT_FUNCTION_TYPE).register(name, lootItemFunction);
    }

    @Override
    public <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature) {
        return this.getRegister(ForgeRegistries.FEATURES).register(name, feature);
    }

    @Override
    public <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenu(String name, TriFunction<Integer, Inventory, FriendlyByteBuf, T> menu) {
        return this.getRegister(ForgeRegistries.MENU_TYPES).register(name, () -> IForgeMenuType.create(menu::apply));
    }

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticles(String name, Supplier<T> particles) {
        return this.getRegister(ForgeRegistries.PARTICLE_TYPES).register(name, particles);
    }

    @Override
    public <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String name, Supplier<T> recipeSerializer) {
        return this.getRegister(ForgeRegistries.RECIPE_SERIALIZERS).register(name, recipeSerializer);
    }

    @Override
    public <T extends RecipeType<?>> Supplier<T> registerRecipeType(String name, Supplier<T> recipeType) {
        return this.getRegister(ForgeRegistries.RECIPE_TYPES).register(name, recipeType);
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSound(String name, Supplier<T> sound) {
        return this.getRegister(ForgeRegistries.SOUND_EVENTS).register(name, sound);
    }

    @Override
    public <T extends Fluid> Supplier<T> registerFluid(String name, Supplier<T> fluid) {
        return this.getRegister(ForgeRegistries.FLUIDS).register(name, fluid);
    }

    @Override
    public void register() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        for(DeferredRegister<?> register : this.registerMap.values()) {
            register.register(eventBus);
        }
    }
}
