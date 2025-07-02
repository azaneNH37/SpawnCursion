package com.azane.spcurs.block.entity;

import com.azane.spcurs.lib.RlHelper;
import com.azane.spcurs.registry.ModBlockEntity;
import com.azane.spcurs.spawn.IEnterScSpawner;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TransformScEntity extends BlockEntity implements IEnterScSpawner
{
    @Getter
    @Setter
    private ResourceLocation baseSpawnerID;

    @Getter
    @Setter
    private boolean isChild = false;

    private boolean onlyOnce = false;

    public TransformScEntity(BlockPos pPos, BlockState pBlockState)
    {
        super(ModBlockEntity.TRANS_SC_ENTITY.get(), pPos, pBlockState);
    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, TransformScEntity pBlockEntity)
    {
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, TransformScEntity pBlockEntity)
    {
        if(pLevel.hasNearbyAlivePlayer(pPos.getX(), pPos.getY(), pPos.getZ(), 16))
        {
            if (pBlockEntity.onlyOnce)
                return;
            pLevel.scheduleTick(pPos, pState.getBlock(), pLevel.getRandom().nextInt(5,40));
            pBlockEntity.onlyOnce = true;
        }
    }

    @Override
    public void load(CompoundTag pTag)
    {
        super.load(pTag);
        if (pTag.contains("BaseSpawnerID")) {
            this.baseSpawnerID = RlHelper.parse(pTag.getString("BaseSpawnerID"));
        } else {
            this.baseSpawnerID = null; // Default to null if not present
        }
        if( pTag.contains("IsChild")) {
            this.isChild = pTag.getBoolean("IsChild");
        } else {
            this.isChild = false; // Default to false if not present
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag)
    {
        super.saveAdditional(pTag);
        if (this.baseSpawnerID != null) {
            pTag.putString("BaseSpawnerID", this.baseSpawnerID.toString());
        } else {
            pTag.remove("BaseSpawnerID"); // Remove if null
        }
        pTag.putBoolean("IsChild", this.isChild);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }
}
