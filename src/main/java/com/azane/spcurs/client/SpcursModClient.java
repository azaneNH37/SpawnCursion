package com.azane.spcurs.client;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.client.renderer.CasinoBlockEntityRenderer;
import com.azane.spcurs.client.renderer.SpcursSpawnerBlockEntityRenderer;
import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.item.ScMycoItem;
import com.azane.spcurs.registry.ModBlockEntity;
import com.azane.spcurs.registry.ModItem;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod.EventBusSubscriber(modid = SpcursMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SpcursModClient
{
    public static final Marker MARKER = MarkerManager.getMarker("SpcursModClientRegistry");

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(ModBlockEntity.SPCURS_SPAWNER_ENTITY.get(), SpcursSpawnerBlockEntityRenderer::new);
            BlockEntityRenderers.register(ModBlockEntity.CASINO_ENTITY.get(), CasinoBlockEntityRenderer::new);
        });
    }

    @SubscribeEvent
    public static void onColorRegister(RegisterColorHandlersEvent.Item event)
    {
        DebugLogger.info(MARKER,"Registering item color handlers for SpcursMod");
        event.register(((pStack, pTintIndex) -> {
            Item item = pStack.getItem();
            if(item instanceof ScMycoItem mycoItem)
            {
                return mycoItem.getColor(pStack, pTintIndex);
            }
            return 0x000000; // Default color if not a ScMycoItem
        }), ModItem.SC_MYCO_ITEM.get());
    }
}
