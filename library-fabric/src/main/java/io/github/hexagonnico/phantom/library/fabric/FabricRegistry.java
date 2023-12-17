package io.github.hexagonnico.phantom.library.fabric;

import io.github.hexagonnico.phantom.library.ModRegistry;
import io.github.hexagonnico.phantom.library.RegistryManager;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class FabricRegistry implements ModRegistry {

	private ResourceLocation identifier(String name) {
		return new ResourceLocation(RegistryManager.modId(), name);
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
	public void register() {

	}
}
