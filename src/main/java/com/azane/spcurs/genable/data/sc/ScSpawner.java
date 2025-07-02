package com.azane.spcurs.genable.data.sc;

import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.genable.data.sc.collection.ScChildren;
import com.azane.spcurs.genable.data.sc.collection.ScCreatures;
import com.azane.spcurs.genable.data.sc.collection.ScEffects;
import com.azane.spcurs.genable.item.base.IGenItem;
import com.azane.spcurs.genable.item.base.IGenItemDatabase;
import com.azane.spcurs.genable.item.base.IPolyItemDataBase;
import com.azane.spcurs.lib.IComponentDisplay;
import com.azane.spcurs.lib.RlHelper;
import com.azane.spcurs.registry.ModItem;
import com.azane.spcurs.util.ComponentUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

@Getter
public class ScSpawner implements IGenItemDatabase, IComponentDisplay
{
    @Expose(serialize = false, deserialize = false)
    @Setter
    private ResourceLocation id;

    @SerializedName("creatures")
    private ScCreatures creatures = new ScCreatures();

    @SerializedName("global_effects")
    private ScEffects globalEffects = new ScEffects();

    @SerializedName("active_range")
    private double activeRange = 64.0D;

    @SerializedName("display")
    private ScDisplayContext displayContext = new ScDisplayContext();

    @SerializedName("loot_table")
    private ResourceLocation lootTable = RlHelper.parse("chests/ancient_city");

    @SerializedName("children")
    private ScChildren scChildren = new ScChildren();

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

    public void appendHoverText(ItemStack stack, List<Component> tooltip, TooltipFlag flag)
    {
        tooltip.add(Component.empty());
        tooltip.add(Component.literal(displayContext.getName()).withStyle(ChatFormatting.BOLD,ComponentUtil.getRarityColor(displayContext.getRarity())));
        tooltip.add(Component.literal("★".repeat(displayContext.getRarity())).withStyle(ComponentUtil.getRarityColor(displayContext.getRarity())));
        tooltip.add(Component.empty());
        creatures.appendHoverText(stack,tooltip,flag);
        tooltip.add(Component.translatable("spcurs.sc.loot_table", lootTable.toString()).withStyle(ChatFormatting.GRAY));
        scChildren.appendHoverText(stack,tooltip,flag);
    }
}