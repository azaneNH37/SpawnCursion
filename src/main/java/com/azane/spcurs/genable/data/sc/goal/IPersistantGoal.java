package com.azane.spcurs.genable.data.sc.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public interface IPersistantGoal
{
    ResourceLocation getGoalType();

    default String asJsonString(){return GoalPersistantHelper.GSON.toJson(this);}

    void applyGoalToEntity(ServerLevel level, BlockPos blockPos, LivingEntity entity,boolean isRecreate);
}
