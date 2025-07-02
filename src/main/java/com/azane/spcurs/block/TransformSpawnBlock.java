package com.azane.spcurs.block;

import com.azane.spcurs.block.entity.SpcursSpawnerBlockEntity;
import com.azane.spcurs.genable.data.sc.collection.ScEffects;
import com.azane.spcurs.item.ScMycoItem;
import com.azane.spcurs.registry.ModBlock;
import com.azane.spcurs.spawn.IEnterScSpawner;
import com.azane.spcurs.spawn.SpcursEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Function;
import java.util.function.Supplier;

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
        this(Properties.of().mapColor(ModBlock.SPAWNER.block.get().defaultMapColor()).strength(2.5f).noOcclusion(),
            BlockPos::above);
    }

    @Override
    public void setBaseSpawnerID(ResourceLocation rl)
    {

    }

    @Override
    public SpcursEntity doScSpawnerReplacement(ServerLevel level, BlockPos pos, BlockState state, ScSpawnerGen gen, Supplier<ScEffects> modifier)
    {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        BlockPos newPos = posModifier.apply(pos);
        level.setBlock(newPos, ModBlock.SPAWNER.block.get().defaultBlockState(), 3);
        return SpcursSpawnerBlockEntity.setSpawner(level,newPos,gen.getSpawnerID(level, newPos, state),modifier);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!level.isClientSide) {
            ItemStack heldItem = player.getItemInHand(hand);
            if(heldItem.getItem() instanceof ScMycoItem scMycoItem)
            {
                ItemStack inserted = heldItem.copy();
                inserted.setCount(1);
                heldItem.shrink(1);
                doScSpawnerReplacement((ServerLevel) level,pos,state,(gen, ppos, pstate) -> scMycoItem.getScSpawnerID(inserted),null);
            }
            else
                return InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }
}
