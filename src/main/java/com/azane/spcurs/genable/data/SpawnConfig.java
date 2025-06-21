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
