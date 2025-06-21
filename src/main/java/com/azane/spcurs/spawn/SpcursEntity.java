package com.azane.spcurs.spawn;

import com.azane.spcurs.genable.data.sc.ScCreature;
import com.azane.spcurs.genable.data.sc.ScSpawner;
import com.azane.spcurs.resource.service.ServerDataService;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * SpcursEntity represents a spawner entity in the game.<br>
 * It manages the spawning of creatures based on the configured data.<br>
 * {@link SpcursEntity} is the own and only entity that manages the spawning logic for a specific spawner.<br>
 */
public class SpcursEntity implements INBTSerializable<CompoundTag>
{
    public static final long CHECK_ACTIVE_FREQ = 20L;

    @Getter
    @NotNull
    private final ResourceLocation spawnerID;

    private boolean active = false;
    private long overallTicks = 0;
    private long ticks = 0;

    private LinkedList<ScCreatureSpawnData> spawnDataList = new LinkedList<>();

    private SpcursEntity(@NotNull ResourceLocation rl)
    {
        this.spawnerID = rl;
    }

    @Nullable
    public static SpcursEntity create(ResourceLocation rl,@Nullable CompoundTag tag,boolean isClientSide)
    {
        if(ServerDataService.get().getSpawner(rl) == null)
            return null;
        SpcursEntity entity = new SpcursEntity(rl);
        if(tag == null)
        {
            ServerDataService.get().getSpawner(rl).getCreatures().entrySet()
                .forEach(entry-> entity.insertToSpawnList(new ScCreatureSpawnData(entry.getKey(), entry.getValue()))
                );
        }
        else
            entity.deserializeNBT(tag);
        return entity;
    }

    public void tick(ServerLevel level, BlockPos pos)
    {
        concreteTick(level,pos);
        if(active)
            activeTick(level,pos);
    }

    private void concreteTick(ServerLevel level, BlockPos pos)
    {
        overallTicks++;
        if(overallTicks % CHECK_ACTIVE_FREQ != 0)
            return;
        updateActive(level,pos);
    }

    private void activeTick(ServerLevel level,BlockPos pos)
    {
        ticks++;
        //DebugLogger.log("SpcursEntity active tick at " + pos + " for spawner " + spawnerID + ", ticks: " + ticks);
        while (!spawnDataList.isEmpty() && spawnDataList.getFirst().isReadyToSpawn(ticks))
        {
            ScCreatureSpawnData cache = spawnDataList.removeFirst();
            ScSpawner spawner = ServerDataService.get().getSpawner(spawnerID);
            ScCreature targetCreature = spawner.getCreatures().get(cache.getScCreatureRl());
            if(targetCreature != null)
            {
                if(!cache.isAbleToSpawn(targetCreature))
                {
                    cache.acceptUnitSpawn(targetCreature,0);
                    continue;
                }
                targetCreature.spawn(level,pos,spawner,cache);
                insertToSpawnList(cache);
            }
        }
    }

    private void updateActive(ServerLevel level,BlockPos blockPos)
    {
        boolean isPlayerAround = level.hasNearbyAlivePlayer(
            (double)blockPos.getX() + (double)0.5F,
            (double)blockPos.getY() + (double)0.5F,
            (double)blockPos.getZ() + (double)0.5F,
            ServerDataService.get().getSpawner(spawnerID).getActiveRange());
        active = isPlayerAround;
    }

    private void insertToSpawnList(ScCreatureSpawnData cache)
    {

        ListIterator<ScCreatureSpawnData> it = spawnDataList.listIterator();
        while(it.hasNext())
        {
            if (cache.getNextSpawnTick() < it.next().getNextSpawnTick())
            {
                it.previous();
                it.add(cache);
                return;
            }
        }
        spawnDataList.addLast(cache);
    }

    @Nullable
    public ScCreatureSpawnData getScCreatureSpawnData(ResourceLocation scCreatureRl)
    {
        for (ScCreatureSpawnData spawnData : spawnDataList) {
            if (spawnData.getScCreatureRl().equals(scCreatureRl)) {
                return spawnData;
            }
        }
        return null;
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        for (int i = 0; i < spawnDataList.size(); i++) {
            nbt.put("spawnData_" + i, spawnDataList.get(i).serializeNBT());
        }
        nbt.putInt("spawnDataCount", spawnDataList.size());
        nbt.putBoolean("active", active);
        nbt.putLong("overallTicks", overallTicks);
        nbt.putLong("ticks", ticks);
        nbt.putString("spawnerID", spawnerID.toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        int spawnDataCount = nbt.getInt("spawnDataCount");
        spawnDataList.clear();
        for (int i = 0; i < spawnDataCount; i++) {
            ScCreatureSpawnData spawnData = new ScCreatureSpawnData(
                ResourceLocation.tryParse(nbt.getCompound("spawnData_" + i).getString("scCreatureRl")),
                ServerDataService.get().getSpawner(spawnerID).getCreatures()
                    .get(ResourceLocation.tryParse(nbt.getCompound("spawnData_" + i).getString("scCreatureRl")))
            );
            spawnData.deserializeNBT(nbt.getCompound("spawnData_" + i));
            spawnDataList.add(spawnData);
        }
        active = nbt.getBoolean("active");
        overallTicks = nbt.getLong("overallTicks");
        ticks = nbt.getLong("ticks");
        if (!spawnerID.toString().equals(nbt.getString("spawnerID"))) {
            throw new IllegalArgumentException("SpawnerID mismatch");
        }
    }
}