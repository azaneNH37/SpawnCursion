package com.azane.spcurs.block.entity;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.registry.ModBlockEntity;
import com.azane.spcurs.spawn.IScSpawner;
import com.azane.spcurs.spawn.SpcursEntity;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SpcursSpawnerBlockEntity extends BlockEntity implements IScSpawner
{
    @Getter
    @Nullable
    private SpcursEntity spawner;

    public SpcursSpawnerBlockEntity(BlockPos pPos, BlockState pBlockState)
    {
        super(ModBlockEntity.SPCURS_SPAWNER_ENTITY.get(), pPos, pBlockState);
    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, SpcursSpawnerBlockEntity pBlockEntity)
    {
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, SpcursSpawnerBlockEntity pBlockEntity)
    {
        if(pBlockEntity.spawner == null)
            return;
        pBlockEntity.spawner.tick((ServerLevel) pLevel,pPos);
        //TODO: it is really-really ugly to set changed every tick, but in order to keep track of the tick, it is necessary?
        pBlockEntity.setChanged();
    }

    /**
     * 这个方法只会设置spcursEntity本身，本身的方块放置不做处理<br>
     * Sets the spawner for the given position in the level.
     * @param level The server level where the block entity is located.
     * @param pos The position of the block entity.
     * @param rl The ResourceLocation of the spawner to set.
     */
    public static void setSpawner(ServerLevel level,BlockPos pos,ResourceLocation rl)
    {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof SpcursSpawnerBlockEntity spawnerBlockEntity)
        {
            spawnerBlockEntity.spawner = SpcursEntity.create(rl,null,false);
            spawnerBlockEntity.setChanged();
        }
    }

    @Override
    public void load(CompoundTag pTag)
    {
        super.load(pTag);
        SpcursMod.LOGGER.warn("load");
        if(pTag.contains("spawnerID"))
        {
            CompoundTag ctag = null;
            if(pTag.contains("spawner"))
                ctag = pTag.getCompound("spawner");
            this.spawner = SpcursEntity.create(ResourceLocation.parse(pTag.getString("spawnerID")), ctag, this.getLevel() == null || this.getLevel().isClientSide());
        }
        else
            this.spawner = null;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag)
    {
        super.saveAdditional(pTag);
        SpcursMod.LOGGER.warn("saveAdditional");
        if(spawner != null)
        {
            pTag.putString("spawnerID",spawner.getSpawnerID().toString());
            pTag.put("spawner",spawner.serializeNBT());
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        //DebugLogger.log("Update");
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        //DebugLogger.log("UpdateTag");
        return this.saveWithoutMetadata();
    }
}
