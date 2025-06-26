package com.azane.spcurs.util;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.genable.tag.ScSpawnerTag;
import com.azane.spcurs.lib.RlHelper;
import com.azane.spcurs.resource.service.CommonDataService;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ScTags
{
    public static final ResourceLocation DEFAULT_SC = RlHelper.build(SpcursMod.MOD_ID,"default");

    public static final ResourceLocation SC_LV1 = RlHelper.build(SpcursMod.MOD_ID,"sc_lv1");
    public static final ResourceLocation SC_LV2 = RlHelper.build(SpcursMod.MOD_ID,"sc_lv2");
    public static final ResourceLocation SC_LV3 = RlHelper.build(SpcursMod.MOD_ID,"sc_lv3");
    public static final ResourceLocation SC_UNDER = RlHelper.build(SpcursMod.MOD_ID,"sc_under");
    public static final ResourceLocation SC_DESERT = RlHelper.build(SpcursMod.MOD_ID,"sc_desert");

    @Nullable
    public static ScSpawnerTag getSpawnerTag(ResourceLocation id)
    {
        return CommonDataService.get().getSpawnerTag(id);
    }
}
