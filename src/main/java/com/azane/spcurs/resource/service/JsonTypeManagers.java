package com.azane.spcurs.resource.service;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.resource.manager.JsonDataTypeManager;

public class JsonTypeManagers
{
    //此处添加需要建立的jsonTypeManager
    public static final JsonDataTypeManager modTypeManager = new JsonDataTypeManager(SpcursMod.MOD_ID);

    /**
     *
     * 在mod setup处调用
     */
    public static void loadJsonTypeManagers()
    {
        modTypeManager.initialize();
    }
}
