package com.bymarcin.powermeter;


import com.bymarcin.powermeter.blockentity.RfMeterBlockEntity;
import com.bymarcin.powermeter.blocks.RfMeterBlock;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RfMeterLogic {

    public class RgbColor {
        public final float r;
        public final float g;
        public final float b;

        public RgbColor(CompoundTag tag) {
            this(getOrDefault(tag, "r", DyeColor.LIME.getTextureDiffuseColors()[0]),
                    getOrDefault(tag, "g", DyeColor.LIME.getTextureDiffuseColors()[1]),
                    getOrDefault(tag, "b", DyeColor.LIME.getTextureDiffuseColors()[2]));
        }

        private static float getOrDefault(CompoundTag tag, String color, float defaultColor) {
            if (tag.contains(color)) {
                return tag.getFloat(color);
            }
            return defaultColor;
        }

        public RgbColor(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public RgbColor(float[] color) {
            this(color[0], color[1], color[2]);
        }

        public RgbColor(DyeColor dyeColor) {
            this(dyeColor.getTextureDiffuseColors());
        }

        public CompoundTag save() {
            var tag = new CompoundTag();
            tag.putFloat("r", r);
            tag.putFloat("g", g);
            tag.putFloat("b", b);
            return tag;
        }
    }
    int transfer = 0;//curent flow in RF/t
    int transferLimit = -1;
    long value = 0;//current used energy
    long lastValue = 0;
    Avg avg = new Avg();
    String name = "";
    String password = "";
    boolean inCounterMode = true;
    boolean isOn = true;
    boolean isProtected = false;
    boolean redstone = false;

    int tick = 0;

    public RgbColor color = new RgbColor(DyeColor.LIME);
    ForgeEnergy up;
    ForgeEnergy down;
    LazyOptional<ForgeEnergy> energyStorageUp;
    LazyOptional<ForgeEnergy> energyStorageDown;

    RfMeterBlockEntity entity;
    
    public RfMeterLogic(RfMeterBlockEntity entity) {
        this.entity = entity;
        this.up = new ForgeEnergy(this, Direction.UP);
        this.down = new ForgeEnergy(this, Direction.DOWN);
        energyStorageUp = LazyOptional.of(()->up);
        energyStorageDown = LazyOptional.of(()->down);
    }

    class ForgeEnergy implements IEnergyStorage {
        RfMeterLogic rfMeterLogic;
        Direction face;

        public ForgeEnergy(RfMeterLogic rfMeterLogic, Direction face) {
            this.rfMeterLogic = rfMeterLogic;
            this.face = face;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return rfMeterLogic.receiveEnergy(face, maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return rfMeterLogic.extractEnergy(face, maxExtract, simulate);
        }

        @Override
        public int getEnergyStored() {
            return rfMeterLogic.getEnergyStored(face);
        }

        @Override
        public int getMaxEnergyStored() {
            return rfMeterLogic.getMaxEnergyStored(face);
        }

        @Override
        public boolean canExtract() {
            return rfMeterLogic.canConnectEnergy(face, false);
        }

        @Override
        public boolean canReceive() {
            return rfMeterLogic.canConnectEnergy(face, true);
        }
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (CapabilityEnergy.ENERGY.equals(cap)) {
            if (side == Direction.UP) {
                return energyStorageUp.cast();
            } else if (side == Direction.DOWN) {
                return energyStorageDown.cast();
            }
        }
        return LazyOptional.empty();
    }

    protected boolean canConnectEnergy(Direction from, boolean receive) {
        return (receive ^ isInverted()) ? (from == Direction.UP) : (from == Direction.DOWN);
    }

    protected int getEnergyStored(Direction from) {
        return 0;
    }

    protected int getMaxEnergyStored(Direction from) {
        return 10000;
    }

    public boolean isInverted() {
        return entity.getBlockState().getValue(RfMeterBlock.FLOW_DIRECTION) == RfMeterBlock.FlowDirection.DOWN_UP;
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

    public boolean canEnergyFlow() {
        return isOn && (inCounterMode || (0 < value)) && !redstone;
    }

    private int checkRedstone(){
        Direction front = entity.getBlockState().getValue(RfMeterBlock.FACING);
        if(entity.hasLevel()) {
            return entity.getLevel().getSignal(entity.getBlockPos().relative(front.getOpposite()), front);
        }
        return 0;
    }

    public void updateRedstone(){
        redstone = 0!=checkRedstone();
    }


    public void onPacket(long value, int transfer, boolean inCounterMode) {
        this.value = value;
        this.transfer = transfer;
        this.inCounterMode = inCounterMode;
    }


    public void serverTick() {
        tick++;
        if (tick % 20 == 0) {
            var packet = new PacketHandler.ClientSyncPacket(entity.getBlockPos(), transfer, value, inCounterMode);
            PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(()-> entity.getLevel().getChunkAt(entity.getBlockPos())), packet);
            tick = 0;
        }
        long lastRecive = Math.abs(value - lastValue);
        avg.putValue(lastRecive);
        transfer = (int) avg.getAvg();
        lastValue = value;
    }

    public void clientTick() {
        tick++;
        if (inCounterMode)
            value += transfer;
        else
            value -= transfer;
    }

    public IEnergyStorage getEnergyStorage(Direction direction){
        var energyEntity = entity.getLevel().getBlockEntity(entity.getBlockPos().relative(direction));
        if(energyEntity == null) return null;
        var capability = energyEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite());
        if(capability.isPresent()) {
            return capability.orElse(null);
        }
        return null;
    }

    protected int receiveEnergy(Direction from, int maxReceive, boolean simulate) {
        if (!canEnergyFlow()) return 0;
        int temp = 0;
        if (from == (isInverted() ? Direction.DOWN : Direction.UP)) {
            IEnergyStorage storage = getEnergyStorage(isInverted() ? Direction.UP : Direction.DOWN);
            if(storage!=null){
                temp = storage.receiveEnergy(transferLimit == -1 ?
                                (inCounterMode ? maxReceive : Math.min((int) value, maxReceive))
                                : Math.min(transferLimit, (inCounterMode ? maxReceive : Math.min((int) value, maxReceive)))
                        , simulate);

                if (!simulate) if (inCounterMode) value += temp;
                else value -= temp;
                return temp;
            }
        }
        return 0;
    }

    protected int extractEnergy(Direction from, int maxExtract, boolean simulate) {
        return 0;
    }

    public void getUpdateTag(CompoundTag nbt) {
        nbt.putInt("transferLimit", transferLimit);

        nbt.putLong("value", value);
        nbt.putLong("lastValue", lastValue);

        nbt.putString("name", name);
        nbt.putString("password", password);

        nbt.putBoolean("inCounterMode", inCounterMode);
        nbt.putBoolean("isOn", isOn);
        nbt.putBoolean("isProtected", isProtected);

        nbt.put("color", color.save());

        nbt.putBoolean("redstone", redstone);
    }

    public void handleUpdateTag(CompoundTag nbt) {
        if (nbt.contains("transferLimit"))
            transferLimit = nbt.getInt("transferLimit");
        if (nbt.contains("value"))
            value = nbt.getLong("value");
        if (nbt.contains("lastValue"))
            lastValue = nbt.getLong("lastValue");
        if (nbt.contains("name"))
            name = nbt.getString("name");
        if (nbt.contains("password"))
            password = nbt.getString("password");
        if (nbt.contains("inCounterMode"))
            inCounterMode = nbt.getBoolean("inCounterMode");
        if (nbt.contains("isOn"))
            isOn = nbt.getBoolean("isOn");
        if (nbt.contains("isProtected"))
            isProtected = nbt.getBoolean("isProtected");
        if (nbt.contains("color"))
            color = new RgbColor(nbt.getCompound("color"));
        if(nbt.contains("redstone")){
            redstone = nbt.getBoolean("redstone");
        }
    }

    public void saveAdditional(CompoundTag nbt) {
        nbt.putInt("transfer", transfer);
        nbt.putInt("transferLimit", transferLimit);

        nbt.putLong("value", value);
        nbt.putLong("lastValue", lastValue);

        nbt.putString("name", name);
        nbt.putString("password", password);

        nbt.putBoolean("inCounterMode", inCounterMode);
        nbt.putBoolean("isOn", isOn);
        nbt.putBoolean("isProtected", isProtected);

        nbt.putInt("tick", tick);

        nbt.put("color", color.save());

        nbt.putBoolean("redstone", redstone);
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
        if (nbt.contains("name"))
            name = nbt.getString("name");
        if (nbt.contains("password"))
            password = nbt.getString("password");
        if (nbt.contains("inCounterMode"))
            inCounterMode = nbt.getBoolean("inCounterMode");
        if (nbt.contains("isOn"))
            isOn = nbt.getBoolean("isOn");
        if (nbt.contains("isProtected"))
            isProtected = nbt.getBoolean("isProtected");
        if (nbt.contains("tick"))
            tick = nbt.getInt("tick");
        if (nbt.contains("color"))
            color = new RgbColor(nbt.getCompound("color"));
        if(nbt.contains("redstone"))
            redstone = nbt.getBoolean("redstone");
    }

    public class Avg {
        int size=10;
        long[] tab= new long[size];
        int index = 0;

        public void putValue(long lastRecive){
            tab[index] = lastRecive;
            index = (index+1) % tab.length;
        }

        public float getAvg(){
            long sum = 0;
            for(long i: tab)
                sum += i;
            return (float)sum/(float)tab.length;
        }
    }
}
