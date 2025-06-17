package com.azane.spcurs.resource.service;


import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.resource.manager.INetworkCacheReloadListener;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public abstract class CommonDataService implements IResourceProvider
{
    protected Map<ResourceLocation,INetworkCacheReloadListener> listeners = new HashMap<>();

    //此处添加需要全局管理的data类
    //从json导入的data

    //游戏内自存储的data

    //此处添加继承自IResourceProvider的接口实现




    /**
     * 理论上 每一次服务端的重新加载都会重新调用该方法
     * 但客户端只会在初始化时调用该方法
     */
    protected void reloadAndBind()
    {
       //实例化全局数据


        ImmutableMap.Builder<ResourceLocation, INetworkCacheReloadListener> builder = ImmutableMap.builder();
        //注册C/S传递和reload加载

        listeners = builder.build();
    }

    private <T extends INetworkCacheReloadListener> void register(T listener,String rl, ImmutableMap.Builder<ResourceLocation, INetworkCacheReloadListener> builder)
    {
        builder.put(Objects.requireNonNull(ResourceLocation.tryBuild(SpcursMod.MOD_ID, rl)),listener);
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
