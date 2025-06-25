package com.azane.spcurs.spawn;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface IEnterScSpawner
{
    @FunctionalInterface
    interface ScSpawnerGen{
        ResourceLocation getSpawnerID(ServerLevel level, BlockPos pos, BlockState state);
    }

    void setBaseSpawnerID(ResourceLocation rl);

    void doScSpawnerReplacement(ServerLevel level, BlockPos pos,BlockState state,ScSpawnerGen gen);
}
