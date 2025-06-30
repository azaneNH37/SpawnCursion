package com.azane.spcurs.genable.data.sc.goal;

import com.azane.cjsop.annotation.JsonClassTypeBinder;
import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.genable.data.ISpcursPlugin;
import com.azane.spcurs.lib.RlHelper;
import com.azane.spcurs.util.TargetPredicateHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import java.util.function.Predicate;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonClassTypeBinder(fullName = "goal.target.add", simpleName = "taradd", namespace = SpcursMod.MOD_ID)
public class GoalTargetAdd implements ISpcursPlugin,IPersistantGoal
{
    @Getter
    @Expose(serialize = false,deserialize = false)
    public final ResourceLocation goalType = RlHelper.build(SpcursMod.MOD_ID, "goal.target.add");

    @SerializedName("priority")
    private int priority;

    @SerializedName("target")
    private String targetType;
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
        if(entity instanceof Mob mob) {
            if (targetType == null || targetType.isEmpty()) {
                DebugLogger.warn("Target type is not specified for EfcTargetAdd effect on entity: " + entity.getType());
                return;
            }
            Predicate<LivingEntity> filter = TargetPredicateHelper.createPredicate(targetType);
            if(filter == null) {
                DebugLogger.warn("Failed to create target predicate for type: " + targetType);
                return;
            }
            mob.targetSelector.addGoal(priority, new NearestAttackableTargetGoal<>(mob, LivingEntity.class, randInterval, mustSee, mustReach, filter));

            if (!isRecreate) {
                GoalPersistantHelper.mobStoreGoal(mob, this);
            }
        }
    }
}
