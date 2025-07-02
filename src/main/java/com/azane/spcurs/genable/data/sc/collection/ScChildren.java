package com.azane.spcurs.genable.data.sc.collection;

import com.azane.spcurs.block.entity.TransformScEntity;
import com.azane.spcurs.genable.data.sc.ScChildConfig;
import com.azane.spcurs.lib.IComponentDisplay;
import com.azane.spcurs.lib.LevelHelper;
import com.azane.spcurs.registry.ModBlock;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ScChildren implements IComponentDisplay
{
    @SerializedName("list")
    @Getter
    private List<ScChildConfig> list = new ArrayList<>();

    public void init()
    {
        list.sort((a, b) -> {
            if (a.getDelay() == b.getDelay())
                return 0;
            return a.getDelay() < b.getDelay() ? -1 : 1;
        });
        list.forEach(config-> {
            if (config.getOffset() == null || config.getOffset().length != 3)
                config.setOffset(new double[]{0, 0, 0});
        });
    }

    public boolean tryPlace(ServerLevel level, BlockPos origin,long tick,int index)
    {
        if(index < 0 || index >= list.size())
            return false;
        ScChildConfig spawnConfig = list.get(index);
        if(tick >= spawnConfig.getDelay())
        {
            placeChild(level, origin, index);
            return true;
        }
       return false;
    }

    private void placeChild(ServerLevel level,BlockPos origin, int index)
    {
        if(index < 0 || index >= list.size())
            return;
        RandomSource random = level.getRandom();
        ScChildConfig spawnConfig = list.get(index);
        for(int i=0;i<spawnConfig.getAmt();i++)
        {
            double x = origin.getX() + spawnConfig.offset[0] + (random.nextDouble() - random.nextDouble()) * spawnConfig.getRange() + 0.5D;
            double z = origin.getZ() + spawnConfig.offset[2] + (random.nextDouble() - random.nextDouble()) * spawnConfig.getRange() + 0.5D;
            Position npos = new Vec3(x,origin.getY()+spawnConfig.offset[1],z);
            BlockPos rawpos = BlockPos.containing(npos);
            BlockPos finalPos = rawpos.above(LevelHelper.getGroundHightAtFullChunk(level,rawpos, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)-rawpos.getY()).above();
            if(level.getBlockState(finalPos).is(ModBlock.SPAWNER.block.get()) || level.getBlockState(finalPos).is(ModBlock.WRAPPER.block.get()))
                continue;
            level.setBlock(finalPos, ModBlock.INSTANT_LOAD.block.get().defaultBlockState(), 3);
            if(level.getBlockEntity(finalPos) instanceof TransformScEntity transformScEntity)
            {
                transformScEntity.setChild(true);
                transformScEntity.setBaseSpawnerID(spawnConfig.getId());
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, List<Component> tooltip, TooltipFlag flag)
    {

    }
}
