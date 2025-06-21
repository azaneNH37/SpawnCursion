package com.azane.spcurs.block;

import com.azane.spcurs.block.entity.SpcursSpawnerBlockEntity;
import com.azane.spcurs.registry.ModBlock;
import com.azane.spcurs.registry.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class SpcursSpawnerBlock extends BaseEntityBlock
{
    public SpcursSpawnerBlock()
    {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.5f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any());
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new SpcursSpawnerBlockEntity(pPos,pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType)
    {
        return createTickerHelper(pBlockEntityType, ModBlockEntity.SPCURS_SPAWNER_ENTITY.get(), pLevel.isClientSide ? SpcursSpawnerBlockEntity::clientTick : SpcursSpawnerBlockEntity::serverTick);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston)
    {
        if(pLevel.isClientSide())
            return;
        for(int dx = -1; dx <= 1; dx++)
        {
            for(int dy = -1; dy <= 1; dy++)
            {
                for(int dz = -1; dz <= 1; dz++)
                {
                    if(dx == 0 && dy == 0 && dz == 0) continue;
                    BlockPos offsetPos = pPos.offset(dx, dy, dz);
                    pLevel.setBlock(offsetPos, ModBlock.WRAPPER.block.get().defaultBlockState(),3);
                }
            }
        }
    }
}
