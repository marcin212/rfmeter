package com.utilitymeters.powermeter;


import com.utilitymeters.powermeter.blocks.RfMeterBlock;
import com.utilitymeters.powermeter.network.PacketHandler;
import com.utilitymeters.powermeter.network.RfMeterSyncC2SPacket;
import com.utilitymeters.powermeter.network.RfMeterSyncPacket;
import com.utilitymeters.powermeter.network.RfMeterSyncS2CPacket;
import com.utilitymeters.utils.DisplayColor;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RfMeterLogic {

//    int transfer = 0;//curent flow in RF/t
//    int transferLimit = -1;
//    long value = 0;//current used energy
//    long prepaidValue = 0;
//    long lastValue = 0;
//    Avg avg = new Avg();
//    String password = "";
//    boolean inCounterMode = true;
//    boolean isOn = true;
//    boolean isProtected = false;
//    boolean redstone = false;
//
//    int tick = 0;
//
//    public DisplayColor color = new DisplayColor();
//    ForgeEnergy up;
//    ForgeEnergy down;
//    LazyOptional<ForgeEnergy> energyStorageUp;
//    LazyOptional<ForgeEnergy> energyStorageDown;
//
//    OldRfMeterBlockEntity entity;
//
//    public RfMeterLogic(OldRfMeterBlockEntity entity) {
//        this.entity = entity;
//        this.up = new ForgeEnergy(this, Direction.UP);
//        this.down = new ForgeEnergy(this, Direction.DOWN);
//        energyStorageUp = LazyOptional.of(()->up);
//        energyStorageDown = LazyOptional.of(()->down);
//    }
//
//    static class ForgeEnergy implements IEnergyStorage {
//        RfMeterLogic rfMeterLogic;
//        Direction face;
//
//        public ForgeEnergy(RfMeterLogic rfMeterLogic, Direction face) {
//            this.rfMeterLogic = rfMeterLogic;
//            this.face = face;
//        }
//
//        @Override
//        public int receiveEnergy(int maxReceive, boolean simulate) {
//            return rfMeterLogic.receiveEnergy(face, maxReceive, simulate);
//        }
//
//        @Override
//        public int extractEnergy(int maxExtract, boolean simulate) {
//            return rfMeterLogic.extractEnergy(face, maxExtract, simulate);
//        }
//
//        @Override
//        public int getEnergyStored() {
//            return rfMeterLogic.getEnergyStored(face);
//        }
//
//        @Override
//        public int getMaxEnergyStored() {
//            return rfMeterLogic.getMaxEnergyStored(face);
//        }
//
//        @Override
//        public boolean canExtract() {
//            return rfMeterLogic.canConnectEnergy(face, false);
//        }
//
//        @Override
//        public boolean canReceive() {
//            return rfMeterLogic.canConnectEnergy(face, true);
//        }
//    }
//
//    public boolean isInCounterMode() {
//        return inCounterMode;
//    }
//
//    public void setCounterMod(Boolean inCounterMode) {
//        this.inCounterMode = inCounterMode;
//    }
//
//    @NotNull
//    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
//        if (ForgeCapabilities.ENERGY.equals(cap)) {
//            if (side == Direction.UP) {
//                return energyStorageUp.cast();
//            } else if (side == Direction.DOWN) {
//                return energyStorageDown.cast();
//            }
//        }
//        return LazyOptional.empty();
//    }
//
//    public DisplayColor getColor() {
//        return color;
//    }
//
//    protected boolean canConnectEnergy(Direction from, boolean receive) {
//        return (receive ^ isInverted()) ? (from == Direction.UP) : (from == Direction.DOWN);
//    }
//
//    protected int getEnergyStored(Direction from) {
//        return 0;
//    }
//
//    protected int getMaxEnergyStored(Direction from) {
//        return 10000;
//    }
//
//    public boolean isInverted() {
//        return entity.getBlockState().getValue(RfMeterBlock.FLOW_DIRECTION) == RfMeterBlock.FlowDirection.DOWN_UP;
//    }
//
//    public void setTransferLimit(int transferLimit) {
//        this.transferLimit = transferLimit;
//    }
//
//    public int getTransferLimit() {
//        return transferLimit;
//    }
//
//    public int getTransfer() {
//        return transfer;
//    }
//
//    public long getCurrentValue() {
//        return value;
//    }
//
//    public void setPassword(String pass) {
//        password = MathUtils.encryptPassword(pass);
//        isProtected = true;
//    }
//
//    public void removePassword() {
//        password = "";
//        isProtected = false;
//    }
//
//    public boolean canEdit(String pass) {
//        return !isProtected || (pass != null && MathUtils.encryptPassword(pass).equals(password));
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public boolean isProtected() {
//        return isProtected;
//    }
//
//    public boolean canEnergyFlow() {
//        return isOn && (inCounterMode || (0 < calculatedPrepaid())) && !redstone;
//    }
//
//    private int checkRedstone(){
//        Direction side = entity.getBlockState().getValue(RfMeterBlock.FACING).getOpposite();
//        if(entity.hasLevel()) {
//            return entity.getLevel().getSignal(entity.getBlockPos().relative(side), side);
//        }
//        return 0;
//    }
//
//    public void updateRedstone(){
//        redstone = 0!=checkRedstone();
//    }
//
//
//    public void onRfMeterSyncPacket(RfMeterSyncPacket packet) {
//
//        if(RfMeterSyncPacket.ContentFlag.COLOR.hasFlag(packet.flags)) {
//            color = new DisplayColor(packet.r, packet.g, packet.b, packet.contrast);
//        }
//
//        if(RfMeterSyncPacket.ContentFlag.VALUE.hasFlag(packet.flags)) {
//            value = packet.value;
//        }
//
//        if(RfMeterSyncPacket.ContentFlag.TRANSFER_LIMIT.hasFlag(packet.flags)) {
//            transferLimit = packet.transferLimit;
//        }
//
//        if(RfMeterSyncPacket.ContentFlag.PASSWORD.hasFlag(packet.flags)) {
//            password = packet.password;
//            isProtected = packet.isProtected;
//        }
//
//        if(RfMeterSyncPacket.ContentFlag.COUNTER_MODE.hasFlag(packet.flags)) {
//            setCounterMod(packet.inCounterMode);
//        }
//
//        if(RfMeterSyncPacket.ContentFlag.ON.hasFlag(packet.flags)) {
//            isOn = packet.isOn;
//        }
//
//        if(RfMeterSyncPacket.ContentFlag.TRANSFER.hasFlag(packet.flags)) {
//            transfer = packet.transfer;
//        }
//
//        if(RfMeterSyncPacket.ContentFlag.PREPAID_VALUE.hasFlag(packet.flags)) {
//            prepaidValue = packet.prepaidValue;
//        }
//
//        if(entity.hasLevel() && !entity.getLevel().isClientSide) {
//            PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(()-> entity.getLevel().getChunkAt(entity.getBlockPos())), ((RfMeterSyncC2SPacket) packet).convert());
//        }
//    }
//
//    public boolean isOn() {
//        return isOn;
//    }
//
//    public long calculatedPrepaid() {
//        return Math.max(0L, prepaidValue - value);
//    }
//
//    public void addTopUp(long value) {
//        prepaidValue += value;
//    }
//
//    public void subTopUp(long value) {
//        prepaidValue = Math.max(0, prepaidValue - value);
//    }
//
//    public long getPrepaidValue() {
//        return prepaidValue;
//    }
//
//    public void serverTick() {
//        tick++;
//        if (tick % 20 == 0) {
//            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncS2CPacket.class)
//                    .addTransfer(transfer)
//                    .addValue(value)
//                    .addPrepaidValue(prepaidValue)
//                    .addCounterMode(inCounterMode)
//                    .build();
//                PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> entity.getLevel().getChunkAt(entity.getBlockPos())), packet);
//            tick = 0;
//        }
//        long lastRecive = Math.abs(value - lastValue);
//        avg.putValue(lastRecive);
//        transfer = (int) avg.getAvg();
//        lastValue = value;
//
//    }
//
//    public void clientTick() {
//        tick++;
//        value += transfer;
//    }
//
//    public IEnergyStorage getEnergyStorage(Direction direction){
//        var energyEntity = entity.getLevel().getBlockEntity(entity.getBlockPos().relative(direction));
//        if(energyEntity == null) return null;
//        var capability = energyEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite());
//        if(capability.isPresent()) {
//            return capability.orElse(null);
//        }
//        return null;
//    }
//
//    protected int receiveEnergy(Direction from, int maxReceive, boolean simulate) {
//        if (!canEnergyFlow()) return 0;
//        if (from == (isInverted() ? Direction.DOWN : Direction.UP)) {
//            IEnergyStorage storage = getEnergyStorage(isInverted() ? Direction.UP : Direction.DOWN);
//            if(storage!=null){
//                int temp = storage.receiveEnergy(transferLimit == -1 ?
//                                (inCounterMode ? maxReceive : Math.min(MathUtils.fromLong(calculatedPrepaid()), maxReceive))
//                                : Math.min(transferLimit, (inCounterMode ? maxReceive : Math.min(MathUtils.fromLong(calculatedPrepaid()), maxReceive)))
//                        , simulate);
//
//                if (!simulate) value += temp;
//                return temp;
//            }
//        }
//        return 0;
//    }
//
//    protected int extractEnergy(Direction from, int maxExtract, boolean simulate) {
//        return 0;
//    }
//
//    public void getUpdateTag(CompoundTag nbt) {
//        nbt.putInt("transferLimit", transferLimit);
//
//        nbt.putLong("value", value);
//        nbt.putLong("prepaidValue", prepaidValue);
//        nbt.putLong("lastValue", lastValue);
//
//        nbt.putString("password", password);
//
//        nbt.putBoolean("inCounterMode", inCounterMode);
//        nbt.putBoolean("isOn", isOn);
//        nbt.putBoolean("isProtected", isProtected);
//
//        nbt.put("color", color.save());
//
//        nbt.putBoolean("redstone", redstone);
//    }
//
//    public void handleUpdateTag(CompoundTag nbt) {
//        if (nbt.contains("transferLimit"))
//            transferLimit = nbt.getInt("transferLimit");
//        if (nbt.contains("value"))
//            value = nbt.getLong("value");
//        if (nbt.contains("lastValue"))
//            lastValue = nbt.getLong("lastValue");
//        if (nbt.contains("password"))
//            password = nbt.getString("password");
//        if (nbt.contains("inCounterMode"))
//            inCounterMode = nbt.getBoolean("inCounterMode");
//        if (nbt.contains("isOn"))
//            isOn = nbt.getBoolean("isOn");
//        if (nbt.contains("isProtected"))
//            isProtected = nbt.getBoolean("isProtected");
//        if (nbt.contains("color"))
//            color = new DisplayColor(nbt.getCompound("color"));
//        if(nbt.contains("redstone"))
//            redstone = nbt.getBoolean("redstone");
//        if(nbt.contains("prepaidValue"))
//            prepaidValue = nbt.getLong("prepaidValue");
//    }
//
//    public void saveAdditional(CompoundTag nbt) {
//        nbt.putInt("transfer", transfer);
//        nbt.putInt("transferLimit", transferLimit);
//
//        nbt.putLong("value", value);
//        nbt.putLong("prepaidValue", prepaidValue);
//        nbt.putLong("lastValue", lastValue);
//
//        nbt.putString("password", password);
//
//        nbt.putBoolean("inCounterMode", inCounterMode);
//        nbt.putBoolean("isOn", isOn);
//        nbt.putBoolean("isProtected", isProtected);
//
//        nbt.putInt("tick", tick);
//
//        nbt.put("color", color.save());
//
//        nbt.putBoolean("redstone", redstone);
//    }
//
//    public void load(CompoundTag nbt) {
//        if (nbt.contains("transfer"))
//            transfer = nbt.getInt("transfer");
//        if (nbt.contains("transferLimit"))
//            transferLimit = nbt.getInt("transferLimit");
//        if (nbt.contains("value"))
//            value = nbt.getLong("value");
//        if (nbt.contains("lastValue"))
//            lastValue = nbt.getLong("lastValue");
//        if (nbt.contains("password"))
//            password = nbt.getString("password");
//        if (nbt.contains("inCounterMode"))
//            inCounterMode = nbt.getBoolean("inCounterMode");
//        if (nbt.contains("isOn"))
//            isOn = nbt.getBoolean("isOn");
//        if (nbt.contains("isProtected"))
//            isProtected = nbt.getBoolean("isProtected");
//        if (nbt.contains("tick"))
//            tick = nbt.getInt("tick");
//        if (nbt.contains("color"))
//            color = new DisplayColor(nbt.getCompound("color"));
//        if(nbt.contains("redstone"))
//            redstone = nbt.getBoolean("redstone");
//        if(nbt.contains("prepaidValue"))
//            prepaidValue = nbt.getLong("prepaidValue");
//    }
//
//    public class Avg {
//        int size=10;
//        long[] tab= new long[size];
//        int index = 0;
//
//        public void putValue(long lastRecive){
//            tab[index] = lastRecive;
//            index = (index+1) % tab.length;
//        }
//
//        public float getAvg(){
//            long sum = 0;
//            for(long i: tab)
//                sum += i;
//            return (float)sum/(float)tab.length;
//        }
//    }
}
