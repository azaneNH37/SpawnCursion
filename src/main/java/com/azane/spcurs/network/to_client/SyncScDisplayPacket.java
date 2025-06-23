package com.azane.spcurs.network.to_client;

import com.azane.spcurs.block.entity.SpcursSpawnerBlockEntity;
import com.azane.spcurs.network.IOgnmPacket;
import com.azane.spcurs.spawn.SpcursDisplay;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

@Getter
public class SyncScDisplayPacket implements IOgnmPacket
{
    private long blockPos;
    private CompoundTag displayData;

    public SyncScDisplayPacket(long blockPos, CompoundTag displayData)
    {
        this.blockPos = blockPos;
        this.displayData = displayData;
    }

    public SyncScDisplayPacket(FriendlyByteBuf buffer)
    {
        this.blockPos = buffer.readLong();
        this.displayData = buffer.readNbt();
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeLong(blockPos);
        buffer.writeNbt(displayData);
    }

    @Override
    public void handle(NetworkEvent.Context context)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.level != null)
        {
            BlockEntity entity = mc.level.getBlockEntity(BlockPos.of(blockPos));
            if(entity instanceof SpcursSpawnerBlockEntity sc)
            {
                sc.getDisplay().deserializeNBT(displayData);
            }
        }
    }
}
