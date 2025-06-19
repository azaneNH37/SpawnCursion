package com.azane.spcurs.genable.data;

import com.azane.spcurs.genable.data.sc.collection.ScEffects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;

public enum ScGson
{
    INSTANCE;

    public final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(ResourceLocation.class,new ResourceLocation.Serializer())
        .registerTypeAdapter(ScEffects.class, new ScEffects.ScEffectDeserializer())
        .create();
}
