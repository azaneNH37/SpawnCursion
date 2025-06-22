package com.azane.spcurs.genable.data.sc.goal;

import com.azane.cjsop.annotation.JsonClassTypeBinder;
import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.genable.data.ISpcursPlugin;
import com.azane.spcurs.genable.data.sc.ScCreature;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

@JsonClassTypeBinder(fullName = "goal.target.scc",simpleName = "tarscc",namespace = SpcursMod.MOD_ID)
public class GoalTargetScCreature implements ISpcursPlugin
{
    private long spawnerPos;

    public GoalTargetScCreature(){}

    public GoalTargetScCreature(long spawnerPos)
    {
        this.spawnerPos = spawnerPos;
    }

    @Override
    public void onEntityCreate(ServerLevel level, BlockPos blockPos, LivingEntity entity)
    {
        if(entity instanceof Mob mob)
        {
            mob.targetSelector.addGoal(0,new NearestAttackableTargetGoal<>(mob, LivingEntity.class,false,living->
                    entity.getPersistentData().contains(ScCreature.IDENTIFIER) &&
                    entity.getPersistentData().getCompound(ScCreature.IDENTIFIER).contains("pos") &&
                    entity.getPersistentData().getCompound(ScCreature.IDENTIFIER).getLong("pos") == spawnerPos
            ));
        }
    }
}
