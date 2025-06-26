package com.azane.spcurs.genable.data.sc.goal;

import com.azane.cjsop.annotation.JsonClassTypeBinder;
import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.genable.data.ISpcursPlugin;
import com.azane.spcurs.util.RegistryCaster;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

@JsonClassTypeBinder(fullName = "goal.target.add", simpleName = "taradd", namespace = SpcursMod.MOD_ID)
public class GoalTargetAdd implements ISpcursPlugin,IPersistantGoal
{
    @Getter
    @Expose(serialize = false,deserialize = false)
    public final ResourceLocation goalType = ResourceLocation.fromNamespaceAndPath(SpcursMod.MOD_ID, "goal.target.add");

    @SerializedName("priority")
    private int priority;

    @SerializedName("target")
    private ResourceLocation targetType;
    @SerializedName("rand_interval")
    private int randInterval = 10;
    @SerializedName("must_see")
    private boolean mustSee;
    @SerializedName("must_reach")
    private boolean mustReach = false;

    @Override
    public void onEntityCreate(ServerLevel level, BlockPos blockPos, LivingEntity entity)
    {
        applyGoalToEntity(level, blockPos, entity, false);
    }

    @Override
    public void applyGoalToEntity(ServerLevel level, BlockPos blockPos, LivingEntity entity, boolean isRecreate)
    {
        if(entity instanceof Mob mob)
        {
            if(targetType == null || targetType.toString().isEmpty())
            {
                DebugLogger.warn("Target type is not specified for EfcTargetAdd effect on entity: " + entity.getType());
                return;
            }
            Class<? extends Entity> targetClass = RegistryCaster.getEntityClass(targetType)
                .orElseThrow(() -> new IllegalArgumentException("Invalid target type: " + targetType));
            if(!LivingEntity.class.isAssignableFrom(targetClass))
            {
                DebugLogger.warn("Target type is not a LivingEntity for EfcTargetAdd effect on entity: " + entity.getType());
                return;
            }
            Class<? extends LivingEntity> targetClassCast = targetClass.asSubclass(LivingEntity.class);
            mob.targetSelector.addGoal(priority, new NearestAttackableTargetGoal<>(mob, targetClassCast, randInterval, mustSee, mustReach, null));

            if(!isRecreate)
            {
                GoalPersistantHelper.mobStoreGoal(mob, this);
            }
        }
    }
}
