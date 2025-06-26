package com.azane.spcurs.spawn;

import com.azane.spcurs.genable.data.SpawnConfig;
import com.azane.spcurs.genable.data.sc.ScCreature;
import com.azane.spcurs.lib.RlHelper;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

@Getter
public class ScCreatureSpawnData implements INBTSerializable<CompoundTag>
{
    private ResourceLocation scCreatureRl;

    private int killed;
    private int spawned;
    private int existing;

    private long nextSpawnTick;

    public ScCreatureSpawnData(ResourceLocation scCreatureRl,ScCreature scCreature)
    {
        this.scCreatureRl = scCreatureRl;
        this.killed = 0;
        this.spawned = 0;
        this.existing = 0;
        this.nextSpawnTick = scCreature.getSpawnConfig().getFirstSpawn();
    }

    public boolean isReadyToSpawn(long currentTick)
    {
        return currentTick >= nextSpawnTick;
    }
    public boolean isAbleToSpawn(ScCreature creature)
    {
        return creature.getSpawnConfig().spawnAvailable(killed, spawned,existing);
    }
    public boolean isSpawnFinished(ScCreature creature)
    {
        boolean finished = creature.getSpawnConfig().spawnFinished(killed, spawned,existing);
        if(finished)
            nextSpawnTick = Long.MAX_VALUE; // Prevent further spawns
        return finished;
    }

    public void onBlockEntityServerLoad(SpcursEntity spcursEntity,ScCreature creature)
    {

    }
    public void acceptUnitSpawn(ScCreature creature,int success)
    {
        spawned += success;
        existing += success;
        nextSpawnTick += creature.getSpawnConfig().getInterval();
    }
    public void acceptUnitKilled(SpcursEntity entity,ScCreature creature,boolean shouldCount)
    {
        existing--;
        if(shouldCount)
        {
            killed++;
            return;
        }
        spawned--;
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("scCreatureRl", scCreatureRl.toString());
        nbt.putInt("killed", killed);
        nbt.putInt("spawned", spawned);
        nbt.putInt("existing", existing);
        nbt.putLong("nextSpawnTick", nextSpawnTick);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        scCreatureRl = RlHelper.parse(nbt.getString("scCreatureRl"));
        if (scCreatureRl == null) {
            throw new IllegalArgumentException("Invalid ResourceLocation: " + nbt.getString("scCreatureRl"));
        }
        killed = nbt.getInt("killed");
        spawned = nbt.getInt("spawned");
        existing = nbt.getInt("existing");
        nextSpawnTick = nbt.getLong("nextSpawnTick");
    }

    @Override
    public String toString()
    {
        return String.format(
            "ScCreatureSpawnData{scCreatureRl=%s, killed=%d,existing=%d,spawned=%d, nextSpawnTick=%d}",
            scCreatureRl, killed, existing,spawned, nextSpawnTick
        );
    }
}
