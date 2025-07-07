package com.azane.spcurs.block;

import com.azane.spcurs.block.entity.CasinoBlockEntity;
import com.azane.spcurs.item.ScMycoItem;
import com.azane.spcurs.lib.AsyncHandler;
import com.azane.spcurs.registry.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

public class CasinoBlock extends BaseEntityBlock
{
    public CasinoBlock()
    {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.5F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CasinoBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CasinoBlockEntity casino) {
                ItemStack heldItem = player.getItemInHand(hand);

                if (heldItem.isEmpty()) {
                    // 空手使用
                    if (!casino.isFull()) {
                        ItemStack extracted = casino.extractItem((ServerPlayer) player);
                        if (!extracted.isEmpty()) {
                            player.getInventory().add(extracted);
                        }
                    } else {
                        casino.consumeAll((ServerLevel) level, (ServerPlayer) player,this.defaultBlockState());
                    }
                } else {
                    if(!(heldItem.getItem() instanceof ScMycoItem))
                        return InteractionResult.PASS;
                    // 持物品使用
                    if (!casino.isFull()) {
                        ItemStack inserted = heldItem.copy();
                        inserted.setCount(1);
                        if (casino.insertItem(inserted,(ServerPlayer) player)) {
                            heldItem.shrink(1);
                        }
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CasinoBlockEntity casino) {
                casino.dropContents(level, pos);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
