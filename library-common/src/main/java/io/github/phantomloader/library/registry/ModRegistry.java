package io.github.phantomloader.library.registry;

import io.github.phantomloader.library.ModEntryPoint;
import io.github.phantomloader.library.events.ClientEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>
 *     Abstract registry class. Every mod loader should provide an implementation of this class.
 * </p>
 * <p>
 *     The correct loader can be instantiated by calling {@link ModRegistry#instantiate(String)} from your mod's main
 *     class. The method will return an instance of {@link ModRegistry} for the current platform that can be used to
 *     register registry objects for the mod.
 * </p>
 *
 * @author Nico
 */
public abstract class ModRegistry {

    /**
     * <p>
     *     Instantiates a {@link ModRegistry}.
     * </p>
     * <p>
     *     Loads a {@link RegistryProvider} using {@link ServiceLoader} to create an instance of the registry for the
     *     current platform. Note that a {@code RegistryProvider} must be defined in {@code META-INF/services} for this
     *     to work, therefore the library mod for the correct loader must be present at runtime.
     * </p>
     *
     * @param mod Mod id of the mod needed to instantiate the {@link ModRegistry#ModRegistry(String)}
     * @return An instance of {@code ModRegistry} for the current platform
     * @throws NoSuchElementException If no registry has been defined in {@code META-INF/services}
     */
    public static ModRegistry instantiate(String mod) {
        return ServiceLoader.load(RegistryProvider.class)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No registry has been defined in META-INF/services. Make sure you are using the correct version of the library mod for your mod loader."))
                .instantiate(mod);
    }

    /** Id of the mod that instantiated this registry */
    public final String mod;

    /**
     * Creates a {@code ModRegistry}.
     *
     * @param mod Id of the mod that instantiated this registry.
     */
    public ModRegistry(String mod) {
        this.mod = mod;
    }

    /**
     * <p>
     *     Registers an {@link Item}.
     * </p>
     *
     * @param name The item's registry name.
     * @param supplier A supplier returning the item to register.
     * @return A supplier returning the registered item.
     * @param <T> The item's class.
     */
    public abstract <T extends Item>Supplier<T> registerItem(String name, Supplier<T> supplier);

    /**
     * <p>
     *     Registers an {@link Item} with the given properties.
     * </p>
     *
     * @param name The item's registry name.
     * @param properties The item's properties.
     * @return A supplier returning the registered item.
     */
    public Supplier<Item> registerItem(String name, Item.Properties properties) {
        return this.registerItem(name, () -> new Item(properties));
    }

    /**
     * <p>
     *     Registers an {@link Item} with the default properties.
     * </p>
     *
     * @param name The item's registry name.
     * @return A supplier returning the registered item.
     */
    public Supplier<Item> registerItem(String name) {
        return this.registerItem(name, new Item.Properties());
    }

    /**
     * <p>
     *     Registers a {@link Block}.
     * </p>
     *
     * @param name The block's registry name.
     * @param supplier A supplier returning the block to register.
     * @return A supplier returning the registered block.
     * @param <T> The block's class.
     */
    public abstract <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> supplier);

    /**
     * <p>
     *     Registers a {@link Block} with the given properties.
     * </p>
     *
     * @param name The block's registry name.
     * @param properties The block's properties.
     * @return A supplier returning the registered block.
     */
    public Supplier<Block> registerBlock(String name, BlockBehaviour.Properties properties) {
        return this.registerBlock(name, () -> new Block(properties));
    }

    /**
     * <p>
     *     Registers a {@link Block} with the default properties.
     * </p>
     *
     * @param name The block's registry name.
     * @return A supplier returning the registered block.
     */
    public Supplier<Block> registerBlock(String name) {
        return this.registerBlock(name, BlockBehaviour.Properties.of());
    }

    /**
     * <p>
     *     Registers a {@link BlockItem} from the given {@link Block}.
     * </p>
     * <p>
     *     This method should be preferred to {@link ModRegistry#registerItem(String, Supplier)} for registering
     *     {@code BlockItem}s because different mod loaders have different rendering methods for block entity items.
     * </p>
     *
     * @param name Name of the item to register.
     * @param block The block to use to create the {@code BlockItem}. Must be a registered block.
     * @return A supplier returning the registered item.
     * @see ModRegistry#registerBlockAndItem(String, Supplier)
     */
    public Supplier<BlockItem> registerBlockItem(String name, Supplier<? extends Block> block) {
        return this.registerItem(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    /**
     * <p>
     *     Registers a {@link Block} and an {@link Item}.
     * </p>
     *
     * @param name Registry name used both by the item and the block.
     * @param block A supplier returning the block to register.
     * @return A supplier returning the registered block.
     * @see ModRegistry#registerBlockItem(String, Supplier)
     * @param <T> The block's class.
     */
    public  <T extends Block> Supplier<T> registerBlockAndItem(String name, Supplier<T> block) {
        Supplier<T> registered = this.registerBlock(name, block);
        this.registerBlockItem(name, registered);
        return registered;
    }

    /**
     * <p>
     *     Registers a {@link Block} with the given properties and an {@link Item}.
     * </p>
     *
     * @param name Registry name used both by the item and the block.
     * @param properties The block's properties.
     * @return A supplier returning the registered block.
     */
    public Supplier<Block> registerBlockAndItem(String name, BlockBehaviour.Properties properties) {
        return this.registerBlockAndItem(name, () -> new Block(properties));
    }

    /**
     * <p>
     *     Registers a {@link Block} with the given properties and an {@link Item}.
     * </p>
     *
     * @param name Registry name used both by the item and the block.
     * @return A supplier returning the registered block.
     */
    public Supplier<Block> registerBlockAndItem(String name) {
        return this.registerBlockAndItem(name, BlockBehaviour.Properties.of());
    }

    /**
     * <p>
     *     Registers a {@link Block} with the same properties as the given one using
     *     {@link BlockBehaviour.Properties#copy(BlockBehaviour)}.
     * </p>
     * <p>
     *     Useful for creating blocks that have many variants such as bricks, cracked bricks, mossy bricks etc.
     * </p>
     *
     * @param name Registry name of the block to register.
     * @param base The block to use as base. Must be a registered block.
     * @return A supplier returning the registered block.
     * @see ModRegistry#registerBlockVariantAndItem(String, Supplier)
     */
    public Supplier<Block> registerBlockVariant(String name, Supplier<? extends Block> base) {
        return this.registerBlock(name, () -> new Block(BlockBehaviour.Properties.copy(base.get())));
    }

    /**
     * <p>
     *     Registers a {@link Block} with the same properties as the given one using
     *     {@link BlockBehaviour.Properties#copy(BlockBehaviour)}. The block to register is created using the given
     *     {@link Function} as a constructor.
     * </p>
     * <p>
     *     Useful for creating slabs, stairs, and walls by copying the properties of their base block.
     * </p>
     *
     * @param name Registry name of the block to register.
     * @param constructor A function that takes a {@code BlockBehaviour#Properties} and returns a {@code Block}. Ideally the block's constructor passed using {@code Block::new}.
     * @param base The block to use as base. Must be a registered block.
     * @return A supplier returning the registered block.
     * @param <T> The block's class.
     */
    public  <T extends Block> Supplier<T> registerBlockVariant(String name, Function<BlockBehaviour.Properties, T> constructor, Supplier<? extends Block> base) {
        return this.registerBlock(name, () -> constructor.apply(BlockBehaviour.Properties.copy(base.get())));
    }

    /**
     * <p>
     *     Registers a {@link Block} with the same properties as the given one using
     *     {@link BlockBehaviour.Properties#copy(BlockBehaviour)} and an {@link Item}.
     * </p>
     * <p>
     *     Useful for creating blocks that have many variants such as bricks, cracked bricks, mossy bricks etc.
     * </p>
     *
     * @param name Registry name used both by the item and the block.
     * @param base The block to use as base. Must be a registered block.
     * @return A supplier returning the registered block.
     * @see ModRegistry#registerBlockVariant(String, Supplier)
     */
    public Supplier<Block> registerBlockVariantAndItem(String name, Supplier<? extends Block> base) {
        return this.registerBlockAndItem(name, () -> new Block(BlockBehaviour.Properties.copy(base.get())));
    }

    /**
     * <p>
     *     Registers a {@link Block} with the same properties as the given one using
     *     {@link BlockBehaviour.Properties#copy(BlockBehaviour)} and an {@link Item}. The block to register is created
     *     using the given {@link Function} as a constructor.
     * </p>
     * <p>
     *     Useful for creating slabs, stairs, and walls by copying the properties of their base block.
     * </p>
     *
     * @param name Registry name used both by the item and the block.
     * @param constructor A function that takes a {@code BlockBehaviour#Properties} and returns a {@code Block}. Ideally the block's constructor passed using {@code Block::new}.
     * @param base The block to use as base. Must be a registered block.
     * @return A supplier returning the registered block.
     * @param <T> The block's class.
     */
    public  <T extends Block> Supplier<T> registerBlockVariantAndItem(String name, Function<BlockBehaviour.Properties, T> constructor, Supplier<? extends Block> base) {
        return this.registerBlockAndItem(name, () -> constructor.apply(BlockBehaviour.Properties.copy(base.get())));
    }

    /**
     * <p>
     *     Registers a {@link BlockEntityType}.
     * </p>
     * <p>
     *     Classes extending {@link BlockEntity} must declare a constructor that takes in a {@link BlockPos} and a {@link BlockState}.
     *     The constructor should be passed to this method as a method reference to register the block entity.
     * </p>
     *
     * @param name The block entity's registry name.
     * @param blockEntity The block entity's constructor.
     * @param blocks A set of blocks that use this block entity. Can be passed using {@link Set#of(Object[])}.
     * @return A supplier returning the registered block entity type.
     * @param <T> The block entity's class.
     */
    public abstract <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BiFunction<BlockPos, BlockState, T> blockEntity, Set<Supplier<? extends Block>> blocks);

    /**
     * <p>
     *     Registers a {@link BlockEntityType}.
     * </p>
     * <p>
     *     Classes extending {@link BlockEntity} must declare a constructor that takes in a {@link BlockPos} and a {@link BlockState}.
     *     The constructor should be passed to this method as a method reference to register the block entity.
     * </p>
     *
     * @param name The block entity's registry name.
     * @param blockEntity The block entity's constructor.
     * @param block The block that uses this block entity.
     * @return A supplier returning the registered block entity type.
     * @param <T> The block entity's class.
     */
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BiFunction<BlockPos, BlockState, T> blockEntity, Supplier<? extends Block> block) {
        return this.registerBlockEntity(name, blockEntity, Set.of(block));
    }

    /**
     * <p>
     *     Registers a {@link CreativeModeTab}.
     * </p>
     * <p>
     *     This function is used to create creative tabs for mods. The preferred method for adding items to creative tabs is passing them here.
     *     Alternatively, the {@link ClientEventHandler#addItemsToCreativeTab(ResourceKey, Consumer)} method can be used.
     * </p>
     *
     * @param name The creative tab's registry name. Note that this is different from the tab's title.
     * @param icon The icon to display in the creative tab.
     * @param title The creative tab's display name, the one that appears in the creative mode tab.
     * @param items A {@link Collection} of the items to display in the creative tab.
     * @return A supplier returning the registered creative tab.
     */
    public abstract Supplier<CreativeModeTab> registerCreativeTab(String name, Supplier<? extends ItemLike> icon, Component title, Collection<Supplier<? extends ItemLike>> items);

    /**
     * <p>
     *     Registers a {@link CreativeModeTab}.
     * </p>
     * <p>
     *     This function is used to create creative tabs for mods. The preferred method for adding items to creative tabs is the {@link #registerCreativeTab(String, Supplier, String, Collection)} method.
     *     Alternatively, the {@link ClientEventHandler#addItemsToCreativeTab(ResourceKey, Consumer)} method can be used.
     * </p>
     *
     * @param name The creative tab's registry name. Note that this is different from the tab's title.
     * @param icon The icon to display in the creative tab.
     * @param title The creative tab's display name, the one that appears in the creative mode tab.
     * @return A supplier returning the registered creative tab.
     */
    public Supplier<CreativeModeTab> registerCreativeTab(String name, Supplier<? extends ItemLike> icon, Component title) {
        return this.registerCreativeTab(name, icon, title, Set.of());
    }

    /**
     * <p>
     *     Registers a {@link CreativeModeTab}.
     * </p>
     * <p>
     *     This function is used to create creative tabs for mods. The preferred method for adding items to creative tabs is passing them here.
     *     Alternatively, the {@link ClientEventHandler#addItemsToCreativeTab(ResourceKey, Consumer)} method can be used.
     * </p>
     *
     * @param name The creative tab's registry name. Note that this is different from the tab's title.
     * @param icon The icon to display in the creative tab.
     * @param title The creative tab's display name, the one that appears in the creative mode tab. Can be a translatable string.
     * @param items A {@link Collection} of the items to display in the creative tab.
     * @return A supplier returning the registered creative tab.
     */
    public Supplier<CreativeModeTab> registerCreativeTab(String name, Supplier<? extends ItemLike> icon, String title, Collection<Supplier<? extends ItemLike>> items) {
        return this.registerCreativeTab(name, icon, Component.translatable(title), items);
    }

    /**
     * <p>
     *     Registers a {@link CreativeModeTab}.
     * </p>
     * <p>
     *     This function is used to create creative tabs for mods. The preferred method for adding items to creative tabs is the {@link #registerCreativeTab(String, Supplier, String, Collection)} method.
     *     Alternatively, the {@link ClientEventHandler#addItemsToCreativeTab(ResourceKey, Consumer)} method can be used.
     * </p>
     *
     * @param name The creative tab's registry name. Note that this is different from the tab's title.
     * @param icon The icon to display in the creative tab.
     * @param title The creative tab's display name, the one that appears in the creative mode tab. Can be a translatable string.
     * @return A supplier returning the registered creative tab.
     */
    public Supplier<CreativeModeTab> registerCreativeTab(String name, Supplier<? extends ItemLike> icon, String title) {
        return this.registerCreativeTab(name, icon, title, Set.of());
    }

    /**
     * <p>
     *     Registers an {@link EntityType}.
     * </p>
     *
     * @param name The entity's registry name.
     * @param builder Entity type builder used to create the entity.
     * @return A supplier returning the registered entity type.
     * @param <T> The entity's class.
     */
    public abstract <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder);

    /**
     * <p>
     *     Registers a {@link SpawnEggItem}.
     * </p>
     * <p>
     *     This function must be used instead of {@link ModRegistry#registerItem(String, Supplier)} to register spawn
     *     eggs because the {@link SpawnEggItem} constructor is deprecated by Forge.
     * </p>
     *
     * @param name The spawn egg's full registry name.
     * @param entity A supplier returning the entity type spawned by this spawn egg.
     * @param primaryColor The spawn egg's primary color.
     * @param secondaryColor The spawn egg's secondary color.
     * @return A supplier returning the registered spawn egg item.
     */
    public Supplier<SpawnEggItem> registerSpawnEgg(String name, Supplier<EntityType<? extends Mob>> entity, int primaryColor, int secondaryColor) {
        return this.registerItem(name, () -> new SpawnEggItem(entity.get(), primaryColor, secondaryColor, new Item.Properties()));
    }

    /**
     * <p>
     *     Registers a {@link MobEffect}.
     * </p>
     *
     * @param name Registry name of the effect
     * @param effect A supplier returning an instance of {@code MobEffect}.
     * @return A supplier returning the registered effect.
     * @param <T> The mob effect's class.
     */
    public abstract <T extends MobEffect> Supplier<T> registerEffect(String name, Supplier<T> effect);

    /**
     * <p>
     *     Registers an {@link Enchantment}.
     * </p>
     *
     * @param name Registry name of the enchantment.
     * @param enchantment A supplier returning an instance of {@code Enchantment}.
     * @return A supplier returning the registered enchantment.
     * @param <T> The enchantment's class.
     */
    public abstract <T extends Enchantment> Supplier<T> registerEnchantment(String name, Supplier<T> enchantment);

    /**
     * <p>
     *     Registers a {@link LootItemFunctionType}.
     * </p>
     *
     * @param name Registry name of the loot item function.
     * @param lootItemFunction A supplier returning an instance of {@code LootItemFunctionType}.
     * @return A supplier returning the registered loot item function.
     * @param <T> The loot item function's class.
     */
    public abstract <T extends LootItemFunctionType> Supplier<T> registerLootItemFunction(String name, Supplier<T> lootItemFunction);

    /**
     * <p>
     *     Registers a world generation {@link Feature} type.
     * </p>
     * <p>
     *     Features registered here can be used to create configured features through json files.
     *     The registry name passed here can then be used as the {@code "type"} parameter in the configured feature file.
     * </p>
     * <p>
     *     Example:
     *     <pre>
     *         REGISTRY.registerFeature("my_feature", MyFeature::new);
     *     </pre>
     *     data/example/worldgen/configured_feature/my_feature.json
     *     <pre>
     *         {
     *             "type": "example:my_feature",
     *             ...
     *         }
     *     </pre>
     * </p>
     *
     * @param name Registry name of the feature.
     * @param feature A supplier returning an instance of {@code Feature}.
     * @return A supplier returning the registered feature.
     * @param <T> The feature's class.
     */
    public abstract <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature);

    /**
     * <p>
     *     Registers a {@link MenuType} needed to create guis.
     * </p>
     *
     * @param name Registry name of the menu.
     * @param menu A function returning an {@link AbstractContainerMenu}, ideally the constructor of a class that extends {@code AbstractContainerMenu} passed as a method reference.
     * @return A supplier returning the registered menu type.
     * @param <T> The container menu class.
     */
    public abstract <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenu(String name, TriFunction<Integer, Inventory, FriendlyByteBuf, T> menu);

    /**
     * <p>
     *     Registers a {@link ParticleType}.
     * </p>
     *
     * @param name Registry name of the particles.
     * @param particles A supplier returning an instance of {@code ParticleType}.
     * @return A supplier returning the registered particles.
     * @param <T> The particles' class.
     */
    public abstract <T extends ParticleType<?>> Supplier<T> registerParticles(String name, Supplier<T> particles);

    /**
     * <p>
     *     Registers a {@link RecipeSerializer}.
     * </p>
     *
     * @param name Registry name of the recipe serializer.
     * @param recipeSerializer A supplier returning an instance of {@code RecipeSerializer}.
     * @return A supplier returning the registered recipe serializer.
     * @param <T> The recipe serializer's class.
     */
    public abstract <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String name, Supplier<T> recipeSerializer);

    /**
     * <p>
     *     Registers a {@link RecipeType}.
     * </p>
     *
     * @param name Registry name of the recipe type.
     * @param recipeType A supplier returning an instance of {@code RecipeType}.
     * @return A supplier returning the registered recipe type.
     * @param <T> The recipe type's class.
     */
    public abstract <T extends RecipeType<?>> Supplier<T> registerRecipeType(String name, Supplier<T> recipeType);

    /**
     * <p>
     *     Registers a {@link RecipeType}.
     * </p>
     *
     * @param name Registry name of the recipe type.
     * @return A supplier returning the registered recipe type.
     */
    public Supplier<RecipeType<?>> registerRecipeType(String name) {
        String identifier = this.mod + ":" + name;
        return this.registerRecipeType(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return identifier;
            }
        });
    }

    /**
     * <p>
     *     Registers a {@link SoundEvent}.
     * </p>
     *
     * @param name Registry name of the sound event.
     * @param sound A supplier returning an instance of {@code SoundEvent}.
     * @return A supplier returning the registered sound event.
     * @param <T> The sound event's class.
     */
    public abstract <T extends SoundEvent> Supplier<T> registerSound(String name, Supplier<T> sound);

    /**
     * <p>
     *     Registers a {@link SoundEvent}.
     * </p>
     *
     * @param name Registry name of the sound event.
     * @return A supplier returning the registered sound event.
     */
    public Supplier<SoundEvent> registerSound(String name) {
        return this.registerSound(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(this.mod, name)));
    }

    /**
     * <p>
     *     Registers a {@link Fluid}.
     * </p>
     *
     * @param name Registry name of the fluid.
     * @param fluid A supplier returning the fluid to register.
     * @return A supplier returning the registered fluid.
     * @param <T> Fluid class.
     */
    public abstract <T extends Fluid> Supplier<T> registerFluid(String name, Supplier<T> fluid);

    /**
     * <p>
     *     Finalizes the registry process.
     *     Must be called from the method annotated with the {@link ModEntryPoint} annotation.
     * </p>
     */
    public abstract void register();
}
