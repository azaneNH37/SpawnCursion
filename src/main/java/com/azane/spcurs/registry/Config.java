package com.azane.spcurs.registry;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config
{
    public static ForgeConfigSpec.BooleanValue DEBUG_SCDATA;
    public static ForgeConfigSpec init()
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Debug");

        builder.comment("Whether to output the ScSpawner DataBase on loading");
        DEBUG_SCDATA = builder.define("ScSpawnerDataOutput",false);

        builder.pop();


        return builder.build();
    }
}
