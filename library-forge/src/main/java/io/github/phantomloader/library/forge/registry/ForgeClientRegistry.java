package io.github.phantomloader.library.forge.registry;

import io.github.phantomloader.library.registry.ClientRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * <p>
 *     Forge event handler used for mod client events.
 * </p>
 *
 * @author Nico
 */
@Mod.EventBusSubscriber(modid = "phantom", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientRegistry implements ClientRegistry {

    private static final HashMap<ResourceKey<CreativeModeTab>, HashSet<Supplier<? extends ItemLike>>> CREATIVE_TABS = new HashMap<>();
    private static final HashMap<Supplier<?>, EntityRendererProvider<?>> ENTITY_RENDERERS = new HashMap<>();
    private static final HashMap<Supplier<?>, BlockEntityRendererProvider<?>> BLOCK_ENTITY_RENDERERS = new HashMap<>();

    /**
     * <p>
     *     Forge event handler used to add items to creative mode tabs.
     * </p>
     *
     * @param event Forge event
     */
    @SubscribeEvent
    public static void creativeTabEvent(BuildCreativeModeTabContentsEvent event) {
        if(CREATIVE_TABS.containsKey(event.getTabKey())) {
            CREATIVE_TABS.get(event.getTabKey()).forEach(item -> event.accept(item.get()));
        }
    }

    /**
     * <p>
     *     Forge event handler used to register entity and block entity renderers.
     * </p>
     *
     * @param event Forge event
     */
    @SubscribeEvent
    public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
        registerEntityRenderers(event::registerEntityRenderer);
        registerBlockEntityRenderers(event::registerBlockEntityRenderer);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> void registerEntityRenderers(BiConsumer<EntityType<T>, EntityRendererProvider<T>> consumer) {
        ENTITY_RENDERERS.forEach((entity, renderer) -> consumer.accept((EntityType<T>) entity.get(), (EntityRendererProvider<T>) renderer));
    }

    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity> void registerBlockEntityRenderers(BiConsumer<BlockEntityType<? extends T>, BlockEntityRendererProvider<T>> consumer) {
        BLOCK_ENTITY_RENDERERS.forEach((blockEntity, renderer) -> consumer.accept((BlockEntityType<? extends T>) blockEntity.get(), (BlockEntityRendererProvider<T>) renderer));
    }

    @Override
    public void addItemsToCreativeTab(ResourceKey<CreativeModeTab> resourceKey, Collection<Supplier<? extends ItemLike>> items) {
        if(CREATIVE_TABS.containsKey(resourceKey)) {
            CREATIVE_TABS.get(resourceKey).addAll(items);
        } else {
            CREATIVE_TABS.put(resourceKey, new HashSet<>(items));
        }
    }

    @Override
    public <T extends Entity> void registerEntityRenderer(Supplier<EntityType<T>> entity, EntityRendererProvider<T> renderer) {
        ENTITY_RENDERERS.put(entity, renderer);
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> blockEntity, BlockEntityRendererProvider<T> renderer) {
        BLOCK_ENTITY_RENDERERS.put(blockEntity, renderer);
    }
}
