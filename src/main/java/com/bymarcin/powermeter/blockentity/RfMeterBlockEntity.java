package com.bymarcin.powermeter.blockentity;

import com.bymarcin.powermeter.PacketHandler;
import com.bymarcin.powermeter.RfMeterMod;
import com.bymarcin.powermeter.registry.RfMeterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RfMeterBlockEntity extends BlockEntity {
    public long lastFlowValue = 0;
    public long value = 0;
    public RfMeterBlockEntity(BlockPos pos, BlockState state) {
        super(RfMeterBlockEntities.RF_METER.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putLong("LastFlowValue", lastFlowValue);
        tag.putLong("Energy", value);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("LastFlowValue")) {
            lastFlowValue = tag.getLong("LastFlowValue");
        }
        if (tag.contains("Energy")) {
            value = tag.getLong("Energy");
        }
        super.load(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag =  super.getUpdateTag();
        tag.putLong("LastFlowValue", lastFlowValue);
        tag.putLong("Energy", value);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag.contains("LastFlowValue")) {
            lastFlowValue = tag.getLong("LastFlowValue");
        }
        if (tag.contains("Energy")) {
            value = tag.getLong("Energy");
        }
        super.handleUpdateTag(tag);
    }

    public long getValue() {
        return value;
    }

    public long getFlowValue() {
        return lastFlowValue;
    }

    public void tickServer() {
        if (level == null || level.isClientSide) return;



        RfMeterMod.LOGGER.debug("TICK");
        if(level.getGameTime() % 20 == 0) {
            lastFlowValue += (int) Math.max(0,Math.ceil(Math.random() * 20) - 10);
            lastFlowValue  = Math.max(0, lastFlowValue);
        }
        value += lastFlowValue;
        RfMeterMod.LOGGER.debug(String.valueOf(lastFlowValue));
        RfMeterMod.LOGGER.debug(String.valueOf(value));

        var packet = new PacketHandler.ClientSyncPacket(getBlockPos(), lastFlowValue, value);
        PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(()-> level.getChunkAt(worldPosition)), packet);
    }

}
