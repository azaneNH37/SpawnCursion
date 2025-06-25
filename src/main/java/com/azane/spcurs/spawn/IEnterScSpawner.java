package com.azane.spcurs.spawn;

import com.azane.spcurs.block.entity.SpcursSpawnerBlockEntity;
import com.azane.spcurs.registry.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public interface IEnterScSpawner
{
    @FunctionalInterface
    interface ScSpawnerGen{
        ResourceLocation getSpawnerID(ServerLevel level, BlockPos pos, BlockState state);
    }

    void setBaseSpawnerID(ResourceLocation rl);

    static void placeScSpawner(ServerLevel level, BlockPos pos,BlockState state,ScSpawnerGen gen)
    {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        BlockPos newPos = pos.above();
        level.setBlock(newPos, ModBlock.SPAWNER.block.get().defaultBlockState(), 3);
        SpcursSpawnerBlockEntity.setSpawner(level,newPos,gen.getSpawnerID(level, newPos, state));
    }

    default void doScSpawnerReplacement(ServerLevel level, BlockPos pos,BlockState state,ScSpawnerGen gen)
    {
        IEnterScSpawner.placeScSpawner(level,pos,state,gen);
    }
}
