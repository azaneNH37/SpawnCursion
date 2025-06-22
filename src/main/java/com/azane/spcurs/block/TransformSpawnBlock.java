package com.azane.spcurs.block;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.block.entity.SpcursSpawnerBlockEntity;
import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.registry.ModBlock;
import com.azane.spcurs.spawn.IEnterScSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class TransformSpawnBlock extends Block implements IEnterScSpawner
{
    public final Function<BlockPos,BlockPos> posModifier;

    public TransformSpawnBlock(Properties pProperties,Function<BlockPos,BlockPos> posModifier)
    {
        super(pProperties);
        this.posModifier = posModifier;
    }
    public TransformSpawnBlock()
    {
        this(Properties.of().mapColor(ModBlock.SPAWNER.block.get().defaultMapColor()).strength(2.5f),
            BlockPos::above);
    }

    @Override
    public ResourceLocation getScSpawnerID(ServerLevel level, BlockPos pos, BlockState state)
    {
        return ResourceLocation.tryBuild(SpcursMod.MOD_ID,"test_ai");
    }

    @Override
    public void doScSpawnerReplacement(ServerLevel level, BlockPos pos, BlockState state)
    {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        BlockPos newPos = posModifier.apply(pos);
        level.setBlock(newPos, ModBlock.SPAWNER.block.get().defaultBlockState(), 3);
        SpcursSpawnerBlockEntity.setSpawner(level,newPos,getScSpawnerID(level,pos,state));
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston)
    {
        pLevel.scheduleTick(pPos, this, 20);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom)
    {
        DebugLogger.log("StateChangeBlock tick at " + pPos);
        doScSpawnerReplacement(pLevel, pPos, pState);
    }
}
