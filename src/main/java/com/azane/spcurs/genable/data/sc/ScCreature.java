package com.azane.spcurs.genable.data.sc;

import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.genable.data.SpawnConfig;
import com.azane.spcurs.genable.data.sc.collection.ScEffects;
import com.azane.spcurs.lib.IComponentDisplay;
import com.azane.spcurs.lib.RlHelper;
import com.azane.spcurs.resource.helper.IresourceLocation;
import com.azane.spcurs.spawn.IScSpawner;
import com.azane.spcurs.spawn.ScCreatureSpawnData;
import com.azane.spcurs.spawn.SpcursEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public class ScCreature implements IresourceLocation, IComponentDisplay
{
    public static final String IDENTIFIER = "sc_creature";
    public static final Marker MARKER = MarkerManager.getMarker("ScCreature");

    @Expose(serialize = false, deserialize = false)
    @Getter
    @Setter
    private ResourceLocation id;

    @SerializedName("creature")
    private ResourceLocation creature;
    @SerializedName("spawn_config")
    private SpawnConfig spawnConfig = new SpawnConfig();
    @SerializedName("effects")
    private ScEffects effects = new ScEffects();

    public void spawn(ServerLevel level, BlockPos centre, SpcursEntity spcursEntity, ScCreatureSpawnData spawnData)
    {
        //DebugLogger.log("Spawning creature " + id + " at " + centre + " with spawn data: " + spawnData);
        ScSpawner scSpawner = spcursEntity.getScSpawner();
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
                //if (!SpawnPlacements.checkSpawnRules(creatureType, level, MobSpawnType.SPAWNER, targetPos, random))
                //    continue;
                Entity entity = creatureType.create(level,null,null,targetPos, MobSpawnType.SPAWNER, false, false);
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
                linkSpawner(level,centre,spcursEntity,entity);
                //DebugLogger.log("Spawned creature " + id + " at " + targetPos);
                if(entity instanceof LivingEntity living)
                {
                    scSpawner.getGlobalEffects().entrySet().forEach(entry-> entry.getValue().onEntityCreate(level,centre,living));
                    effects.entrySet().forEach(entry-> entry.getValue().onEntityCreate(level,centre,living));
                    if(spcursEntity.getTempSpawnModifier() != null)
                        spcursEntity.getTempSpawnModifier().entrySet().forEach(entry -> entry.getValue().onEntityCreate(level, centre, living));
                }
                level.levelEvent(2004, centre, 0);
                level.gameEvent(entity, GameEvent.ENTITY_PLACE, targetPos);
                if (entity instanceof Mob mob)
                    mob.spawnAnim();
            }
        }
        spawnData.acceptUnitSpawn(this,success);
        //DebugLogger.log("Spawning creature " + id + " at " + centre + " with spawn data: " + spawnData);
    }

    public void linkSpawner(ServerLevel level,BlockPos pos,SpcursEntity spcursEntity,Entity entity)
    {
        CompoundTag tag = new CompoundTag();
        tag.putLong("pos",pos.asLong());
        tag.putString("level",level.toString());
        tag.putString("ScCreature",id.toString());
        entity.getPersistentData().put(IDENTIFIER, tag);
        //DebugLogger.log("Linked ScCreature with NBT {}", tag);
    }

    public static void feedbackSpawner(ServerLevel level, Entity entity, CompoundTag data, @Nullable DamageSource damageSource)
    {
        if(!(data.contains("pos") && data.contains("level") && data.contains("ScCreature")))
        {
            DebugLogger.error(MARKER,"ScCreature feedback data is invalid: " + data);
            return;
        }
        BlockPos pos = BlockPos.of(data.getLong("pos"));
        String levelName = data.getString("level");
        if(!level.toString().equals(levelName))
        {
            DebugLogger.warn(MARKER,"ScCreature feedback level mismatch: expected " + level + ", got " + levelName);
            return;
        }
        ResourceLocation creatureId = RlHelper.parse(data.getString("ScCreature"));
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof IScSpawner scBlockEntity)
        {
            SpcursEntity spawner = scBlockEntity.getSpawner();
            if(spawner == null)
            {
                DebugLogger.warn(MARKER,"ScCreature feedback spawner is null at " + pos);
                return;
            }
            ScCreatureSpawnData spawnData = spawner.getScCreatureSpawnData(creatureId);
            ScSpawner scSpawner = spawner.getScSpawner();
            ScCreature creature = scSpawner.getCreatures().get(creatureId);
            if(spawnData == null)
            {
                DebugLogger.warn(MARKER,"ScCreature feedback spawn-data is null for creature " + creatureId + " at " + pos);
                return;
            }
            if(creature == null)
            {
                DebugLogger.warn(MARKER,"ScCreature feedback creature is null for " + creatureId + " at " + pos);
                return;
            }
            //DebugLogger.info(MARKER,"ScCreature feedback for " + creatureId + " at " + pos + ", entity: " + entity.getType() + ", damageSource: " + damageSource);
            spawnData.acceptUnitKilled(spawner,creature,creature.shouldCountDeath(entity,damageSource));
            //DebugLogger.log("Spawning creature feedback with spawn data: " + spawnData);
            return;
        }
        DebugLogger.warn(MARKER,"ScCreature feedback block entity is not an IScSpawner: " + blockEntity + " at " + pos);
    }

    public boolean shouldCountDeath(Entity entity,DamageSource damageSource)
    {
        if(entity.getRemovalReason() != Entity.RemovalReason.KILLED)
            return false;
        //TODO: 这里可以添加伤害类型检查来判断是否应该计数死亡
        return true;
    }

    @Override
    public String toString()
    {
        return "ScCreature{" +
                "\nid=" + id +
                "\ncreature=" + creature +
                "\nspawnConfig=" + (spawnConfig != null ? spawnConfig.toString() : "<null>") +
                "\neffects=" + (effects != null ? effects.toString() : "<null>") +
                '}';
    }

    @Override
    public void appendHoverText(ItemStack stack, List<Component> tooltip, TooltipFlag flag)
    {
        EntityType<?> entityType = EntityType.byString(creature.toString()).orElse(EntityType.PIG);
        tooltip.add(Component.translatable(entityType.getDescriptionId()).withStyle(ChatFormatting.WHITE));
        spawnConfig.appendHoverText(stack, tooltip, flag);
    }
}