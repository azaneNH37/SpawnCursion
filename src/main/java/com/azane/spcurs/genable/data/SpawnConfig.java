package com.azane.spcurs.genable.data;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class SpawnConfig
{
    @SerializedName("kill_amt")
    private int killAmt;
    @SerializedName("create_amt")
    private int createAmt;
    @SerializedName("spawn_cnt")
    private int spawnCnt;
    @SerializedName("interval")
    private long interval = 400;
    @SerializedName("limitation")
    private SpawnLimit limitation;
    @SerializedName("range")
    private double range;
    @SerializedName("first_spawn")
    private long firstSpawn = 5;

    public enum SpawnLimit
    {
        kill,
        create,
        all,
        any
    }

    public boolean spawnAvailable(int curKill,int curSpawn)
    {
        return switch (limitation) {
            case kill -> curKill < killAmt;
            case create -> curSpawn < createAmt;
            case all -> curKill < killAmt && curSpawn < createAmt;
            case any -> curKill < killAmt || curSpawn < createAmt;
        };
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("SpawnConfig{");
        builder.append("\n  killAmt=").append(killAmt)
            .append("\t  createAmt=").append(createAmt)
            .append("\t  spawnCnt=").append(spawnCnt)
            .append("\n  interval=").append(interval)
            .append("\t  limitation=").append(limitation)
            .append("\t  range=").append(range)
            .append("\n  firstSpawn=").append(firstSpawn)
            .append("\n}");
        return builder.toString();
    }
}
