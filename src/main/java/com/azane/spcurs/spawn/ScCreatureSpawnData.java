package com.azane.spcurs.spawn;

import com.azane.spcurs.genable.data.sc.ScCreature;
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

    private long nextSpawnTick;

    public ScCreatureSpawnData(ResourceLocation scCreatureRl,ScCreature scCreature)
    {
        this.scCreatureRl = scCreatureRl;
        this.killed = 0;
        this.spawned = 0;
        this.nextSpawnTick = scCreature.getSpawnConfig().getFirstSpawn();
    }

    public boolean isReadyToSpawn(long currentTick)
    {
        return currentTick >= nextSpawnTick;
    }
    public boolean isAbleToSpawn(ScCreature creature)
    {
        return creature.getSpawnConfig().spawnAvailable(killed, spawned);
    }

    public void acceptUnitSpawn(ScCreature creature,int success)
    {
        spawned += success;
        nextSpawnTick += creature.getSpawnConfig().getInterval();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("scCreatureRl", scCreatureRl.toString());
        nbt.putInt("killed", killed);
        nbt.putInt("spawned", spawned);
        nbt.putLong("nextSpawnTick", nextSpawnTick);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        scCreatureRl = ResourceLocation.tryParse(nbt.getString("scCreatureRl"));
        if (scCreatureRl == null) {
            throw new IllegalArgumentException("Invalid ResourceLocation: " + nbt.getString("scCreatureRl"));
        }
        killed = nbt.getInt("killed");
        spawned = nbt.getInt("spawned");
        nextSpawnTick = nbt.getLong("nextSpawnTick");
    }
}
