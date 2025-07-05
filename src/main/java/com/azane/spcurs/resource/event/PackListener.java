package com.azane.spcurs.resource.event;

import com.azane.spcurs.resource.pack.CustomRepoSource;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PackListener
{
    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event)
    {
        if(event.getPackType() == PackType.SERVER_DATA)
        {
            CustomRepoSource.INSTANCE.packType = PackType.SERVER_DATA;
            event.addRepositorySource(CustomRepoSource.INSTANCE);
        }
    }
}
