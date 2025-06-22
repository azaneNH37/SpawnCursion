package com.azane.spcurs.util;

import com.azane.spcurs.genable.data.CommonGoalArg;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class GoalCaster
{
    @FunctionalInterface
    public interface GoalFactory<T extends Goal>
    {
        @Nullable
        T create(Mob mob, CommonGoalArg arg);
    }

    public static final Map<ResourceLocation, GoalFactory<?>> GOAL_MAP = new HashMap<>();

    static {
        register("avoid", (mob,arg)->{
            if(mob instanceof PathfinderMob pmob)
            {
                Class<? extends Entity> targetClass = RegistryCaster.getEntityClass(arg.getEntityType())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid target type: " + arg.getEntityType()));
                return new AvoidEntityGoal<>(pmob,targetClass.asSubclass(LivingEntity.class),(float) arg.getRange(),(float) arg.getSpeedModifier(),(float) arg.getSpeedModifier());
            }
            return null;
        });
        register("breath",((mob, arg) -> mob instanceof PathfinderMob ? new BreathAirGoal((PathfinderMob) mob) : null));
        register("float",((mob, arg) -> new FloatGoal(mob)));
        register("panic",((mob, arg) -> mob instanceof PathfinderMob ? new PanicGoal((PathfinderMob) mob,arg.getSpeedModifier()) : null));
        register("move_restrict",((mob, arg) -> mob instanceof PathfinderMob ? new MoveTowardsRestrictionGoal((PathfinderMob) mob,arg.getSpeedModifier()) : null));
        register("moveto_target",((mob, arg) -> mob instanceof PathfinderMob ? new MoveTowardsTargetGoal((PathfinderMob) mob,arg.getSpeedModifier(),(float) arg.getRange()) : null));
        register("random_look",((mob, arg) -> new RandomLookAroundGoal(mob)));
    }

    public static void register(String name, GoalFactory<?> factory)
    {
        ResourceLocation rl = ResourceLocation.parse(name);
        if(GOAL_MAP.containsKey(rl))
        {
            throw new IllegalArgumentException("Goal with name " + name + " already registered!");
        }
        GOAL_MAP.put(rl, factory);
    }

    public GoalFactory<?> getGoalFactory(ResourceLocation name)
    {
        return GOAL_MAP.get(name);
    }
}
