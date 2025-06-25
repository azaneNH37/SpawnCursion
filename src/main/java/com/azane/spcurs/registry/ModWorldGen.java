package com.azane.spcurs.registry;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.world.feature.ScSpawnerFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModWorldGen
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SpcursMod.MOD_ID);

    public static final RegistryObject<ScSpawnerFeature> SC_SPAWNER_FEATURE = FEATURES.register("sc_spawner", ScSpawnerFeature::new);
}
