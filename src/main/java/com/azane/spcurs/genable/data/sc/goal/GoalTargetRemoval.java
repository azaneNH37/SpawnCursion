package com.azane.spcurs.genable.data.sc.goal;

import com.azane.cjsop.annotation.JsonClassTypeBinder;
import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.genable.data.ISpcursPlugin;
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
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonClassTypeBinder(fullName = "goal.target.rm",simpleName = "tarrm",namespace = SpcursMod.MOD_ID)
public class GoalTargetRemoval implements ISpcursPlugin,IPersistantGoal
{
    @Getter
    @Expose(serialize = false,deserialize = false)
    public final ResourceLocation goalType = ResourceLocation.fromNamespaceAndPath(SpcursMod.MOD_ID, "goal.target.rm");

    @SerializedName("hurt")
    private boolean hurt = false;
    @SerializedName("nearest")
    private boolean nearest = true;

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
            GoalSelector targetSelector = mob.targetSelector;
            targetSelector.removeAllGoals(goal ->
                (nearest && (goal instanceof NearestAttackableTargetGoal<?>))
                    || (hurt && (goal instanceof HurtByTargetGoal))
            );

            if(!isRecreate)
                GoalPersistantHelper.mobStoreGoal(mob, this);
        }
    }
}