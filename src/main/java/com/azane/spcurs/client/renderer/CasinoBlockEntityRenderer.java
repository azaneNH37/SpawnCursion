package com.azane.spcurs.client.renderer;

import com.azane.spcurs.block.entity.CasinoBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CasinoBlockEntityRenderer implements BlockEntityRenderer<CasinoBlockEntity>
{
    public CasinoBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(CasinoBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        //DebugLogger.logReduced("render"," CasinoBlockEntityRenderer");
        for (int i = 0; i < 2; i++) {
            ItemStack stack = blockEntity.getItem(i);
            if (!stack.isEmpty()) {
                //DebugLogger.logReduced("item"," "+i+" "+stack.getItem().getDescriptionId());
                poseStack.pushPose();

                // 位置偏移
                poseStack.translate(i, 1.2 , 0.5);

                // 旋转动画
                if(blockEntity.getLevel() != null)
                {
                    long gameTime = blockEntity.getLevel().getGameTime();
                    float rotation = (gameTime + partialTick) * 4.0F;
                    poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
                    //DebugLogger.log("{}",partialTick);
                    float scale = 1.2F + Mth.sin(gameTime*0.1F)*0.2F;
                    poseStack.scale(scale,scale,scale);
                }


                // 缩放
                //poseStack.scale(0.5F, 0.5F, 0.5F);

                Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack, ItemDisplayContext.GROUND, packedLight, packedOverlay,
                    poseStack, bufferSource, blockEntity.getLevel(), 0);

                poseStack.popPose();
            }
        }
    }
}
