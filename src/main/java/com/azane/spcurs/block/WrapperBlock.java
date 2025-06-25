package com.azane.spcurs.block;

import com.azane.spcurs.registry.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;

public class WrapperBlock extends Block
{
    public static final IntegerProperty HEIGHT = IntegerProperty.create("height",0,5);

    public WrapperBlock()
    {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(-1).explosionResistance(3600000).noOcclusion().lightLevel(state->15));
        this.registerDefaultState(this.stateDefinition.any().setValue(HEIGHT,2));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
    {
        pBuilder.add(HEIGHT);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston)
    {
        if(pLevel.isClientSide)return;
        if(pMovedByPiston)return;
        pLevel.scheduleTick(pPos, this, pLevel.getRandom().nextInt(1,3));
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom)
    {
        if(pState.is(ModBlock.WRAPPER.block.get()) && pState.getValue(HEIGHT) > 0)
        {
            if(!pLevel.getBlockState(pPos.above()).is(ModBlock.SPAWNER.block.get()))
                pLevel.setBlock(pPos.above(),pState.setValue(HEIGHT,pState.getValue(HEIGHT)-1),3);
        }
    }
}
