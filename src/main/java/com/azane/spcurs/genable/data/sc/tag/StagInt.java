package com.azane.spcurs.genable.data.sc.tag;

import com.azane.cjsop.annotation.JsonClassTypeBinder;
import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.genable.data.ISpcursPlugin;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonClassTypeBinder(fullName = "tag.int",simpleName = "tagi",namespace = SpcursMod.MOD_ID)
@Getter
public class StagInt implements ISpcursPlugin
{
    @SerializedName("key")
    private String key;
    @SerializedName("value")
    private int value;

    @Override
    public void onEntityCreate(ServerLevel level, BlockPos blockPos, LivingEntity entity)
    {
        entity.getPersistentData().putInt(key, value);
    }
}