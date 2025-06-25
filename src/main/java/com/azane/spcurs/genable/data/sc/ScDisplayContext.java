package com.azane.spcurs.genable.data.sc;

import com.azane.spcurs.SpcursMod;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

@Getter
public class ScDisplayContext
{
    @SerializedName("img")
    private ResourceLocation imgRl = ResourceLocation.tryBuild(SpcursMod.MOD_ID,"spawner/default");
    @SerializedName(value = "render_color", alternate = "entity_color")
    @Setter
    private int entityColor = 0xFFFFFFFF;
    @SerializedName("line_color")
    private int itemLineColor = 0xFFFFFFFF;
    @SerializedName("head_color")
    private int itemHeadColor = 0xFFFF0000;
    @SerializedName("leg_color")
    private int itemLegColor = 0xFFEEE6C8;

    @SerializedName("name")
    private String name = "spcurs.sc.name.default";

    public int getItemColor(int tintIndex)
    {
        return switch (tintIndex) {
            case 0 -> itemLineColor;
            case 1 -> itemHeadColor;
            case 2 -> itemLegColor;
            default -> entityColor;
        };
    }
}
