package com.azane.spcurs.util;

import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.lib.RlHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class TargetPredicateHelper
{
    public static final String LIVING_ENTITY_TYPE = "living";
    public static final String MONSTER_TYPE = "monster";
    public static final String PLAYER_TYPE = "player";

    public static final Map<String, TagKey<EntityType<?>>> cacheTag = new HashMap<>();

    public static Predicate<LivingEntity> createPredicate(String rl)
    {
        if(rl.equals(MONSTER_TYPE))
            return le -> le instanceof Monster;
        else if (rl.equals(LIVING_ENTITY_TYPE))
            return le -> true;
        else if (rl.equals(PLAYER_TYPE))
            return le -> le instanceof Player;
        if(rl.charAt(0) == '#') {
            String tagName = rl.substring(1);
            ResourceLocation tagRl = RlHelper.parse(tagName);
            if(tagRl == null) {
                DebugLogger.error("Invalid tag resource location: " + rl);
                return null;
            }
            if(!cacheTag.containsKey(tagName))
                cacheTag.put(tagName, TagKey.create(Registries.ENTITY_TYPE, tagRl));
            return le -> le.getType().is(cacheTag.get(tagName));
        } else {
            ResourceLocation tagRl = RlHelper.parse(rl);
            if(tagRl == null) {
                DebugLogger.error("Invalid entity resource location: " + rl);
                return null;
            }
            return le -> tagRl.equals(ForgeRegistries.ENTITY_TYPES.getKey(le.getType()));
        }
    }
}
