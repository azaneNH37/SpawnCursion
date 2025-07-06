package com.azane.spcurs.event;

import com.azane.spcurs.util.TargetPredicateHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityHurtEvent
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurt(LivingHurtEvent event)
    {
        LivingEntity receiver = event.getEntity();
        Entity sender = event.getSource().getEntity();
        if(sender == null)
            return;
        if(receiver.getPersistentData().contains(TargetPredicateHelper.TEAM_KEY) &&
        sender.getPersistentData().contains(TargetPredicateHelper.TEAM_KEY))
        {
            if(receiver.getPersistentData().getInt(TargetPredicateHelper.TEAM_KEY)
            == sender.getPersistentData().getInt(TargetPredicateHelper.TEAM_KEY))
                event.setCanceled(true);
        }
    }
}
