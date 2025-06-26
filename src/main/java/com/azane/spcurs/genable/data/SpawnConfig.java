package com.azane.spcurs.genable.data;

import com.azane.spcurs.lib.IComponentDisplay;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

@Getter
public class SpawnConfig implements IComponentDisplay
{
    @SerializedName("kill_amt")
    private int killAmt;
    @SerializedName("create_amt")
    private int createAmt;
    @SerializedName("existing_amt")
    private int existingAmt;
    @SerializedName("spawn_cnt")
    private int spawnCnt;
    @SerializedName("interval")
    private long interval = 400;
    @SerializedName("strategy")
    private SpawnStrategy strategy = SpawnStrategy.limit;
    @SerializedName("range")
    private double range;
    @SerializedName("first_spawn")
    private long firstSpawn = 5;

    @Override
    public void appendHoverText(ItemStack stack, List<Component> tooltip, TooltipFlag flag)
    {
        ((MutableComponent)(tooltip.get(tooltip.size()-1))).append(" ").append(
            Component.translatable("spcurs.sc.config.strategy." + strategy.name())
                .withStyle(ChatFormatting.BOLD)
                .withStyle(strategy == SpawnStrategy.create ? ChatFormatting.GREEN : ChatFormatting.RED)
                .append(String.valueOf(expectedSpawnAmt())));
        if(flag.isAdvanced() || flag.isCreative())
        {
            tooltip.add(Component.translatable("spcurs.sc.config.counter",killAmt,createAmt,existingAmt).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("spcurs.sc.config.per_spawn",spawnCnt).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("spcurs.sc.config.timer",firstSpawn,interval).withStyle(ChatFormatting.GRAY).append(": " + interval));
            tooltip.add(Component.translatable("spcurs.sc.config.range",range).withStyle(ChatFormatting.GRAY));
        }
    }

    public enum SpawnStrategy
    {
        kill,
        create,
        limit
    }

    public boolean spawnAvailable(int curKill,int curSpawn,int curExist)
    {
        return switch (strategy) {
            case kill -> curKill < killAmt;
            case create -> curSpawn < createAmt;
            case limit -> curExist < existingAmt && curKill < killAmt;
        };
    }
    public boolean spawnFinished(int curKill,int curSpawn,int curExist)
    {
        return switch (strategy) {
            case kill, limit -> curKill >= killAmt;
            case create -> curSpawn >= createAmt;
        };
    }
    public int expectedSpawnAmt()
    {
        return switch (strategy) {
            case kill,limit -> killAmt;
            case create -> createAmt;
        };
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("SpawnConfig{");
        builder.append("\n  killAmt=").append(killAmt)
            .append("\t  createAmt=").append(createAmt)
            .append("\t  existingAmt=").append(existingAmt)
            .append("\t  spawnCnt=").append(spawnCnt)
            .append("\n  interval=").append(interval)
            .append("\t  strategy=").append(strategy)
            .append("\t  range=").append(range)
            .append("\n  firstSpawn=").append(firstSpawn)
            .append("\n}");
        return builder.toString();
    }
}
