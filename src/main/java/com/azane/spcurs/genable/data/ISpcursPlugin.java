package com.azane.spcurs.genable.data;

import com.google.gson.Gson;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public interface ISpcursPlugin
{
    default void onEntityCreate(ServerLevel level, BlockPos blockPos, LivingEntity entity){}
}
