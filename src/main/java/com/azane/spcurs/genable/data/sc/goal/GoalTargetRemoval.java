package com.azane.spcurs.genable.data.sc.goal;

import com.azane.cjsop.annotation.JsonClassTypeBinder;
import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.genable.data.ISpcursPlugin;
import com.google.gson.annotations.SerializedName;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;

@JsonClassTypeBinder(fullName = "goal.target.rm",simpleName = "tarrm",namespace = SpcursMod.MOD_ID)
public class GoalTargetRemoval implements ISpcursPlugin
{
    @SerializedName("hurt")
    private boolean hurt = false;
    @SerializedName("nearest")
    private boolean nearest = true;

    @Override
    public void onEntityCreate(ServerLevel level, BlockPos blockPos, LivingEntity entity)
    {
        if(entity instanceof Mob mob)
        {
            GoalSelector targetSelector = mob.targetSelector;
            targetSelector.removeAllGoals(goal ->
               (nearest && goal instanceof NearestHealableRaiderTargetGoal<?>)
                   || (hurt && goal instanceof HurtByTargetGoal)
            );
        }
    }
}