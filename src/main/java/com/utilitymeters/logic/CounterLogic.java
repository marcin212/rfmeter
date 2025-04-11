package com.utilitymeters.logic;

import com.utilitymeters.logic.smoothers.Smoother;
import com.utilitymeters.powermeter.MathUtils;
import com.utilitymeters.powermeter.network.PacketHandler;
import com.utilitymeters.powermeter.network.RfMeterSyncPacket;
import com.utilitymeters.powermeter.network.RfMeterSyncS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;

public class CounterLogic {
    int transfer = 0;//curent flow in RF/t
    int transferLimit = -1;
    long value = 0;//current used energy
    long prepaidValue = 0;
    long lastValue = 0;
    Smoother smoother;
    String password = "";
    boolean inCounterMode = true;
    boolean isOn = true;
    boolean isProtected = false;
    boolean redstoneSignal = false;
    boolean tickLock = false;

    int tick = 0;

    public CounterLogic(Smoother smoother) {
        this.smoother = smoother;
    }

    public boolean isInCounterMode() {
        return inCounterMode;
    }

    public void setRedstoneSignal(int redstoneSignal) {
        this.redstoneSignal = redstoneSignal > 0;
    }

    public void setCounterMod(Boolean inCounterMode) {
        this.inCounterMode = inCounterMode;
    }

    public void setTransferLimit(int transferLimit) {
        this.transferLimit = transferLimit;
    }

    public int getTransferLimit() {
        return transferLimit;
    }

    public int getTransfer() {
        return transfer;
    }

    public long getCurrentValue() {
        return value;
    }

    public void setPassword(String pass) {
        password = MathUtils.encryptPassword(pass);
        isProtected = true;
    }

    public void removePassword() {
        password = "";
        isProtected = false;
    }

    public boolean canEdit(String pass) {
        return !isProtected || (pass != null && MathUtils.encryptPassword(pass).equals(password));
    }

    public String getPassword() {
        return password;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public boolean canFlow() {
        return isOn() && (inCounterMode || (0 < calculatedPrepaid()));
    }

    public boolean isOn() {
        return isOn && !redstoneSignal;
    }

    public long calculatedPrepaid() {
        return Math.max(0L, prepaidValue - value);
    }

    public void addTopUp(long value) {
        prepaidValue += value;
    }

    public void subTopUp(long value) {
        prepaidValue = Math.max(0, prepaidValue - value);
    }

    public long getPrepaidValue() {
        return prepaidValue;
    }

    public void serverTick(BlockEntity entity) {
        tick++;
        tickLock = false;

        long lastReceive = Math.abs(value - lastValue);
        smoother.putValue(lastReceive);
        transfer = (int) smoother.smoothedValue();
        lastValue = value;

       // System.out.println("["+entity.getBlockState().getBlock().getDescriptionId()+"]CounterLogic.serverTick: transfer = " + transfer);

        if (tick % 20 == 0) {
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncS2CPacket.class)
                    .addTransfer(transfer)
                    .addValue(value)
                    .addPrepaidValue(prepaidValue)
                    .addCounterMode(inCounterMode)
                    .build();
            PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> entity.getLevel().getChunkAt(entity.getBlockPos())), packet);
            tick = 0;
        }

    }

    public void clientTick() {
        value += transfer;
    }

    public long flow(long maxReceive, boolean simulate) {
        if (!canFlow() || tickLock) return 0;
        long receive = 0;
        if (isInCounterMode()) {
            receive = flowInCounterMode(maxReceive);
        } else {
            receive = flowInPrepaidMode(maxReceive);
        }
        if (!simulate){
            value += receive;
            tickLock = true;
        }
        return receive;
    }

    public Boolean inUnlimitedFlow() {
        return transferLimit == -1;
    }

    private long flowInCounterMode(long maxReceive) {
        if (inUnlimitedFlow()) {
            return maxReceive;
        }
        return Math.min(transferLimit, maxReceive);
    }

    private long flowInPrepaidMode(long maxReceive) {
        if (inUnlimitedFlow()) {
            return Math.min(calculatedPrepaid(), maxReceive);
        }
        return Math.min(transferLimit, Math.min(calculatedPrepaid(), maxReceive));
    }

    public void onRfMeterSyncPacket(RfMeterSyncPacket packet) {
        if(RfMeterSyncPacket.ContentFlag.VALUE.hasFlag(packet.flags)) {
            value = packet.value;
        }

        if(RfMeterSyncPacket.ContentFlag.TRANSFER_LIMIT.hasFlag(packet.flags)) {
            transferLimit = packet.transferLimit;
        }

        if(RfMeterSyncPacket.ContentFlag.PASSWORD.hasFlag(packet.flags)) {
            password = packet.password;
            isProtected = packet.isProtected;
        }

        if(RfMeterSyncPacket.ContentFlag.COUNTER_MODE.hasFlag(packet.flags)) {
            setCounterMod(packet.inCounterMode);
        }

        if(RfMeterSyncPacket.ContentFlag.ON.hasFlag(packet.flags)) {
            isOn = packet.isOn;
        }

        if(RfMeterSyncPacket.ContentFlag.TRANSFER.hasFlag(packet.flags)) {
            transfer = packet.transfer;
        }

        if(RfMeterSyncPacket.ContentFlag.PREPAID_VALUE.hasFlag(packet.flags)) {
            prepaidValue = packet.prepaidValue;
        }
    }

    public void save(CompoundTag nbt) {
        nbt.putInt("transfer", transfer);
        nbt.putInt("transferLimit", transferLimit);
        nbt.putLong("value", value);
        nbt.putLong("prepaidValue", prepaidValue);
        nbt.putLong("lastValue", lastValue);
        nbt.putString("password", password);
        nbt.putBoolean("inCounterMode", inCounterMode);
        nbt.putBoolean("isOn", isOn);
        nbt.putBoolean("redstoneSignal", redstoneSignal);
        nbt.putBoolean("isProtected", isProtected);
    }

    public void load(CompoundTag nbt) {
        if (nbt.contains("transfer"))
            transfer = nbt.getInt("transfer");
        if (nbt.contains("transferLimit"))
            transferLimit = nbt.getInt("transferLimit");
        if (nbt.contains("value"))
            value = nbt.getLong("value");
        if (nbt.contains("lastValue"))
            lastValue = nbt.getLong("lastValue");
        if (nbt.contains("password"))
            password = nbt.getString("password");
        if (nbt.contains("inCounterMode"))
            inCounterMode = nbt.getBoolean("inCounterMode");
        if (nbt.contains("isOn"))
            isOn = nbt.getBoolean("isOn");
        if (nbt.contains("redstone")) //old tag
            redstoneSignal = nbt.getBoolean("redstone");
        if (nbt.contains("redstoneSignal"))
            redstoneSignal = nbt.getBoolean("redstoneSignal");
        if (nbt.contains("isProtected"))
            isProtected = nbt.getBoolean("isProtected");
        if (nbt.contains("prepaidValue"))
            prepaidValue = nbt.getLong("prepaidValue");
    }

}
