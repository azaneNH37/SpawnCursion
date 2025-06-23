package com.azane.spcurs.client.lib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class FaceLocalPlayerStack
{
    public static void push(PoseStack poseStack, BlockPos centre,Vec3 offset)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        Vec3 playerPos = mc.player.getEyePosition();
        Vec3 blockPos = Vec3.atCenterOf(centre);

        poseStack.pushPose();

        // 移动到方块上方
        poseStack.translate(offset.x, offset.y, offset.z);

        // 计算朝向玩家的旋转
        Vec3 lookDirection = playerPos.subtract(blockPos).normalize();
        float yaw = (float) Math.atan2(lookDirection.x, lookDirection.z);
        float pitch = (float) Math.asin(-lookDirection.y);

        // 应用旋转使其始终面向玩家
        poseStack.mulPose(Axis.YP.rotation(yaw));
        poseStack.mulPose(Axis.XP.rotation(pitch));
    }

    public static void pop(PoseStack poseStack)
    {
        poseStack.popPose();
    }
}
