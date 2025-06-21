package com.azane.spcurs.client.renderer;

import com.azane.spcurs.block.entity.SpcursSpawnerBlockEntity;
import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.genable.data.sc.ScSpawner;
import com.azane.spcurs.resource.service.ClientDataService;
import com.azane.spcurs.spawn.SpcursEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class SpcursSpawnerBlockEntityRenderer implements BlockEntityRenderer<SpcursSpawnerBlockEntity>
{
    public SpcursSpawnerBlockEntityRenderer(BlockEntityRendererProvider.Context context){}

    @Override
    public void render(SpcursSpawnerBlockEntity pBlockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay)
    {
        SpcursEntity entity = pBlockEntity.getSpawner();
        if(entity == null)
        {
            //DebugLogger.logReduced("ScRender", "SpcursSpawnerBlockEntityRenderer: render: entity is null, blockPos: " + pBlockEntity.getBlockPos());
            return;
        }
        ScSpawner spawner = ClientDataService.get().getSpawner(entity.getSpawnerID());
        ResourceLocation texture = spawner.getDisplayContext().getImgRl();
        texture = ResourceLocation.tryBuild(texture.getNamespace(),"textures/"+texture.getPath()+".png");
        int color = spawner.getDisplayContext().getRenderColor();
        BlockPos pos = pBlockEntity.getBlockPos();
        Vec3 centre = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        float size = 3.2f; // 立方体的大小
        int[] colors = new int[]{
            FastColor.ARGB32.alpha(color), // 透明度分量
            FastColor.ARGB32.red(color),   // 红色分量
            FastColor.ARGB32.green(color), // 绿色分量
            FastColor.ARGB32.blue(color)    // 蓝色分量
        };

        // 准备顶点缓冲区
        VertexConsumer buffer = pBuffer.getBuffer(RenderType.entityTranslucent(texture));

        // 设置变换矩阵
        poseStack.pushPose();

        renderCubeFrame(3.2f,colors,poseStack, buffer, LightTexture.FULL_BRIGHT, pPackedOverlay);

        poseStack.translate(0.5f,0.5f,0.5f);
        float offset = -size/2;
        LevelRenderer.renderLineBox(
            poseStack,
            pBuffer.getBuffer(RenderType.LINES),
            offset,offset,offset,
            -offset, -offset, -offset,
            colors[1]/255f, colors[2]/255f, colors[3]/255f, 1.0f
        );

        poseStack.popPose();
    }

    private void renderCubeFrame(float size,int[] colors,PoseStack poseStack, VertexConsumer vertexConsumer,
                                 int packedLight, int packedOverlay) {
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        // 渲染六个面，每面3x3
        float offset = -size/2 + 0.5f; // 中心偏移

        // 前面 (Z+) - 四个顶点顺序：左下->右下->右上->左上
        renderFaceQuad(matrix, normal, vertexConsumer,
            offset, offset, offset + size,           // 左下
            offset + size, offset, offset + size,    // 右下
            offset + size, offset + size, offset + size, // 右上
            offset, offset + size, offset + size,    // 左上
            0, 0, 1, packedLight, packedOverlay,
            colors[0], colors[1], colors[2], colors[3]);

        // 后面 (Z-) - 顶点顺序相反以正确显示
        renderFaceQuad(matrix, normal, vertexConsumer,
            offset + size, offset, offset,           // 右下
            offset, offset, offset,                  // 左下
            offset, offset + size, offset,           // 左上
            offset + size, offset + size, offset,    // 右上
            0, 0, -1, packedLight, packedOverlay,
            colors[0], colors[1], colors[2], colors[3]);

        // 右面 (X+)
        renderFaceQuad(matrix, normal, vertexConsumer,
            offset + size, offset, offset + size,    // 前下
            offset + size, offset, offset,           // 后下
            offset + size, offset + size, offset,    // 后上
            offset + size, offset + size, offset + size, // 前上
            1, 0, 0, packedLight, packedOverlay,
            colors[0], colors[1], colors[2], colors[3]);

        // 左面 (X-)
        renderFaceQuad(matrix, normal, vertexConsumer,
            offset, offset, offset,                  // 后下
            offset, offset, offset + size,           // 前下
            offset, offset + size, offset + size,    // 前上
            offset, offset + size, offset,           // 后上
            -1, 0, 0, packedLight, packedOverlay,
            colors[0], colors[1], colors[2], colors[3]);

        // 上面 (Y+)
        /*
        renderFaceQuad(matrix, normal, vertexConsumer,
            offset, offset + size, offset + size,    // 左前
            offset + size, offset + size, offset + size, // 右前
            offset + size, offset + size, offset,    // 右后
            offset, offset + size, offset,           // 左后
            0, 1, 0, packedLight, packedOverlay);

        // 下面 (Y-)
        renderFaceQuad(matrix, normal, vertexConsumer,
            offset, offset, offset,                  // 左后
            offset + size, offset, offset,           // 右后
            offset + size, offset, offset + size,    // 右前
            offset, offset, offset + size,           // 左前
            0, -1, 0, packedLight, packedOverlay);
         */
    }

    private void renderFaceQuad(Matrix4f matrix, Matrix3f normal, VertexConsumer vertexConsumer,
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
