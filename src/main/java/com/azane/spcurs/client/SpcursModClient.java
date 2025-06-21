package com.azane.spcurs.client;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.client.renderer.SpcursSpawnerBlockEntityRenderer;
import com.azane.spcurs.registry.ModBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SpcursMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SpcursModClient
{
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(ModBlockEntity.SPCURS_SPAWNER_ENTITY.get(), SpcursSpawnerBlockEntityRenderer::new);
        });
    }
}
