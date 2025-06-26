package com.azane.spcurs.genable.data.sc.goal;

import com.azane.spcurs.genable.data.ISpcursPlugin;
import com.azane.spcurs.genable.data.ScGson;
import com.azane.spcurs.lib.GsonExtra;
import com.azane.spcurs.lib.RlHelper;
import com.azane.spcurs.resource.helper.ParserHelper;
import com.azane.spcurs.resource.service.JsonTypeManagers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;

public final class GoalPersistantHelper
{
    public static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(ResourceLocation .class,new ResourceLocation.Serializer())
        .addSerializationExclusionStrategy(GsonExtra.EXPOSE_FILTER_serialize)
        .addDeserializationExclusionStrategy(GsonExtra.EXPOSE_FILTER_deserialize)
        .create();

    public static final String SC_GOAL_TAG = "ScGoal";

    /**
     * 在mob从ScSpawner生成时存储Goal
     * @param mob
     * @param goal
     */
    public static void mobStoreGoal(Mob mob,IPersistantGoal goal)
    {
        CompoundTag data = mob.getPersistentData();
        if(!data.contains(SC_GOAL_TAG))
        {
            data.put(SC_GOAL_TAG, new CompoundTag());
            data.getCompound(SC_GOAL_TAG).putInt("cnt",0);
        }
        CompoundTag goalData = data.getCompound(SC_GOAL_TAG);
        goalData.putString("goalType%d".formatted(goalData.getInt("cnt")), goal.getGoalType().toString());
        goalData.putString("goal%d".formatted(goalData.getInt("cnt")), goal.asJsonString());
        goalData.putInt("cnt",goalData.getInt("cnt")+1);
    }

    /**
     * 在mob经过游戏重载后应用Goal
     * @param level
     * @param pos
     * @param mob
     */
    public static void mobApplyGoals(ServerLevel level, BlockPos pos,Mob mob)
    {
        CompoundTag data = mob.getPersistentData();
        if(!data.contains(SC_GOAL_TAG))
            return;

        CompoundTag goalData = data.getCompound(SC_GOAL_TAG);
        int cnt = goalData.getInt("cnt");
        for(int i = 0; i < cnt; i++)
        {
            ResourceLocation goalType = RlHelper.parse(goalData.getString("goalType%d".formatted(i)));
            String goalJson = goalData.getString("goal%d".formatted(i));
            ParserHelper.parseJsonDynamic(ScGson.INSTANCE.GSON,goalJson, ISpcursPlugin.class,goalType, JsonTypeManagers.modTypeManager).onEntityCreate(level, pos, mob);
        }
    }
}