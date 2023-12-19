package io.github.phantomloader.library.forge;

import io.github.phantomloader.library.ModRegistry;
import io.github.phantomloader.library.forge.items.BlockEntityItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ForgeRegistry extends ModRegistry {

	private final DeferredRegister<Item> items;
	private final DeferredRegister<Block> blocks;
	private final DeferredRegister<BlockEntityType<?>> blockEntities;
	private final DeferredRegister<EntityType<?>> entities;
	private final DeferredRegister<Feature<?>> features;

	public ForgeRegistry(String mod) {
		super(mod);
		this.items = DeferredRegister.create(ForgeRegistries.ITEMS, mod);
		this.blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, mod);
		this.blockEntities = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, mod);
		this.entities = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, mod);
		this.features = DeferredRegister.create(ForgeRegistries.FEATURES, mod);
	}

	@Override
	public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
		return this.items.register(name, item);
	}

	@Override
	public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
		return this.blocks.register(name, block);
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
	public <T extends BlockEntity> Supplier<BlockEntityType<? extends T>> registerBlockEntity(String name, Supplier<? extends Block> block, BiFunction<BlockPos, BlockState, T> blockEntity) {
		return this.blockEntities.register(name, () -> BlockEntityType.Builder.of(blockEntity::apply, block.get()).build(null));
	}

	@Override
	public <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) {
		return this.entities.register(name, () -> builder.build(name));
	}

	@Override
	public Supplier<SpawnEggItem> registerSpawnEgg(String name, Supplier<EntityType<? extends Mob>> entity, int primaryColor, int secondaryColor) {
		return this.registerItem(name, () -> new ForgeSpawnEggItem(entity, primaryColor, secondaryColor, new Item.Properties()));
	}

	@Override
	public <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature) {
		return this.features.register(name, feature);
	}

	@Override
	public void register() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		this.items.register(eventBus);
		this.blocks.register(eventBus);
		this.blockEntities.register(eventBus);
		this.entities.register(eventBus);
		this.features.register(eventBus);
	}
}
