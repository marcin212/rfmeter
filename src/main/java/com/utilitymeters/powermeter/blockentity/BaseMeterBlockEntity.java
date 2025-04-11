package com.utilitymeters.powermeter.blockentity;

import com.utilitymeters.logic.CounterLogic;
import com.utilitymeters.powermeter.blocks.BaseMeterBlock;
import com.utilitymeters.powermeter.network.PacketHandler;
import com.utilitymeters.powermeter.network.RfMeterSyncC2SPacket;
import com.utilitymeters.powermeter.network.RfMeterSyncPacket;
import com.utilitymeters.utils.DisplayColor;
import com.utilitymeters.utils.FlowDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public abstract class BaseMeterBlockEntity extends BlockEntity {
    private final CounterLogic logic;
    private DisplayColor displayColor = new DisplayColor();

    public BaseMeterBlockEntity(BlockEntityType<?> type,BlockPos pos, BlockState state, CounterLogic logic) {
        super(type, pos, state);
        this.logic = logic;
    }

    public void tick() {
        if (level == null) return;
        if (level.isClientSide()) {
            logic.clientTick();
        } else {
            logic.serverTick(this);
            if (level.hasChunkAt(getBlockPos())) {
                level.getChunkAt(getBlockPos()).setUnsaved(true);
            }
        }
    }

    public CounterLogic counter() {
        return logic;
    }

    public FlowDirection flowDirection() {
        return getBlockState().getValue(BaseMeterBlock.FLOW_DIRECTION);
    }

    public BlockEntity targetEntity() {
        return getLevel().getBlockEntity(getBlockPos().relative(flowDirection().outputDirection()));
    }

    public <X> LazyOptional<X> targetCapability(Capability<X> cap) {
        var capEntity = targetEntity();
        if(capEntity == null) return LazyOptional.empty();
        return capEntity.getCapability(cap, flowDirection().outputDirection().getOpposite());
    }

    public void redstoneChanged(int value) {
        counter().setRedstoneSignal(value);
    }

    public void onRfMeterSyncPacket(RfMeterSyncPacket packet) {
        if(RfMeterSyncPacket.ContentFlag.COLOR.hasFlag(packet.flags)) {
            displayColor = new DisplayColor(packet.r, packet.g, packet.b, packet.contrast);
        }

        logic.onRfMeterSyncPacket(packet);

        if(hasLevel() && !getLevel().isClientSide) {
            PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(()-> getLevel().getChunkAt(getBlockPos())), ((RfMeterSyncC2SPacket) packet).convert());
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        logic.save(tag);
        displayColor.save(tag);
        saveData(tag);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        logic.load(tag);
        displayColor.load(tag);
        loadData(tag);
        super.load(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        logic.save(tag);
        displayColor.save(tag);
        saveData(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        logic.load(tag);
        displayColor.load(tag);
        loadData(tag);
        super.handleUpdateTag(tag);
    }

    public DisplayColor displayColors() {
        return displayColor;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public abstract void saveData(CompoundTag tag);
    public abstract void loadData(CompoundTag tag);
}
