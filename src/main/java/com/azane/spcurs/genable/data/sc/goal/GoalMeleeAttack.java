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
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonClassTypeBinder(fullName = "goal.attack.melee", simpleName = "gatkm", namespace = SpcursMod.MOD_ID)
public class GoalMeleeAttack implements ISpcursPlugin,IPersistantGoal
{
    @Getter
    @Expose(serialize = false,deserialize = false)
    public final ResourceLocation goalType = ResourceLocation.fromNamespaceAndPath(SpcursMod.MOD_ID, "goal.attack.melee");

    @SerializedName("priority")
    private int priority = 0;
    @SerializedName("speed")
    private double speedModifier = 1.0D;
    @SerializedName("follow_blind")
    private boolean followTargetEvenIfNotSeen = false;

    @Override
    public void onEntityCreate(ServerLevel level, BlockPos blockPos, LivingEntity entity)
    {
        applyGoalToEntity(level, blockPos, entity, false);
    }

    @Override
    public void applyGoalToEntity(ServerLevel level, BlockPos blockPos, LivingEntity entity, boolean isRecreate)
    {
        if(entity instanceof PathfinderMob mob)
        {
            mob.goalSelector.addGoal(priority,new MeleeAttackGoal(mob,speedModifier,followTargetEvenIfNotSeen));
            if(!isRecreate)
            {
                GoalPersistantHelper.mobStoreGoal(mob,this);
            }
        }
    }
}
