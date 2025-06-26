package com.azane.spcurs.genable.data.sc.goal;

import com.azane.cjsop.annotation.JsonClassTypeBinder;
import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.genable.data.ISpcursPlugin;
import com.azane.spcurs.genable.data.sc.ScCreature;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

@JsonClassTypeBinder(fullName = "goal.target.scc",simpleName = "tarscc",namespace = SpcursMod.MOD_ID)
public class GoalTargetScCreature implements ISpcursPlugin,IPersistantGoal
{
    @Getter
    @Expose(serialize = false,deserialize = false)
    public final ResourceLocation goalType = ResourceLocation.fromNamespaceAndPath(SpcursMod.MOD_ID,"goal.target.scc");

    private long spawnerPos;

    public GoalTargetScCreature(){}

    public GoalTargetScCreature(long spawnerPos)
    {
        this.spawnerPos = spawnerPos;
    }

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
            mob.targetSelector.addGoal(0,new NearestAttackableTargetGoal<>(mob, LivingEntity.class,false,living->
                entity.getPersistentData().contains(ScCreature.IDENTIFIER) &&
                    entity.getPersistentData().getCompound(ScCreature.IDENTIFIER).contains("pos") &&
                    entity.getPersistentData().getCompound(ScCreature.IDENTIFIER).getLong("pos") == spawnerPos
            ));

            if(!isRecreate)
                GoalPersistantHelper.mobStoreGoal(mob, this);
        }
    }
}
