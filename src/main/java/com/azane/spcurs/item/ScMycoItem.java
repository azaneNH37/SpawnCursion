package com.azane.spcurs.item;

import com.azane.spcurs.genable.data.sc.ScSpawner;
import com.azane.spcurs.genable.item.base.IGenItem;
import com.azane.spcurs.genable.item.base.IPolyItemDataBase;
import com.azane.spcurs.resource.service.ServerDataService;
import com.azane.spcurs.util.ScTags;
import lombok.Getter;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScMycoItem extends Item implements IPolyItemDataBase<ScSpawner>, IGenItem
{
    @Getter
    private final Class<ScSpawner> dataBaseType = ScSpawner.class;
    @Getter
    private final Map<ResourceLocation, ScSpawner> databaseCache = new ConcurrentHashMap<>();

    public ScMycoItem() { super(new Item.Properties());}

    @Override
    @SuppressWarnings("unchecked")
    public ScMycoItem getItem(){return this;}

    @Override
    public boolean isDataBaseForStack(ItemStack itemStack) { return isThisGenItem(itemStack);}

    public int getColor(ItemStack stack, int tintIndex)
    {
        if(!isDataBaseForStack(stack))
            return 0x000000;
        ScSpawner dataBase = getDataBaseForStack(stack);
        if (dataBase != null) {
            return dataBase.getDisplayContext().getItemColor(tintIndex);
        }
        return 0x000000; // Default color if no database found
    }

    public ResourceLocation getScSpawnerID(ItemStack itemStack) {
        if (!isDataBaseForStack(itemStack)) {
            return ScTags.DEFAULT_SC;
        }
        ScSpawner dataBase = getDataBaseForStack(itemStack);
        if (dataBase != null) {
            return dataBase.getId();
        }
        return ScTags.DEFAULT_SC;
    }

    public static NonNullList<ItemStack> fillCreativeTab() {
        NonNullList<ItemStack> stacks = NonNullList.create();
        ServerDataService.get().getAllSpawners().stream().sorted((r, i)->r.getKey().getPath().hashCode()).forEach(entry->{
            stacks.add(entry.getValue().buildItemStack(1));
        });
        return stacks;
    }

    @Override
    public String getDescriptionId(ItemStack pStack)
    {
        ScSpawner dataBase = getDataBaseForStack(pStack);
        if (dataBase == null)
            return "item.spcurs.scmyco.fail";
        return "item.spcurs.scmyco";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
    {
        if(!isDataBaseForStack(pStack))
        {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            pTooltipComponents.add(Component.literal("No database found for this item."));
            return;
        }
        ScSpawner dataBase = getDataBaseForStack(pStack);
        if (dataBase == null) {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            pTooltipComponents.add(Component.literal("No database found for this item."));
            return;
        }
        dataBase.appendHoverText(pStack, pTooltipComponents, pIsAdvanced);
    }
}
