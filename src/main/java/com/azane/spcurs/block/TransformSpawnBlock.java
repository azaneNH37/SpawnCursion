package com.azane.spcurs.block;

import com.azane.spcurs.block.entity.SpcursSpawnerBlockEntity;
import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.genable.data.sc.collection.ScEffects;
import com.azane.spcurs.registry.ModBlock;
import com.azane.spcurs.spawn.IEnterScSpawner;
import com.azane.spcurs.util.ScTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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
    public void doScSpawnerReplacement(ServerLevel level, BlockPos pos, BlockState state, ScSpawnerGen gen, Supplier<ScEffects> modifier)
    {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        BlockPos newPos = posModifier.apply(pos);
        level.setBlock(newPos, ModBlock.SPAWNER.block.get().defaultBlockState(), 3);
        SpcursSpawnerBlockEntity.setSpawner(level,newPos,gen.getSpawnerID(level, newPos, state),modifier);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston)
    {
        pLevel.scheduleTick(pPos, this, 10);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom)
    {
        DebugLogger.log("StateChangeBlock tick at " + pPos);
        doScSpawnerReplacement(pLevel, pPos, pState,
            (level, pos, state) -> ScTags.getSpawnerTag(ScTags.SC_LV1).getRandom(),null);
    }
}
