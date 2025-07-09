package com.azane.spcurs.block;

import com.azane.spcurs.block.entity.TransformScEntity;
import com.azane.spcurs.genable.tag.ScSpawnerTag;
import com.azane.spcurs.item.ScMycoItem;
import com.azane.spcurs.registry.ModConfig;
import com.azane.spcurs.registry.ModBlockEntity;
import com.azane.spcurs.resource.service.ServerDataService;
import com.azane.spcurs.spawn.IEnterScSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LoadSpawnBlock extends BaseEntityBlock
{
    public LoadSpawnBlock()
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
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType)
    {
        return createTickerHelper(pBlockEntityType, ModBlockEntity.TRANS_SC_ENTITY.get(), pLevel.isClientSide ? TransformScEntity::clientTick : TransformScEntity::serverTick);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom)
    {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof TransformScEntity transformScEntity)
        {
            transformScEntity.doScSpawnerReplacement(pLevel, pPos, pState,
                (level, pos, state) -> {
                    ResourceLocation rl = transformScEntity.getBaseSpawnerID();
                    ScSpawnerTag tag = ServerDataService.get().getSpawnerTag(rl);
                    if(tag != null)
                        return tag.getRandom();
                    else
                        return rl;
                }, transformScEntity::getTempSpawnModifier);
            transformScEntity.setChild(transformScEntity.isChild());
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!level.isClientSide) {
            ItemStack heldItem = player.getItemInHand(hand);
            if(heldItem.getItem() instanceof ScMycoItem scMycoItem)
            {
                if(level.getBlockEntity(pos) instanceof IEnterScSpawner enterScSpawner)
                {
                    ItemStack inserted = heldItem.copy();
                    inserted.setCount(1);
                    heldItem.shrink(1);
                    enterScSpawner.setBaseSpawnerID(scMycoItem.getScSpawnerID(inserted));
                }
            }
            else
                return InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }
}
