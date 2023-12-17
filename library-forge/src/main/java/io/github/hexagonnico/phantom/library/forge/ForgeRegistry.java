package io.github.hexagonnico.phantom.library.forge;

import io.github.hexagonnico.phantom.library.ModRegistry;
import io.github.hexagonnico.phantom.library.RegistryManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ForgeRegistry implements ModRegistry {

	private final DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, RegistryManager.modId());
	private final DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, RegistryManager.modId());

	@Override
	public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
		return this.items.register(name, item);
	}

	@Override
	public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
		return this.blocks.register(name, block);
	}

	@Override
	public void register() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		this.items.register(eventBus);
		this.blocks.register(eventBus);
	}
}
