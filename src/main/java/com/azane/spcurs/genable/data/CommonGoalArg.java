package com.azane.spcurs.genable.data;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

@Getter
public class CommonGoalArg
{
    @SerializedName("speed")
    double speedModifier = 1.0D;
    @SerializedName("range")
    double range = 16.0D;
    @SerializedName("entity_type")
    ResourceLocation entityType;
}
