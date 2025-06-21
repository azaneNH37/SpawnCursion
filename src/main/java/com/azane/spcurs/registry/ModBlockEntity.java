package com.azane.spcurs.registry;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.block.entity.SpcursSpawnerBlockEntity;
import com.mojang.datafixers.DSL;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntity
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SpcursMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<SpcursSpawnerBlockEntity>> SPCURS_SPAWNER_ENTITY =
        BLOCK_ENTITIES.register("spcurs_spawner", () ->
        BlockEntityType.Builder.of(
            SpcursSpawnerBlockEntity::new,
            ModBlock.SPAWNER.block.get()
        ).build(DSL.remainderType())
    );
}
