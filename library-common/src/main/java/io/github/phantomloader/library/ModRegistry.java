package io.github.phantomloader.library;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.BiFunction;
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
	 * @param blocks A collection of blocks that use this block entity.
	 * @return A supplier returning the registered block entity type.
	 * @param <T> The block entity's class.
	 */
	public abstract <T extends BlockEntity> Supplier<BlockEntityType<? extends T>> registerBlockEntity(String name, BiFunction<BlockPos, BlockState, T> blockEntity, Collection<Supplier<? extends Block>> blocks);

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
	public <T extends BlockEntity> Supplier<BlockEntityType<? extends T>> registerBlockEntity(String name, BiFunction<BlockPos, BlockState, T> blockEntity, Supplier<? extends Block> block) {
		return this.registerBlockEntity(name, blockEntity, Set.of(block));
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

	public abstract <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature);

	/**
	 * <p>
	 *     Finalizes the registry process.
	 *     Must be called from the method annotated with the {@link Mod} annotation.
	 * </p>
	 */
	public abstract void register();
}
