package io.github.phantomloader.library.fabric.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

	private final Block baseBlock;

	public BlockEntityItemRenderer(Block baseBlock) {
		this.baseBlock = baseBlock;
	}

	@Override
	public void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		if(this.baseBlock instanceof EntityBlock entityBlock) {
			BlockEntity blockEntity = entityBlock.newBlockEntity(BlockPos.ZERO, this.baseBlock.defaultBlockState());
			if(blockEntity != null) {
				Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(blockEntity, matrices, vertexConsumers, light, overlay);
			}
		}
	}
}
