package com.azane.spcurs.resource.manager.specific;

import com.azane.spcurs.resource.helper.ITagLike;
import com.azane.spcurs.resource.helper.ExtractHelper;
import com.azane.spcurs.resource.helper.IresourceLocation;
import com.azane.spcurs.resource.manager.DynamicDataManager;
import com.azane.spcurs.resource.manager.JsonDataManager;
import com.azane.spcurs.resource.manager.JsonDataTypeManager;
import com.google.gson.Gson;
import lombok.Getter;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class TagLikeDataManager<U,T extends ITagLike<U>> extends DynamicDataManager<T>
{
    @Getter
    private final Class<U> tagType;

    public TagLikeDataManager(Class<T> dataClass, Class<U> tagType, Gson pGson, FileToIdConverter directory, String marker, JsonDataTypeManager manager, Consumer<JsonDataManager<T>> onDataMapInit)
    {
        super(dataClass, pGson, directory, marker, manager, onDataMapInit);
        this.tagType = tagType;
    }

    public TagLikeDataManager(Class<T> dataClass,Class<U> tagType,Gson pGson, String directory, String marker, JsonDataTypeManager manager, Consumer<JsonDataManager<T>> onDataMapInit)
    {
        this(dataClass, tagType, pGson, FileToIdConverter.json(directory), marker, manager, onDataMapInit);
    }

    @Override
    protected void generateUnitData(ResourceLocation id, T data)
    {
        if (data != null) {
            if(dataMap.containsKey(id))
            {
                T oldData = dataMap.get(id);
                oldData.castToType(tagType).absorb(data.castToType(tagType));
                return;
            }
            if(data instanceof IresourceLocation rlData)
                rlData.setId(ExtractHelper.extractPureId(id));
            dataMap.put(id, data);
        }
    }
}
