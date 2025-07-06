package com.azane.spcurs.registry;

import com.azane.spcurs.SpcursMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;

public class ModConfig
{
    public static ForgeConfigSpec.BooleanValue DEBUG_SCDATA;
    public static ForgeConfigSpec.BooleanValue DEV_MODE;
    public static ForgeConfigSpec.IntValue GAME_CASINO_RANGE;
    public static ForgeConfigSpec.IntValue GAME_CASINO_SPECTOR;
    public static ForgeConfigSpec.BooleanValue SC_CONSTANT_ACTIVE;

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

        builder.push("Game");
        builder.comment("The distance for the spawner of casino.");
        GAME_CASINO_RANGE = builder.defineInRange("CasinoScDistance",16,16,64);
        builder.comment("The milliseconds for player to enter spectator mode after the casino is activated. Set to 0 to disable.");
        GAME_CASINO_SPECTOR = builder.defineInRange("CasinoSpectatorDelay",0,0,10000);
        builder.comment("Whether to keep the ScSpawner active once it is loaded. Could lead to performance issues if too many ScSpawners are loaded.");
        SC_CONSTANT_ACTIVE = builder.define("ScSpawnerConstActive",false);
        builder.pop();


        return builder.build();
    }
}
