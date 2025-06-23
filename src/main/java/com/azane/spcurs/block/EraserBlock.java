package com.azane.spcurs.block;

import com.azane.spcurs.registry.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;

public class EraserBlock extends Block
{
    public static final IntegerProperty STATE = IntegerProperty.create("state",0,16);

    public EraserBlock()
    {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(-1).explosionResistance(3600000).noOcclusion().lightLevel(state->15));
        this.registerDefaultState(this.stateDefinition.any().setValue(WrapperBlock.HEIGHT,3).setValue(STATE,0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
    {
        pBuilder.add(WrapperBlock.HEIGHT,STATE);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston)
    {
        if(pLevel.isClientSide)return;
        if(pMovedByPiston)return;
        pLevel.scheduleTick(pPos, this, pLevel.getRandom().nextInt(2,4));
        //pLevel.scheduleTick(pPos,this,pLevel.getRandom().nextInt(4,6));
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom)
    {
        if(pState.is(ModBlock.ERASER.block.get()))
        {
            if(pState.getValue(WrapperBlock.HEIGHT) > 0)
                pLevel.setBlock(pPos.below(),pState.setValue(WrapperBlock.HEIGHT,pState.getValue(WrapperBlock.HEIGHT)-1),3);
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(),3);
        }
    }
}
