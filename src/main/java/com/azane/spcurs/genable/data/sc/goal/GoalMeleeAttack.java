package com.azane.spcurs.genable.data.sc.goal;

import com.azane.cjsop.annotation.JsonClassTypeBinder;
import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.genable.data.ISpcursPlugin;
import com.google.gson.annotations.SerializedName;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

@JsonClassTypeBinder(fullName = "goal.attack.melee", simpleName = "gatkm", namespace = SpcursMod.MOD_ID)
public class GoalMeleeAttack implements ISpcursPlugin
{
    @SerializedName("priority")
    private int priority = 0;
    @SerializedName("speed")
    private double speedModifier = 1.0D;
    @SerializedName("follow_blind")
    private boolean followTargetEvenIfNotSeen = false;

    @Override
    public void onEntityCreate(ServerLevel level, BlockPos blockPos, LivingEntity entity)
    {
        if(entity instanceof PathfinderMob mob)
        {
            mob.goalSelector.addGoal(priority,new MeleeAttackGoal(mob,speedModifier,followTargetEvenIfNotSeen));
        }
    }
}
