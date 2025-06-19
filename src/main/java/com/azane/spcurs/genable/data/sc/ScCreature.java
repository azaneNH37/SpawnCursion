package com.azane.spcurs.genable.data.sc;

import com.azane.spcurs.genable.data.SpawnConfig;
import com.azane.spcurs.genable.data.sc.collection.ScEffects;
import com.azane.spcurs.spawn.ScCreatureSpawnData;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

@Getter
public class ScCreature
{
    @SerializedName("creature")
    private ResourceLocation creature;
    @SerializedName("spawn_config")
    private SpawnConfig spawnConfig;
    @SerializedName("effects")
    private ScEffects effects;

    public void spawn(ServerLevel level, BlockPos centre, ScSpawner scSpawner, ScCreatureSpawnData spawnData)
    {
        RandomSource random = level.getRandom();
        EntityType<?> creatureType = EntityType.byString(creature.toString()).orElse(EntityType.PIG);
        int success = 0;
        for(int i=0;i< spawnConfig.getSpawnCnt();i++)
        {
            Position pos = new Vec3(
                centre.getX()+(random.nextDouble()- random.nextDouble())*spawnConfig.getRange() + 0.5D,
                centre.getY()+random.nextInt(3)-1,
                centre.getZ()+(random.nextDouble()- random.nextDouble())*spawnConfig.getRange() + 0.5D
            );
            if(level.noCollision(creatureType.getAABB(pos.x(), pos.y(), pos.z())))
            {
                BlockPos targetPos = BlockPos.containing(pos);
                if (!SpawnPlacements.checkSpawnRules(creatureType, level, MobSpawnType.SPAWNER, targetPos, random))
                    continue;
                Entity entity = creatureType.create(level);
                if(entity == null)
                    break;
                entity.moveTo(targetPos,random.nextFloat() * 360.0F, 0.0F);
                if (entity instanceof Mob mob)
                {
                    mob.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random spawn bonus", random.triangle(0.0D, 0.11485000000000001D), AttributeModifier.Operation.MULTIPLY_BASE));
                    mob.setLeftHanded(random.nextFloat() < 0.05F);
                }
                if (!level.addFreshEntity(entity))
                    break;
                success++;
                if(entity instanceof LivingEntity living)
                {
                    effects.entrySet().forEach(entry->{
                        entry.getValue().onEntityCreate(level,centre,living);
                    });
                }
                level.levelEvent(2004, centre, 0);
                level.gameEvent(entity, GameEvent.ENTITY_PLACE, targetPos);
                if (entity instanceof Mob mob)
                    mob.spawnAnim();
            }
        }
        spawnData.acceptUnitSpawn(this,success);
    }

    @Override
    public String toString()
    {
        return "ScCreature{" +
                "\ncreature=" + creature +
                "\nspawnConfig=" + (spawnConfig != null ? spawnConfig.toString() : "<null>") +
                "\neffects=" + (effects != null ? effects.toString() : "<null>") +
                '}';
    }
}