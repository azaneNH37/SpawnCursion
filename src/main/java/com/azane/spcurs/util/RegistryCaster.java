package com.azane.spcurs.util;

import com.azane.spcurs.lib.RlHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public final class RegistryCaster
{
    public static final ResourceLocation LIVING_ENTITY_TYPE = RlHelper.parse("living");
    public static final ResourceLocation MONSTER_TYPE = RlHelper.parse("monster");
    public static final ResourceLocation PLAYER_TYPE = RlHelper.parse("player");

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
