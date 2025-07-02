package com.azane.spcurs.genable.data.sc;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
public class ScChildConfig
{
    @SerializedName("id")
    private ResourceLocation id;
    @SerializedName("delay")
    private long delay = 20;
    @SerializedName("amt")
    private int amt = 1;
    @SerializedName("offset")
    @Setter
    public double[] offset = new double[] {0, 0, 0};
    @SerializedName("range")
    private double range = 5.0;
}