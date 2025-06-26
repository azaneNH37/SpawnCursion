package com.azane.spcurs;

import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.network.OgnmChannel;
import com.azane.spcurs.registry.*;
import com.azane.spcurs.resource.service.JsonTypeManagers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SpcursMod.MOD_ID)
public class SpcursMod
{
    public static final String MOD_ID = "spcurs";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public SpcursMod()
    {
        this(FMLJavaModLoadingContext.get());
    }

    public SpcursMod(FMLJavaModLoadingContext context)
    {
        DebugLogger.init();

        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ModBlock.BLOCKS.register(modEventBus);
        ModItem.ITEMS.register(modEventBus);
        ModBlockEntity.BLOCK_ENTITIES.register(modEventBus);
        ModWorldGen.FEATURES.register(modEventBus);
        ModCreativeTab.TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        //context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(()-> {
            JsonTypeManagers.loadJsonTypeManagers();
            OgnmChannel.DEFAULT.initialize();
        });
    }
}
