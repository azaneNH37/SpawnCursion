package com.azane.spcurs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WrapperBlock extends Block
{
    public WrapperBlock()
    {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.5f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any());
    }
}
