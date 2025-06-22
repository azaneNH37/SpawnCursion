package com.azane.spcurs.spawn;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.genable.tag.ScSpawnerTag;
import com.azane.spcurs.resource.service.CommonDataService;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ScTags
{
    public static final ResourceLocation FEATURE_SC_I = ResourceLocation.fromNamespaceAndPath(SpcursMod.MOD_ID,"fsc1");

    @Nullable
    public static ScSpawnerTag getSpawnerTag(ResourceLocation id)
    {
        return CommonDataService.get().getSpawnerTag(id);
    }
}
