package com.azane.spcurs.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public final class RegistryCaster
{
    public static final ResourceLocation LIVING_ENTITY_TYPE = ResourceLocation.tryParse("living");
    public static final ResourceLocation MONSTER_TYPE = ResourceLocation.tryParse("monster");
    public static final ResourceLocation PLAYER_TYPE = ResourceLocation.tryParse("player");

    public static Optional<Class<? extends Entity>> getEntityClass(ResourceLocation rl)
    {
        if(rl.equals(MONSTER_TYPE))
            return Optional.of(Monster.class);
        else if (rl.equals(LIVING_ENTITY_TYPE)) {
            return Optional.of(LivingEntity.class);
        } else if (rl.equals(PLAYER_TYPE)) {
            return Optional.of(Player.class);
        }
        return EntityType.byString(rl.toString()).map(EntityType::getBaseClass);
    }
}
