package com.azane.spcurs.client.renderer;

import com.azane.spcurs.block.entity.SpcursSpawnerBlockEntity;
import com.azane.spcurs.client.lib.CubeFrameRenderer;
import com.azane.spcurs.client.lib.FaceLocalPlayerStack;
import com.azane.spcurs.client.lib.SimpleProgressBarRenderer;
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
        //DebugLogger.logReduced("ScRender", "SpcursSpawnerBlockEntityRenderer: render: entity is null, blockPos: " + pBlockEntity.getBlockPos());
        if(entity == null)
            return;
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

        CubeFrameRenderer.renderCubeFrame(15,3.2f,colors,poseStack, buffer, LightTexture.FULL_BRIGHT, pPackedOverlay);

        poseStack.translate(0.5f,0.5f,0.5f);
        float offset = -size/2;
        LevelRenderer.renderLineBox(
            poseStack,
            pBuffer.getBuffer(RenderType.LINES),
            offset,offset,offset,
            -offset, -offset, -offset,
            colors[1]/255f, colors[2]/255f, colors[3]/255f, 1.0f
        );

        FaceLocalPlayerStack.push(poseStack, pos, new Vec3(0.5, 3, 0.5));

        DebugLogger.logReduced("render","spawn:{} kill:{}",pBlockEntity.getDisplay().getSpawnProgress(),pBlockEntity.getDisplay().getKillProgress());
        SimpleProgressBarRenderer.renderProgressBar(poseStack, pBuffer,
            LightTexture.FULL_BRIGHT, pPackedOverlay,
            new Vec3(0,0,0), 7f, 1f,
            pBlockEntity.getDisplay().getSpawnProgress(),
            pBlockEntity.getDisplay().getKillProgress()
        );

        FaceLocalPlayerStack.pop(poseStack);

        poseStack.popPose();
    }
}
