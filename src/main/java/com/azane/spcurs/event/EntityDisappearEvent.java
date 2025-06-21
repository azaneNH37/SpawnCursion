package com.azane.spcurs.event;

import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.genable.data.sc.ScCreature;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber
public class EntityDisappearEvent
{
    /**
     * 如果有mod尝试在该事件post之后修改LivingEntity存活信息，那此处无能为力，将会成为UB行为
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDeath(LivingDeathEvent event)
    {
        if(event.isCanceled())
            return;
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if(level.isClientSide())
            return;
        tryFeedBackSpawner((ServerLevel) level, entity, event.getSource());
    }

    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event)
    {
        if(event.getLevel().isClientSide())
            return;
        Entity entity = event.getEntity();
        Level level = entity.level();
        //if(entity.getPersistentData().contains(ScCreature.IDENTIFIER))
        //    DebugLogger.log("remove:{}",entity.getRemovalReason());
        tryFeedBackSpawner((ServerLevel) level, entity, null);
    }

    private static void tryFeedBackSpawner(ServerLevel level, Entity entity, @Nullable DamageSource damageSource)
    {
        //Not removed, no need to feedback
        if(!entity.isRemoved())
            return;
        if(entity.getPersistentData().contains(ScCreature.IDENTIFIER))
        {
            CompoundTag ctag = entity.getPersistentData().getCompound(ScCreature.IDENTIFIER);
            entity.getPersistentData().remove(ScCreature.IDENTIFIER);
            ScCreature.feedbackSpawner(level,entity,ctag, damageSource);
        }
    }
}
