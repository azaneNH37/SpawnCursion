package com.azane.spcurs.registry;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.block.entity.SpcursSpawnerBlockEntity;
import com.azane.spcurs.block.entity.TransformScEntity;
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
    public static final RegistryObject<BlockEntityType<TransformScEntity>> TRANS_SC_ENTITY =
        BLOCK_ENTITIES.register("trans_sc", () ->
            BlockEntityType.Builder.of(
                TransformScEntity::new,
                ModBlock.LOADER.block.get()
            ).build(DSL.remainderType())
        );
}
