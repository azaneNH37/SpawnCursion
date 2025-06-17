package com.azane.spcurs.genable.item.base;

import com.azane.spcurs.resource.helper.IresourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IGenItemDatabase extends IresourceLocation
{
    void registerDataBase();

    ItemStack buildItemStack(int count);

    default CompoundTag buildTag()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putString(IresourceLocation.TAG_RL, getId().toString());
        return nbt;
    }
}