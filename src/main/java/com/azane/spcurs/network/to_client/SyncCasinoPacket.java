package com.azane.spcurs.network.to_client;

import com.azane.spcurs.block.entity.CasinoBlockEntity;
import com.azane.spcurs.block.entity.SpcursSpawnerBlockEntity;
import com.azane.spcurs.network.IOgnmPacket;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

@Getter
public class SyncCasinoPacket implements IOgnmPacket
{
    private long blockPos;
    private CompoundTag inventoryData;

    public SyncCasinoPacket(long blockPos, CompoundTag displayData)
    {
        this.blockPos = blockPos;
        this.inventoryData = displayData;
    }

    public SyncCasinoPacket(FriendlyByteBuf buffer)
    {
        this.blockPos = buffer.readLong();
        this.inventoryData = buffer.readNbt();
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeLong(blockPos);
        buffer.writeNbt(inventoryData);
    }

    @Override
    public void handle(NetworkEvent.Context context)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.level != null)
        {
            BlockEntity entity = mc.level.getBlockEntity(BlockPos.of(blockPos));
            if(entity instanceof CasinoBlockEntity casino)
            {
                casino.load(inventoryData);
            }
        }
    }
}
