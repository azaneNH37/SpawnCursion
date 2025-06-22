package com.azane.spcurs.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class WrapperBlock extends Block
{
    public WrapperBlock()
    {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.5f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any());
    }
}
