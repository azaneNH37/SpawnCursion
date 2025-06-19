package com.azane.spcurs.genable.data.sc.collection;

import com.azane.spcurs.genable.data.sc.ScCreature;
import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Set;

public class ScCreatures
{
    @SerializedName("set")
    private Map<ResourceLocation, ScCreature> set;

    public Set<Map.Entry<ResourceLocation, ScCreature>> entrySet()
    {
        return set.entrySet();
    }
    public ScCreature get(ResourceLocation rl)
    {
        return set.get(rl);
    }
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("ScCreatures{\n");
        for (Map.Entry<ResourceLocation, ScCreature> entry : set.entrySet())
        {
            builder.append(entry.getKey()).append(" = ").append(entry.getValue().toString()).append("\n");
        }
        builder.append('}');
        return builder.toString();
    }
}