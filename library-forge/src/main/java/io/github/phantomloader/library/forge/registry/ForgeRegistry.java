package io.github.phantomloader.library.forge.registry;

import io.github.phantomloader.library.forge.items.BlockEntityItem;
import io.github.phantomloader.library.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.*;
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
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * <p>
 *     Forge implementation of {@link ModRegistry}.
 * </p>
 * <p>
 *     Note that registries should not be instantiated directly, one should call {@link ModRegistry#instantiate(String)}
 *     from the common module instead.
 * </p>
 *
 * @author Nico
 */
@Mod.EventBusSubscriber(modid = "phantom", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeRegistry extends ModRegistry {

    private static final HashMap<ResourceKey<?>, DeferredRegister<?>> REGISTRIES = new HashMap<>();

    private static <T> DeferredRegister<T> createOrGetRegistry(IForgeRegistry<T> type, String mod) {
        return createOrGetRegistry(type.getRegistryKey(), mod);
    }

    @SuppressWarnings("unchecked")
    private static <T> DeferredRegister<T> createOrGetRegistry(ResourceKey<Registry<T>> type, String mod) {
        if(REGISTRIES.containsKey(type)) {
            return (DeferredRegister<T>) REGISTRIES.get(type);
        }
        DeferredRegister<T> register = DeferredRegister.create(type, mod);
        REGISTRIES.put(type, register);
        return register;
    }

    /** Map to store entity attributes to register in the forge event */
    private static final HashMap<Supplier<EntityType<? extends LivingEntity>>, AttributeSupplier.Builder> ENTITY_ATTRIBUTES = new HashMap<>();

    /**
     * <p>
     *     Creates a {@code ForgeRegistry}.
     * </p>
     * <p>
     *     Note that registries should not be instantiated directly, one should call {@link ModRegistry#instantiate(String)}
     *     from the common module instead.
     * </p>
     *
     * @param mod Id of the mod that instantiated this registry.
     */
    public ForgeRegistry(String mod) {
        super(mod);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        return createOrGetRegistry(ForgeRegistries.ITEMS, this.mod).register(name, item);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        return createOrGetRegistry(ForgeRegistries.BLOCKS, this.mod).register(name, block);
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
        return createOrGetRegistry(ForgeRegistries.BLOCK_ENTITY_TYPES, this.mod).register(name, () -> BlockEntityType.Builder.of(blockEntity::apply, blocks.stream().map(Supplier::get).toArray(Block[]::new)).build(null));
    }

    @Override
    public Supplier<CreativeModeTab> registerCreativeTab(String name, Supplier<? extends ItemLike> icon, Component title, Collection<Supplier<? extends ItemLike>> items) {
        return createOrGetRegistry(Registries.CREATIVE_MODE_TAB, this.mod).register(name, () -> CreativeModeTab.builder()
                .title(title)
                .icon(() -> new ItemStack(icon.get()))
                .displayItems((params, output) -> items.forEach(item -> output.accept(item.get())))
                .build()
        );
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) {
        return createOrGetRegistry(ForgeRegistries.ENTITY_TYPES, this.mod).register(name, () -> builder.build(name));
    }

    @Override
    public void registerEntityAttributes(Supplier<EntityType<? extends LivingEntity>> entity, AttributeSupplier.Builder attributes) {
        ENTITY_ATTRIBUTES.put(entity, attributes); // TODO: Fix this
    }

    /**
     * <p>
     *     Forge event used to register entity attributes.
     * </p>
     *
     * @param event Forge event.
     */
    @SubscribeEvent
    public static void onCreateAttributesEvent(EntityAttributeCreationEvent event) {
        ENTITY_ATTRIBUTES.forEach((entity, attributes) -> event.put(entity.get(), attributes.build()));
    }

    @Override
    public Supplier<SpawnEggItem> registerSpawnEgg(String name, Supplier<EntityType<? extends Mob>> entity, int primaryColor, int secondaryColor) {
        return this.registerItem(name, () -> new ForgeSpawnEggItem(entity, primaryColor, secondaryColor, new Item.Properties()));
    }

    @Override
    public <T extends MobEffect> Supplier<T> registerEffect(String name, Supplier<T> effect) {
        return createOrGetRegistry(ForgeRegistries.MOB_EFFECTS, this.mod).register(name, effect);
    }

    @Override
    public <T extends Enchantment> Supplier<T> registerEnchantment(String name, Supplier<T> enchantment) {
        return createOrGetRegistry(ForgeRegistries.ENCHANTMENTS, this.mod).register(name, enchantment);
    }

    @Override
    public <T extends LootItemFunctionType> Supplier<T> registerLootItemFunction(String name, Supplier<T> lootItemFunction) {
        return createOrGetRegistry(Registries.LOOT_FUNCTION_TYPE, this.mod).register(name, lootItemFunction);
    }

    @Override
    public <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature) {
        return createOrGetRegistry(ForgeRegistries.FEATURES, this.mod).register(name, feature);
    }

//    @Override
//    public <T extends MenuType<?>> Supplier<T> registerMenu(String name, BiFunction<Integer, Inventory, T> menu) {
//        return createOrGetRegistry(ForgeRegistries.MENU_TYPES, this.mod).register(name, () -> IForgeMenuType.create(((windowId, inv, data) -> menu.apply(windowId, inv))));
//    }

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticles(String name, Supplier<T> particles) {
        return createOrGetRegistry(ForgeRegistries.PARTICLE_TYPES, this.mod).register(name, particles);
    }

    @Override
    public <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String name, Supplier<T> recipeSerializer) {
        return createOrGetRegistry(ForgeRegistries.RECIPE_SERIALIZERS, this.mod).register(name, recipeSerializer);
    }

    @Override
    public <T extends RecipeType<?>> Supplier<T> registerRecipeType(String name, Supplier<T> recipeType) {
        return createOrGetRegistry(ForgeRegistries.RECIPE_TYPES, this.mod).register(name, recipeType);
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSound(String name, Supplier<T> sound) {
        return createOrGetRegistry(ForgeRegistries.SOUND_EVENTS, this.mod).register(name, sound);
    }

    @Override
    public void register() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRIES.values().forEach(registry -> registry.register(eventBus));
    }
}
