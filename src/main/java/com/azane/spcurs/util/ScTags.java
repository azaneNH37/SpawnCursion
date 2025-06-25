package com.azane.spcurs.util;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.genable.tag.ScSpawnerTag;
import com.azane.spcurs.resource.service.CommonDataService;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ScTags
{
    public static final ResourceLocation SC_LV1 = ResourceLocation.fromNamespaceAndPath(SpcursMod.MOD_ID,"sc_lv1");
    public static final ResourceLocation SC_LV2 = ResourceLocation.fromNamespaceAndPath(SpcursMod.MOD_ID,"sc_lv2");
    public static final ResourceLocation SC_LV3 = ResourceLocation.fromNamespaceAndPath(SpcursMod.MOD_ID,"sc_lv3");
    public static final ResourceLocation SC_UNDER = ResourceLocation.fromNamespaceAndPath(SpcursMod.MOD_ID,"sc_under");
    public static final ResourceLocation SC_DESERT = ResourceLocation.fromNamespaceAndPath(SpcursMod.MOD_ID,"sc_desert");

    @Nullable
    public static ScSpawnerTag getSpawnerTag(ResourceLocation id)
    {
        return CommonDataService.get().getSpawnerTag(id);
    }
}
