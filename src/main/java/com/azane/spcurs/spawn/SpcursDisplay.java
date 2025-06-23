package com.azane.spcurs.spawn;

import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

@Setter
public class SpcursDisplay implements INBTSerializable<CompoundTag>
{
    private int expectedSpawnAmt;
    private int killed;
    private int spawned;

    public float getSpawnProgress()
    {
        if(expectedSpawnAmt <= 0)
            return 0.0f;
        return (float) spawned / expectedSpawnAmt;
    }
    public float getKillProgress()
    {
        if(expectedSpawnAmt <= 0)
            return 0.0f;
        return (float) killed / expectedSpawnAmt;
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("expectedSpawnAmt", expectedSpawnAmt);
        nbt.putInt("killed", killed);
        nbt.putInt("spawned", spawned);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        expectedSpawnAmt = nbt.getInt("expectedSpawnAmt");
        killed = nbt.getInt("killed");
        spawned = nbt.getInt("spawned");
    }
}
