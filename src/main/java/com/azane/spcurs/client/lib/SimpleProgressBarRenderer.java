package com.azane.spcurs.client.lib;

import com.azane.spcurs.SpcursMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class SimpleProgressBarRenderer
{
    public static final ResourceLocation TEXTURE = ResourceLocation.tryBuild(SpcursMod.MOD_ID,"textures/gui/blank.png");
    public static final int[] COLOR_BAR = new int[]{
        0xFF0000FF, // 蓝色
        0xFF00FF00, // 绿色
        0xFFFF0000, // 红色
        0xFFFFFF00, // 黄色
        0xFFFF00FF, // 品红色
        0xFF00FFFF, // 青色
    };

    public static void renderProgressBar(
        PoseStack poseStack, MultiBufferSource bufferSource,
        int packedLight, int packedOverlay,
        Vec3 offset,float barWidth,float barHeight,float ... progresses)
    {
        poseStack.pushPose();
        poseStack.translate(offset.x, offset.y, offset.z);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE));
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        // 背景条
        renderProgressQuad(matrix, normal, consumer,
            -barWidth/2, -barHeight/2, 0,
            barWidth/2, barHeight/2, 0.01f,
            0xFFAAAAAA, // 灰色背景
            packedLight, packedOverlay);

        int layer = 0;
        for(float progress : progresses)
        {
            float filledWidth = barWidth * progress;
            if (progress > 0) {
                renderProgressQuad(matrix, normal, consumer,
                    -barWidth/2, -barHeight/2, 0.01f*(layer+1),
                    -barWidth/2 + filledWidth, barHeight/2, 0.01f *(layer+2),
                    COLOR_BAR[layer % COLOR_BAR.length],
                    packedLight, packedOverlay);
            }
            layer++;
        }

        poseStack.popPose();
    }

    private static void renderProgressQuad(Matrix4f matrix, Matrix3f normal, VertexConsumer vertexConsumer,
                                    float x1, float y1, float z1, float x2, float y2, float z2,
                                    int color,
                                    int packedLight, int packedOverlay) {

        // 复用renderFaceQuad的逻辑，但使用自定义颜色而非纹理
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        int alpha = (color >> 24) & 0xFF;

        // 渲染矩形的正面
        vertexConsumer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha)
            .uv(0.0f, 1.0f).overlayCoords(packedOverlay)
            .uv2(packedLight).normal(normal, 0, 0, 1).endVertex();

        vertexConsumer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha)
            .uv(1.0f, 1.0f).overlayCoords(packedOverlay)
            .uv2(packedLight).normal(normal, 0, 0, 1).endVertex();

        vertexConsumer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha)
            .uv(1.0f, 0.0f).overlayCoords(packedOverlay)
            .uv2(packedLight).normal(normal, 0, 0, 1).endVertex();

        vertexConsumer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha)
            .uv(0.0f, 0.0f).overlayCoords(packedOverlay)
            .uv2(packedLight).normal(normal, 0, 0, 1).endVertex();
    }
}
