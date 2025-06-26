package com.azane.spcurs.genable.data.sc.collection;

import com.azane.spcurs.genable.data.ISpcursPlugin;
import com.azane.spcurs.genable.data.ScGson;
import com.azane.spcurs.resource.helper.ParserHelper;
import com.azane.spcurs.resource.service.JsonTypeManagers;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;
import java.util.*;

public class ScEffects
{
    @SerializedName("set")
    private LinkedHashMap<ResourceLocation, ISpcursPlugin> set = new LinkedHashMap<>();

    public static class ScEffectSerializer implements JsonDeserializer<ScEffects>,JsonSerializer<ScEffects>
    {
        @Override
        public ScEffects deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            //DebugLogger.log("Deserializing ScEffects: " + json);
            JsonObject jsonObject = json.getAsJsonObject();

            ScEffects scEffects = new ScEffects();
            Optional.ofNullable(jsonObject.getAsJsonObject("set"))
                .map(JsonObject::asMap).ifPresent(map->map.forEach(
                    (rl,element)->{
                        ResourceLocation resourceLocation = ResourceLocation.tryParse(rl);
                        //DebugLogger.log(rl);
                        if (resourceLocation != null && element.isJsonObject())
                        {
                            scEffects.set.put(resourceLocation,
                                ParserHelper.parseJsonDynamic(
                                    ScGson.INSTANCE.GSON,
                                    element,
                                    ISpcursPlugin.class,
                                    resourceLocation,
                                    JsonTypeManagers.modTypeManager
                                    ));
                        }
                    }
                ));
            return scEffects;
        }

        @Override
        public JsonElement serialize(ScEffects src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject jsonObject = new JsonObject();
            JsonObject setObject = new JsonObject();
            src.set.forEach((resourceLocation, plugin) -> {
                setObject.add(resourceLocation.toString(), context.serialize(plugin));
            });
            jsonObject.add("set", setObject);
            return jsonObject;
        }
    }

    public Set<Map.Entry<ResourceLocation, ISpcursPlugin>> entrySet()
    {
        return set.entrySet();
    }

    public static class Builder
    {
        private final ScEffects scEffects = new ScEffects();

        public Builder add(String resourceLocation, ISpcursPlugin plugin)
        {
            scEffects.set.put(ResourceLocation.parse(resourceLocation), plugin);
            return this;
        }

        public ScEffects build()
        {
            return scEffects;
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("ScEffects{");
        for (Map.Entry<ResourceLocation, ISpcursPlugin> entry : set.entrySet())
            builder.append("\n  ").append(entry.getKey()).append(": ").append(entry.getValue().toString());
        return builder+"\n}";
    }
}