package com.azane.spcurs.client.lib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class CubeFrameRenderer
{
    /**
     *
     * @param active flag to indicate which frame is active,1 Z+ 2 Z- 4 X+ 8 X- 16 Y+ 32 Y-
     * @param size
     * @param colors
     * @param poseStack
     * @param vertexConsumer
     * @param packedLight
     * @param packedOverlay
     */
    public static void renderCubeFrame(int active,float size, int[] colors, PoseStack poseStack, VertexConsumer vertexConsumer,
                                 int packedLight, int packedOverlay) {
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        // 渲染六个面，每面3x3
        float offset = -size/2 + 0.5f; // 中心偏移

        // 前面 (Z+) - 四个顶点顺序：左下->右下->右上->左上
        if((active & 1) != 0)
            renderFaceQuad(matrix, normal, vertexConsumer,
            offset, offset, offset + size,           // 左下
            offset + size, offset, offset + size,    // 右下
            offset + size, offset + size, offset + size, // 右上
            offset, offset + size, offset + size,    // 左上
            0, 0, 1, packedLight, packedOverlay,
            colors[0], colors[1], colors[2], colors[3]);

        // 后面 (Z-) - 顶点顺序相反以正确显示
        if((active & 2) != 0)
            renderFaceQuad(matrix, normal, vertexConsumer,
            offset + size, offset, offset,           // 右下
            offset, offset, offset,                  // 左下
            offset, offset + size, offset,           // 左上
            offset + size, offset + size, offset,    // 右上
            0, 0, -1, packedLight, packedOverlay,
            colors[0], colors[1], colors[2], colors[3]);

        // 右面 (X+)
        if((active & 4) != 0)
            renderFaceQuad(matrix, normal, vertexConsumer,
            offset + size, offset, offset + size,    // 前下
            offset + size, offset, offset,           // 后下
            offset + size, offset + size, offset,    // 后上
            offset + size, offset + size, offset + size, // 前上
            1, 0, 0, packedLight, packedOverlay,
            colors[0], colors[1], colors[2], colors[3]);

        // 左面 (X-)
        if((active & 8) != 0)
            renderFaceQuad(matrix, normal, vertexConsumer,
            offset, offset, offset,                  // 后下
            offset, offset, offset + size,           // 前下
            offset, offset + size, offset + size,    // 前上
            offset, offset + size, offset,           // 后上
            -1, 0, 0, packedLight, packedOverlay,
            colors[0], colors[1], colors[2], colors[3]);

        // 上面 (Y+)
        if((active & 16) != 0)
            renderFaceQuad(matrix, normal, vertexConsumer,
            offset, offset + size, offset + size,    // 左前
            offset + size, offset + size, offset + size, // 右前
            offset + size, offset + size, offset,    // 右后
            offset, offset + size, offset,           // 左后
            0, 1, 0, packedLight, packedOverlay,
            colors[0], colors[1], colors[2], colors[3]);

        // 下面 (Y-)
        if((active & 32) != 0)
            renderFaceQuad(matrix, normal, vertexConsumer,
            offset, offset, offset,                  // 左后
            offset + size, offset, offset,           // 右后
            offset + size, offset, offset + size,    // 右前
            offset, offset, offset + size,           // 左前
            0, -1, 0, packedLight, packedOverlay,
            colors[0], colors[1], colors[2], colors[3]);

    }

    public static void renderFaceQuad(Matrix4f matrix, Matrix3f normal, VertexConsumer vertexConsumer,
                                float x1, float y1, float z1, // 顶点1
                                float x2, float y2, float z2, // 顶点2
                                float x3, float y3, float z3, // 顶点3
                                float x4, float y4, float z4, // 顶点4
                                float nx, float ny, float nz, int packedLight, int packedOverlay,
                                int alpha,int red, int green, int blue
    ) {

        // 渲染四个顶点构成的面，确保顶点顺序正确
        vertexConsumer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha)
            .uv(0.0f, 1.0f).overlayCoords(packedOverlay)
            .uv2(packedLight).normal(normal, nx, ny, nz).endVertex();

        vertexConsumer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha)
            .uv(1.0f, 1.0f).overlayCoords(packedOverlay)
            .uv2(packedLight).normal(normal, nx, ny, nz).endVertex();

        vertexConsumer.vertex(matrix, x3, y3, z3).color(red, green, blue, alpha)
            .uv(1.0f, 0.0f).overlayCoords(packedOverlay)
            .uv2(packedLight).normal(normal, nx, ny, nz).endVertex();

        vertexConsumer.vertex(matrix, x4, y4, z4).color(red, green, blue, alpha)
            .uv(0.0f, 0.0f).overlayCoords(packedOverlay)
            .uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
    }
}
