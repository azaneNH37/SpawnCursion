package com.azane.spcurs.genable.data.sc.goal;

import com.azane.cjsop.annotation.JsonClassTypeBinder;
import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.genable.data.CommonGoalArg;
import com.azane.spcurs.genable.data.ISpcursPlugin;
import com.azane.spcurs.util.GoalCaster;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

@JsonClassTypeBinder(fullName = "goal.common", simpleName = "gcomn", namespace = SpcursMod.MOD_ID)
public class GoalCommon implements ISpcursPlugin,IPersistantGoal
{
    @Getter
    @Expose(serialize = false,deserialize = false)
    public final ResourceLocation goalType = ResourceLocation.fromNamespaceAndPath(SpcursMod.MOD_ID, "goal.common");

    @SerializedName("priority")
    private int priority = 0;
    @SerializedName("goal")
    private ResourceLocation rl;
    @SerializedName("arg")
    private CommonGoalArg arg;

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
            GoalCaster.GoalFactory<?> factory = GoalCaster.GOAL_MAP.get(rl);
            if(factory == null)
            {
                DebugLogger.error("Goal with name {} not found for entity: {}", rl, entity.getType());
                return;
            }
            Goal goal = factory.create(mob, arg);
            if(goal == null)
            {
                DebugLogger.error("Failed to create goal for entity: {} with arg: {}", entity.getType(), arg);
                return;
            }
            mob.goalSelector.addGoal(priority, goal);
            if(!isRecreate)
                GoalPersistantHelper.mobStoreGoal(mob,this);
        }
    }
}
