package com.azane.spcurs.network.to_client;

import com.azane.spcurs.network.IOgnmPacket;
import com.azane.spcurs.resource.service.ClientDataService;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;

@Getter
public class SyncGlobalDatapackPacket implements IOgnmPacket
{
    private final Map<ResourceLocation, Map<ResourceLocation, String>> cache;
    /**
     * Packet constructor for decoding data from a buffer.
     *
     * @param buf The buffer containing the packet data.
     */

    public SyncGlobalDatapackPacket(FriendlyByteBuf buf)
    {
        cache = buf.readMap(FriendlyByteBuf::readResourceLocation, buf2 -> buf2.readMap(FriendlyByteBuf::readResourceLocation, FriendlyByteBuf::readUtf));
    }

    public SyncGlobalDatapackPacket(Map<ResourceLocation, Map<ResourceLocation, String>> cache)
    {
        this.cache = cache;
    }


    @Override
    public void handle(NetworkEvent.Context context)
    {
        ClientDataService.fromNetwork(getCache());
    }

    @Override
    public void encode(FriendlyByteBuf buf)
    {
        buf.writeMap(getCache(), FriendlyByteBuf::writeResourceLocation, (buf1, map) -> {
            buf1.writeMap(map, FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::writeUtf);
        });
    }
}

