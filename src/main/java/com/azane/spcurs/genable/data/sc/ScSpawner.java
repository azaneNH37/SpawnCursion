package com.azane.spcurs.genable.data.sc;

import com.azane.spcurs.genable.data.sc.collection.ScCreatures;
import com.azane.spcurs.resource.helper.IresourceLocation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

@Getter
public class ScSpawner implements IresourceLocation
{
    @Expose(serialize = false, deserialize = false)
    @Setter
    private ResourceLocation id;

    @SerializedName("creatures")
    private ScCreatures creatures;

    @SerializedName("active_range")
    private double activeRange = 64.0D;

    @SerializedName("display")
    private ScDisplayContext displayContext = new ScDisplayContext();

    @SerializedName("loot_table")
    private ResourceLocation lootTable = ResourceLocation.parse("chests/ancient_city");

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("ScSpawner{");
        builder.append("\nid = ").append(id)
               .append(", activeRange = ").append(activeRange)
               .append("\n creatures = ").append(creatures.toString());
        builder.append('}');
        return builder.toString();
    }
}