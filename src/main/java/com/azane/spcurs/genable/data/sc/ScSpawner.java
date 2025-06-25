package com.azane.spcurs.genable.data.sc;

import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.genable.data.sc.collection.ScCreatures;
import com.azane.spcurs.genable.item.base.IGenItem;
import com.azane.spcurs.genable.item.base.IGenItemDatabase;
import com.azane.spcurs.genable.item.base.IPolyItemDataBase;
import com.azane.spcurs.registry.ModItem;
import com.azane.spcurs.resource.helper.IresourceLocation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Getter
public class ScSpawner implements IGenItemDatabase
{
    @Expose(serialize = false, deserialize = false)
    @Setter
    private ResourceLocation id;

    @SerializedName("creatures")
    private ScCreatures creatures = new ScCreatures();

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

    @Override
    public void registerDataBase()
    {
        Item item = ModItem.SC_MYCO_ITEM.get();
        if(item instanceof IPolyItemDataBase<?> polyItem)
        {
            polyItem.castToType(ScSpawner.class).registerDataBase(this);
        }
    }

    @Override
    public ItemStack buildItemStack(int count)
    {
        Item item = ModItem.SC_MYCO_ITEM.get();
        if(item instanceof IGenItem genItem)
        {
            return genItem.templateBuildItemStack(buildTag(),1);
        }
        DebugLogger.error("The item %s is not an instance of IGenItem, cannot build item stack.".formatted(item.getDescriptionId()));
        return null;
    }
}