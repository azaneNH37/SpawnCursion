package com.azane.spcurs.resource.service;

import com.azane.spcurs.genable.data.sc.ScSpawner;
import com.azane.spcurs.genable.tag.ScSpawnerTag;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Set;

public interface IResourceProvider
{
    Gson GSON = new GsonBuilder()
        .registerTypeAdapter(ResourceLocation.class,new ResourceLocation.Serializer())
        .create();


    //此处添加全局访问数据的接口
    ScSpawner getSpawner(ResourceLocation id);
    Set<Map.Entry<ResourceLocation, ScSpawner>> getAllSpawners();

    ScSpawnerTag getSpawnerTag(ResourceLocation id);
    Set<Map.Entry<ResourceLocation, ScSpawnerTag>> getAllSpawnerTags();
}
