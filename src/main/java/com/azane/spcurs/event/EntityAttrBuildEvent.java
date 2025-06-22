package com.azane.spcurs.event;

import com.azane.spcurs.SpcursMod;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpcursMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttrBuildEvent
{
    @SubscribeEvent
    public static void onEntityAttrBuild(EntityAttributeModificationEvent event)
    {
        event.getTypes().stream()
            .filter(entityType -> !event.has(entityType, Attributes.ATTACK_DAMAGE))
            .forEach(entityType -> event.add(entityType, Attributes.ATTACK_DAMAGE));
        event.getTypes().stream()
            .filter(entityType -> !event.has(entityType,Attributes.ATTACK_SPEED))
            .forEach(entityType -> event.add(entityType, Attributes.ATTACK_SPEED));
    }
}
