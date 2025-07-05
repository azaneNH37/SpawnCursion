package com.azane.spcurs.block;

import com.azane.spcurs.block.entity.TransformScEntity;
import com.azane.spcurs.genable.tag.ScSpawnerTag;
import com.azane.spcurs.registry.ModConfig;
import com.azane.spcurs.resource.service.ServerDataService;
import com.azane.spcurs.spawn.SpcursEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

public class InstantSpawnBlock extends BaseEntityBlock
{
    public InstantSpawnBlock()
    {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(-1).explosionResistance(3600000).pushReaction(PushReaction.IGNORE).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any());
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new TransformScEntity(pPos,pState);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston)
    {
        if(pLevel.isClientSide())
            return;
        pLevel.scheduleTick(pPos,this,pLevel.getRandom().nextInt(20,60));
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom)
    {
        if(ModConfig.DEV_MODE.get())
            return;
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof TransformScEntity transformScEntity)
        {
            SpcursEntity entity = transformScEntity.doScSpawnerReplacement(pLevel, pPos, pState,
                (level, pos, state) -> {
                    ResourceLocation rl = transformScEntity.getBaseSpawnerID();
                    ScSpawnerTag tag = ServerDataService.get().getSpawnerTag(rl);
                    if(tag != null)
                        return tag.getRandom();
                    else
                        return rl;
                },transformScEntity::getTempSpawnModifier);
            entity.setChild(transformScEntity.isChild());
        }
    }
}
