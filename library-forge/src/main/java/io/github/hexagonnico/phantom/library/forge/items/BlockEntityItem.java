package io.github.hexagonnico.phantom.library.forge.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class BlockEntityItem extends BlockItem {

	public BlockEntityItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				Minecraft minecraft = Minecraft.getInstance();
				Block block = getBlock();
				BlockEntity blockEntity = block instanceof EntityBlock entityBlock ? entityBlock.newBlockEntity(BlockPos.ZERO, block.defaultBlockState()) : null;
				return new BlockEntityWithoutLevelRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels()) {
					@Override
					public void renderByItem(@NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int x, int y) {
						if(blockEntity != null) {
							minecraft.getBlockEntityRenderDispatcher().renderItem(blockEntity, poseStack, multiBufferSource, x, y);
						}
					}
				};
			}
		});
	}
}
