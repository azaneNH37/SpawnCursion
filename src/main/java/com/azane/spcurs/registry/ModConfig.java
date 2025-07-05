package com.azane.spcurs.registry;

import com.azane.spcurs.SpcursMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;

public class ModConfig
{
    public static ForgeConfigSpec.BooleanValue DEBUG_SCDATA;
    public static ForgeConfigSpec.BooleanValue DEV_MODE;

    public static void register(ModLoadingContext context)
    {
        context.registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON,init(), SpcursMod.MOD_ID+"/common.toml");
    }

    private static ForgeConfigSpec init()
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Debug");

        builder.comment("Whether to output the ScSpawner DataBase on loading");
        DEBUG_SCDATA = builder.define("ScSpawnerDataOutput",false);

        builder.pop();

        builder.push("Dev");
        builder.comment("Enter DevMode to prevent in-game behaviour when constructing structures.");
        DEV_MODE = builder.define("DevMode",false);

        builder.pop();


        return builder.build();
    }
}
