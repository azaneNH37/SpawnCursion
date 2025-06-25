package com.azane.spcurs.registry;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.item.ScMycoItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTab
{
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SpcursMod.MOD_ID);
    public static RegistryObject<CreativeModeTab> SC = TABS.register("sc", () -> CreativeModeTab.builder()
        .title(Component.translatable("spcurs.tab.sc"))
        .icon(() -> new ItemStack(Items.ENCHANTED_BOOK))
        .displayItems((parameters, output) -> output.acceptAll(ScMycoItem.fillCreativeTab())).build());
}
