package com.azane.spcurs.block.entity;

import com.azane.spcurs.genable.data.sc.collection.ScEffects;
import com.azane.spcurs.genable.data.sc.effects.EfcAttrModifier;
import com.azane.spcurs.genable.data.sc.goal.GoalTargetRemoval;
import com.azane.spcurs.genable.data.sc.goal.GoalTargetScCreature;
import com.azane.spcurs.genable.data.sc.tag.StagInt;
import com.azane.spcurs.item.ScMycoItem;
import com.azane.spcurs.lib.LevelHelper;
import com.azane.spcurs.lib.RlHelper;
import com.azane.spcurs.network.OgnmChannel;
import com.azane.spcurs.network.to_client.SyncCasinoPacket;
import com.azane.spcurs.registry.ModBlockEntity;
import com.azane.spcurs.registry.ModConfig;
import com.azane.spcurs.spawn.IEnterScSpawner;
import com.azane.spcurs.util.TargetPredicateHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.items.ItemStackHandler;

public class CasinoBlockEntity extends BlockEntity
{
    private final ItemStackHandler inventory = new ItemStackHandler(2);

    public CasinoBlockEntity(BlockPos pPos, BlockState pBlockState)
    {
        super(ModBlockEntity.CASINO_ENTITY.get(), pPos, pBlockState);
    }

    public boolean insertItem(ItemStack stack, ServerPlayer player) {
        for (int i = 0; i < 2; i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                inventory.setStackInSlot(i, stack);
                setServerChanged(player);
                return true;
            }
        }
        return false;
    }

    public ItemStack extractItem(ServerPlayer player) {
        for (int i = 1; i >= 0; i--) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                inventory.setStackInSlot(i, ItemStack.EMPTY);
                setServerChanged(player);
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public void consumeAll(ServerLevel level,ServerPlayer player,BlockState state)
    {
        for (int i = 0; i < 2; i++) {
            BlockPos pos = getBlockPos().north(ModConfig.GAME_CASINO_RANGE.get()).east(ModConfig.GAME_CASINO_RANGE.get() * (i == 0 ? 1 : -1));
            pos = pos.above(LevelHelper.getGroundHightAtFullChunk(level,pos, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)-pos.getY()).above();
            ItemStack stack = inventory.getStackInSlot(i);
            if(stack.getItem() instanceof ScMycoItem scMycoItem)
            {
                int finalI = i;
                IEnterScSpawner.placeScSpawner(level,pos,state,(plevel, ppos, pstate)-> scMycoItem.getScSpawnerID(stack),
                    ()-> new ScEffects.Builder()
                        .add("spcurs:efc.attr-tmp",
                            EfcAttrModifier.of(RlHelper.parse("generic.follow_range"), AttributeModifier.Operation.ADDITION,ModConfig.GAME_CASINO_RANGE.get()*6,null,null))
                        .add("spcurs:goal.target.rm-tmp",
                            GoalTargetRemoval.of(true,true))
                        .add("spcurs:goal.target.scc-tmp",
                            GoalTargetScCreature.of(finalI == 0 ? TargetPredicateHelper.CASINO_TEAM_BLUE : TargetPredicateHelper.CASINO_TEAM_RED))
                        .add("spcurs:tag.int-team",
                            StagInt.of(TargetPredicateHelper.TEAM_KEY, finalI == 1 ? TargetPredicateHelper.CASINO_TEAM_BLUE : TargetPredicateHelper.CASINO_TEAM_RED))
                        .build());
            }
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
        setServerChanged(player);
    }

    public boolean isFull() {
        return !inventory.getStackInSlot(0).isEmpty() && !inventory.getStackInSlot(1).isEmpty();
    }

    public boolean isEmpty() {
        return inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(1).isEmpty();
    }

    public void dropContents(Level level, BlockPos pos) {
        for (int i = 0; i < 2; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
    }

    public ItemStack getItem(int slot) {
        return inventory.getStackInSlot(slot);
    }

    public void setServerChanged(ServerPlayer player)
    {
        this.setChanged();
        OgnmChannel.DEFAULT.sendTo(new SyncCasinoPacket(this.getBlockPos().asLong(),this.saveWithoutMetadata()),player);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
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
