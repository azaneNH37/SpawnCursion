package com.azane.spcurs.resource.service;


import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.genable.data.ScGson;
import com.azane.spcurs.genable.data.sc.ScSpawner;
import com.azane.spcurs.genable.tag.ScSpawnerTag;
import com.azane.spcurs.lib.RlHelper;
import com.azane.spcurs.resource.manager.CommonDataManager;
import com.azane.spcurs.resource.manager.INetworkCacheReloadListener;
import com.azane.spcurs.resource.manager.specific.TagLikeDataManager;
import com.azane.spcurs.util.DataServiceInit;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public abstract class CommonDataService implements IResourceProvider
{
    protected Map<ResourceLocation,INetworkCacheReloadListener> listeners = new HashMap<>();

    //此处添加需要全局管理的data类
    //从json导入的data
    protected CommonDataManager<ScSpawner> spawners;
    protected TagLikeDataManager<ScSpawnerTag,ResourceLocation> scTag;

    //游戏内自存储的data

    //此处添加继承自IResourceProvider的接口实现
    public ScSpawner getSpawner(ResourceLocation id)
    {
        return spawners.getData(id);
    }
    public Set<Map.Entry<ResourceLocation, ScSpawner>> getAllSpawners()
    {
        return spawners.getAllDataEntries();
    }

    public ScSpawnerTag getSpawnerTag(ResourceLocation id)
    {
        return scTag.getData(id);
    }
    public Set<Map.Entry<ResourceLocation, ScSpawnerTag>> getAllSpawnerTags()
    {
        return scTag.getAllDataEntries();
    }


    /**
     * 理论上 每一次服务端的重新加载都会重新调用该方法
     * 但客户端只会在初始化时调用该方法
     */
    protected void reloadAndBind()
    {
       //实例化全局数据
        spawners = new CommonDataManager<>(ScSpawner.class, ScGson.INSTANCE.GSON, "spcurs/spawner","SpcursSpawner", DataServiceInit.scSpawnerInit);
        scTag = new TagLikeDataManager<>(ScSpawnerTag.class,ResourceLocation.class, GSON,"tags/spawner", "SpcursScSpawnerTag", null);

        ImmutableMap.Builder<ResourceLocation, INetworkCacheReloadListener> builder = ImmutableMap.builder();
        //注册C/S传递和reload加载
        register(spawners, "spawner", builder);
        register(scTag, "spawner_tag", builder);
        listeners = builder.build();
    }

    private <T extends INetworkCacheReloadListener> void register(T listener,String rl, ImmutableMap.Builder<ResourceLocation, INetworkCacheReloadListener> builder)
    {
        builder.put(Objects.requireNonNull(RlHelper.build(SpcursMod.MOD_ID, rl)),listener);
    }

    /**
     * 根据当前环境选择合适的缓存<br/>
     * 当前环境为单人游戏或多人游戏的服务端时，返回{@link ServerDataService}实例<br/>
     * 当前环境为多人游戏的客户端时，返回{@link ClientDataService}实例
     * @return {@link IResourceProvider} 实例
     */
    public static IResourceProvider get()
    {
        ServerDataService service = ServerDataService.getS_INSTANCE();
        return service == null ? ClientDataService.getC_INSTANCE() : service;
    }
}
