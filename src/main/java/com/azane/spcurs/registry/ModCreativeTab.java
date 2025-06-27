package com.azane.spcurs.registry;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.item.ScMycoItem;
import com.azane.spcurs.lib.RlHelper;
import com.azane.spcurs.resource.service.ClientDataService;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTab
{
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SpcursMod.MOD_ID);
    public static RegistryObject<CreativeModeTab> SC = TABS.register("sc", () -> CreativeModeTab.builder()
        .title(Component.translatable("spcurs.tab.sc"))
        .icon(() -> ClientDataService.get().getSpawner(RlHelper.parse("spcurs:default")).buildItemStack(1))
        .displayItems((parameters, output) -> output.acceptAll(ScMycoItem.fillCreativeTab())).build());
    public static RegistryObject<CreativeModeTab> COMMON = TABS.register("common",() -> CreativeModeTab.builder()
        .title(Component.translatable("spcurs.tab.common"))
        .icon(()->new ItemStack(ModBlock.LOADER.item.get(),1))
        .displayItems((parameters, output)-> output.acceptAll(
            ModItem.ITEMS.getEntries().stream().map(RegistryObject::get)
                .filter(item -> !(item instanceof ScMycoItem))
                .map(item -> new ItemStack(item,1)).toList())).build());
}
