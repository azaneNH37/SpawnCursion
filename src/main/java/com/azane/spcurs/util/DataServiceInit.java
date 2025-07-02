package com.azane.spcurs.util;

import com.azane.spcurs.genable.data.sc.ScSpawner;
import com.azane.spcurs.registry.Config;
import com.azane.spcurs.resource.manager.JsonDataManager;

import java.util.function.Consumer;

public class DataServiceInit
{
    public static Consumer<JsonDataManager<ScSpawner>> scSpawnerInit = jm -> {
        jm.getAllData().forEach((id, spawner) -> {
            spawner.registerDataBase();
            spawner.getScChildren().init();
            spawner.getCreatures().getSet().forEach((rid, creature) -> creature.setId(rid));
            int raw_color = spawner.getDisplayContext().getEntityColor();
            raw_color &= 0xFFFFFFFF;
            spawner.getDisplayContext().setEntityColor((raw_color & 0xFF000000) == 0 ? (raw_color | 0xFF000000) : raw_color);
        });
        if(Config.DEBUG_SCDATA.get())
            jm.debugLogAllData();
    };
}