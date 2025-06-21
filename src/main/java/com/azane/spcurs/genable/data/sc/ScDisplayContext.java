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
    @SerializedName("render_color")
    @Setter
    private int renderColor = 0xFFFFFFFF;
}
