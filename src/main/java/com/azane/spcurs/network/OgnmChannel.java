package com.azane.spcurs.network;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.network.to_client.SyncCasinoPacket;
import com.azane.spcurs.network.to_client.SyncGlobalDatapackPacket;
import com.azane.spcurs.network.to_client.SyncScDisplayPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.simple.SimpleChannel;

public class OgnmChannel extends BasePacketHandler
{
    public static final OgnmChannel DEFAULT = new OgnmChannel();

    private OgnmChannel(){}

    private final SimpleChannel CHANNEL = createChannel(ResourceLocation.tryBuild(SpcursMod.MOD_ID,"main"),"1.0");

    public SimpleChannel getChannel()
    {
        return this.CHANNEL;
    }

    public void initialize()
    {
        registerServerToClient(SyncGlobalDatapackPacket.class,SyncGlobalDatapackPacket::new);
        registerServerToClient(SyncScDisplayPacket.class, SyncScDisplayPacket::new);
        registerServerToClient(SyncCasinoPacket.class,SyncCasinoPacket::new);
    }
}
