package com.azane.spcurs.genable.data;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.resources.ResourceLocation;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
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
