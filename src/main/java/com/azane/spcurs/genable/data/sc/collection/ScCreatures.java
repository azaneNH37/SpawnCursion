package com.azane.spcurs.genable.data.sc.collection;

import com.azane.spcurs.genable.data.sc.ScCreature;
import com.azane.spcurs.lib.IComponentDisplay;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScCreatures implements IComponentDisplay
{
    @SerializedName("set")
    @Getter
    private LinkedHashMap<ResourceLocation, ScCreature> set = new LinkedHashMap<>();

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

    @Override
    public void appendHoverText(ItemStack stack, List<Component> tooltip, TooltipFlag flag)
    {
        set.values().forEach(creature -> creature.appendHoverText(stack,tooltip,flag));
    }
}