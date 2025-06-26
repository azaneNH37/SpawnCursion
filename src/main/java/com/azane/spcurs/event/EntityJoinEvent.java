package com.azane.spcurs.event;

import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.genable.data.sc.goal.GoalPersistantHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityJoinEvent
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityJoin(EntityJoinLevelEvent event)
    {
        Level level = event.getLevel();
        if(level.isClientSide())
            return;
        Entity entity = event.getEntity();
        if(entity instanceof Mob mob)
        {
            //DebugLogger.log("Applying goals for mob: %s at %s".formatted(mob.getType().getDescriptionId(), mob.getOnPos()));
            GoalPersistantHelper.mobApplyGoals((ServerLevel) level,mob.getOnPos(),mob);
        }
    }
}
