package com.azane.spcurs.lib;

import com.azane.spcurs.SpcursMod;
import net.minecraft.resources.ResourceLocation;

/**
 * What the holy f**king shit does Forge want f**king to do in single version 1.20.1 ????
 */
public final class RlHelper
{
    public static final ResourceLocation EMPTY = RlHelper.build(SpcursMod.MOD_ID,"null");

    public static ResourceLocation parse(String rl)
    {
        return ResourceLocation.tryParse(rl);
    }

    public static ResourceLocation build(String namespace,String path)
    {
        return ResourceLocation.tryBuild(namespace,path);
    }
}
