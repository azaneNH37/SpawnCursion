package com.azane.spcurs.genable.data.sc.effects;

import com.azane.cjsop.annotation.JsonClassTypeBinder;
import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.genable.data.ISpcursPlugin;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonClassTypeBinder(fullName = "efc.effect", simpleName = "effc", namespace = SpcursMod.MOD_ID)
public class EfcEffectAdder implements ISpcursPlugin
{
    @SerializedName("effectID")
    private ResourceLocation rl;
    @SerializedName("level")
    private int effect_level = -10;
    @SerializedName("duration")
    private int duration = -1;
    @SerializedName("visible")
    private boolean visible = false;

    public void onEntityCreate(ServerLevel level, BlockPos blockPos, LivingEntity entity)
    {
        MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(rl);
        if(effect == null)
        {
            DebugLogger.error("EfcEffectAdder: Effect %s not found, cannot apply to entity %s".formatted(rl, entity.getStringUUID()));
            return;
        }
        entity.addEffect(new MobEffectInstance(effect,duration,effect_level-1,false,visible,false));
    }
    @Override
    public String toString()
    {
        return "EfcEffectAdder{effectID: %s, level: %d, duration: %d, visible: %b}".formatted(rl, effect_level, duration, visible);
    }
}
