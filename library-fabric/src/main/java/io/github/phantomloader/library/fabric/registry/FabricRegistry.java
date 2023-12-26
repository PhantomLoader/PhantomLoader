package io.github.phantomloader.library.fabric.registry;

import io.github.phantomloader.library.fabric.FabricClientInitializer;
import io.github.phantomloader.library.registry.ModRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;

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
		if(block.get() instanceof EntityBlock) {
			FabricClientInitializer.registerEntityBlockRenderer(block.get());
		}
		return super.registerBlockItem(name, block);
	}

	@Override
	public <T extends BlockEntity> Supplier<BlockEntityType<? extends T>> registerBlockEntity(String name, BiFunction<BlockPos, BlockState, T> blockEntity, Set<Supplier<? extends Block>> blocks) {
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
	public <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature) {
		T registered = Registry.register(BuiltInRegistries.FEATURE, this.identifier(name), feature.get());
		return () -> registered;
	}

	@Override
	public void register() {

	}
}
