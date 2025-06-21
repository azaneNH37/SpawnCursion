package com.azane.spcurs.registry;

import com.azane.spcurs.SpcursMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItem
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpcursMod.MOD_ID);

    public static RegistryObject<Item> register(String name, Supplier<? extends Item> supplier)
    {
        return ITEMS.register(name,supplier);
    }
}
