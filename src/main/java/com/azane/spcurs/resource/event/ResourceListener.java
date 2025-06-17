package com.azane.spcurs.resource.event;


import com.azane.spcurs.network.OgnmChannel;
import com.azane.spcurs.network.to_client.SyncGlobalDatapackPacket;
import com.azane.spcurs.resource.service.ServerDataService;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ResourceListener
{
    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event)
    {
        ServerDataService.onDataPackReload(event::addListener);
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event)
    {
        ServerDataService.onServerStop();
    }

    @SubscribeEvent
    public static void OnDatapackSync(OnDatapackSyncEvent event)
    {
        ServerDataService.getNetworkCacheOptional().ifPresent(
            networkCache -> {
                if (networkCache.isEmpty())return;
                SyncGlobalDatapackPacket msg = new SyncGlobalDatapackPacket(networkCache);
                if(event.getPlayer() != null) {
                    OgnmChannel.DEFAULT.sendTo(msg,event.getPlayer());
                } else {
                    OgnmChannel.DEFAULT.sendListTo(msg,event.getPlayers());
                }
            }
        );
    }
}
