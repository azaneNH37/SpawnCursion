package com.azane.spcurs.debug.block;

import com.azane.spcurs.debug.log.DebugLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class StateChangeBlock extends Block
{
    public StateChangeBlock()
    {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.5f));
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
        pLevel.setBlock(pPos, Blocks.STONE.defaultBlockState(), 3);
    }
}
