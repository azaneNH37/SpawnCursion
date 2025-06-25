package com.azane.spcurs.world.feature;

import com.azane.spcurs.block.entity.TransformScEntity;
import com.azane.spcurs.registry.ModBlock;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class ScSpawnerFeature extends Feature<ScSpawnerFeature.ScSpawnerConfiguration>
{
    private static final BlockState defaultBase = Blocks.QUARTZ_BLOCK.defaultBlockState();
    private static final BlockState defaultCarpet = Blocks.QUARTZ_SLAB.defaultBlockState();
    private static final BlockState defaultColumn = Blocks.QUARTZ_PILLAR.defaultBlockState();

    @SuppressWarnings("unchecked")
    private static final List<Vec3i>[] posGroup = new List[]{
        List.of(new Vec3i(-1,0,0), new Vec3i(1,0,0), new Vec3i(0,0,-1), new Vec3i(0,0,1)),
        List.of(new Vec3i(-1,0,-1), new Vec3i(1,0,-1), new Vec3i(-1,0,1), new Vec3i(1,0,1)),
        List.of(new Vec3i(0,0,-2), new Vec3i(0,0,2), new Vec3i(-2,0,0), new Vec3i(2,0,0)),
        List.of(new Vec3i(-2,0,-1),new Vec3i(-1,0,-2), new Vec3i(1,0,-2), new Vec3i(2,0,-1),
                new Vec3i(-2,0,1), new Vec3i(-1,0,2), new Vec3i(1,0,2), new Vec3i(2,0,1)),
        List.of(new Vec3i(-2,0,-2), new Vec3i(-2,0,2), new Vec3i(2,0,-2), new Vec3i(2,0,2)),
        List.of(new Vec3i(-3,0,0), new Vec3i(3,0,0), new Vec3i(0,0,-3), new Vec3i(0,0,3)),
        List.of(new Vec3i(-3,0,-1),new Vec3i(-3,0,-2), new Vec3i(-2,0,-3), new Vec3i(-1,0,-3),
                new Vec3i(1,0,-3), new Vec3i(2,0,-3), new Vec3i(3,0,-2), new Vec3i(3,0,-1),
                new Vec3i(-3,0,1), new Vec3i(-2,0,3), new Vec3i(-1,0,3), new Vec3i(1,0,3),
                new Vec3i(2,0,3), new Vec3i(3,0,2), new Vec3i(3,0,1))
    };

    public ScSpawnerFeature()
    {
        super(ScSpawnerConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<ScSpawnerConfiguration> context)
    {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        ScSpawnerConfiguration config = context.config();

        int ranBase = random.nextInt(1,255);
        int ranCarpet = random.nextInt(1,255);

        for(int i=0;i<7;i++)
        {
            if((ranBase & (1 << i)) != 0)
            {
                BlockState baseState = getRandom(config.base, random, defaultBase);
                posGroup[i].forEach(vec3i -> level.setBlock(pos.below().offset(vec3i), baseState, 3));
            }
            if((ranCarpet & (1 << i)) != 0)
            {
                BlockState carpetState = getRandom(config.carpet, random, defaultCarpet);
                posGroup[i].forEach(vec3i -> level.setBlock(pos.offset(vec3i), carpetState, 3));
            }
        }

        int columnNum = random.nextInt(3, 6);
        for(int i=0;i<columnNum;i++)
        {
            Vec3i vec3i = new Vec3i(
                random.nextIntBetweenInclusive(-8,8),
                random.nextIntBetweenInclusive(0,2),
                random.nextIntBetweenInclusive(-8,8));
            if(vec3i.equals(Vec3i.ZERO))
                continue;
            BlockPos columnPos = pos.offset(vec3i);
            BlockState columnState = getRandom(config.column, random, defaultColumn);
            int columnHeight = random.nextIntBetweenInclusive(2, 5);
            for(int j=0;j<columnHeight;j++)
            {
                BlockPos currentPos = columnPos.above(j);
                level.setBlock(currentPos, columnState, 3);
            }
            level.setBlock(columnPos.above(columnHeight),Blocks.SEA_LANTERN.defaultBlockState(),3);
        }
        level.setBlock(pos, ModBlock.LOADER.block.get().defaultBlockState(), 3);
        if(level.getBlockEntity(pos) instanceof TransformScEntity transformScEntity)
            transformScEntity.setBaseSpawnerID(config.spawnerID);
        return true;
    }

    private BlockState getRandom(List<BlockState> list,RandomSource randomSource,BlockState defaultState)
    {
        if(list == null || list.isEmpty())
            return defaultState;
        int index = (int) (randomSource.nextDouble() * list.size());
        return list.get(index);
    }

    public record ScSpawnerConfiguration(ResourceLocation spawnerID, List<BlockState> base, List<BlockState> carpet,
                                         List<BlockState> column) implements FeatureConfiguration
    {
        public static final Codec<ScSpawnerConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("spawner").forGetter(config -> config.spawnerID),
                Codec.list(BlockState.CODEC).fieldOf("base").forGetter(config -> config.base),
                Codec.list(BlockState.CODEC).fieldOf("carpet").forGetter(config -> config.carpet),
                Codec.list(BlockState.CODEC).fieldOf("column").forGetter(config -> config.column)
            ).apply(instance, ScSpawnerConfiguration::new)
        );

    }
}
