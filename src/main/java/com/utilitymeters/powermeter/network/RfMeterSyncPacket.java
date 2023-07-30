package com.utilitymeters.powermeter.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class RfMeterSyncPacket {
    public enum ContentFlag {
        COLOR(1),
        PASSWORD(1<<1),
        VALUE(1<<2),
        TRANSFER_LIMIT(1<<3),
        COUNTER_MODE(1<<4),
        ON(1<<5),
        TRANSFER(1<<6),
        PREPAID_VALUE(1<<7),
        ;
        private final int flag;
        ContentFlag(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return flag;
        }

        public boolean hasFlag(int flags) {
            return (flags & getFlag()) > 0;
        }

        public int setFlag(int flags) {
            return flags | flag;
        }

    }
    public static class Builder<T extends RfMeterSyncPacket> {
        private final T packet;
        private int flag = 0;

        public Builder(BlockPos pos, Class<T> tClass) {
            if(tClass == RfMeterSyncC2SPacket.class) {
                packet = (T) new RfMeterSyncC2SPacket();
            } else {
                packet = (T) new RfMeterSyncS2CPacket();
            }
            packet.pos = pos;
        }
        public T build() {
            packet.flags = flag;
            return packet;
        }
        public Builder<T> addColor(float r, float g, float b, float contrast) {
            packet.r = r;
            packet.g = g;
            packet.b = b;
            packet.contrast = contrast;
            flag = ContentFlag.COLOR.setFlag(flag);
            return this;
        }

        public Builder<T> addPrepaidValue(long prepaidValue) {
            packet.prepaidValue = prepaidValue;
            flag = ContentFlag.PREPAID_VALUE.setFlag(flag);
            return this;
        }

        public Builder<T> addPassword(String password, boolean isProtected) {
            packet.password = password;
            packet.isProtected = isProtected;
            flag = ContentFlag.PASSWORD.setFlag(flag);
            return this;
        }

        public Builder<T> addValue(long value) {
            packet.value = value;
            flag = ContentFlag.VALUE.setFlag(flag);
            return this;
        }

        public Builder<T> addTransfer(int lastFlowValue) {
            packet.transfer = lastFlowValue;
            flag = ContentFlag.TRANSFER.setFlag(flag);
            return this;
        }

        public Builder<T> addTransferLimit(int transferLimit) {
            packet.transferLimit = transferLimit;
            flag = ContentFlag.TRANSFER_LIMIT.setFlag(flag);
            return this;
        }

        public Builder<T> addCounterMode(boolean counterMode) {
            packet.inCounterMode = counterMode;
            flag = ContentFlag.COUNTER_MODE.setFlag(flag);
            return this;
        }

        public Builder<T> addOn(boolean on) {
            packet.isOn = on;
            flag = ContentFlag.ON.setFlag(flag);
            return this;
        }

    }

    public BlockPos pos;
    public float r;
    public float g;
    public float b;
    public float contrast;

    public long value;
    public int transferLimit;
    public String password;
    public boolean isProtected;
    public boolean inCounterMode;
    public boolean isOn;
    public int transfer = 0;
    public int flags = 0;
    public long prepaidValue = 0;

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(flags);

        if (ContentFlag.COLOR.hasFlag(flags)) {
            buffer.writeFloat(r);
            buffer.writeFloat(g);
            buffer.writeFloat(b);
            buffer.writeFloat(contrast);
        }

        if (ContentFlag.VALUE.hasFlag(flags)) {
            buffer.writeLong(value);
        }

        if (ContentFlag.TRANSFER_LIMIT.hasFlag(flags)) {
            buffer.writeInt(transferLimit);
        }

        if (ContentFlag.PASSWORD.hasFlag(flags)) {
            var passwordBytes = password.getBytes();
            buffer.writeInt(passwordBytes.length);
            buffer.writeByteArray(passwordBytes);
            buffer.writeBoolean(isProtected);
        }

        if (ContentFlag.COUNTER_MODE.hasFlag(flags)) {
            buffer.writeBoolean(inCounterMode);
        }

        if (ContentFlag.ON.hasFlag(flags)) {
            buffer.writeBoolean(isOn);
        }

        if (ContentFlag.TRANSFER.hasFlag(flags)) {
            buffer.writeInt(transfer);
        }

        if(ContentFlag.PREPAID_VALUE.hasFlag(flags)) {
            buffer.writeLong(prepaidValue);
        }
    }

    public static <T extends RfMeterSyncPacket> T decode(FriendlyByteBuf buffer, Class<T> tClass) {
        T packet;
        if(tClass == RfMeterSyncC2SPacket.class) {
            packet = (T) new RfMeterSyncC2SPacket();
        } else {
            packet = (T) new RfMeterSyncS2CPacket();
        }

        packet.pos = buffer.readBlockPos();
        packet.flags = buffer.readInt();

        if(ContentFlag.COLOR.hasFlag(packet.flags)) {
            packet.r = buffer.readFloat();
            packet.g = buffer.readFloat();
            packet.b = buffer.readFloat();
            packet.contrast = buffer.readFloat();
        }

        if(ContentFlag.VALUE.hasFlag(packet.flags)) {
            packet.value = buffer.readLong();
        }

        if(ContentFlag.TRANSFER_LIMIT.hasFlag(packet.flags)) {
            packet.transferLimit = buffer.readInt();
        }

        if(ContentFlag.PASSWORD.hasFlag(packet.flags)) {
            var passwordLength = buffer.readInt();
            packet.password = new String(buffer.readByteArray(passwordLength));
            packet.isProtected = buffer.readBoolean();
        }

        if(ContentFlag.COUNTER_MODE.hasFlag(packet.flags)) {
            packet.inCounterMode = buffer.readBoolean();
        }

        if(ContentFlag.ON.hasFlag(packet.flags)) {
            packet.isOn = buffer.readBoolean();
        }

        if(ContentFlag.TRANSFER.hasFlag(packet.flags)) {
            packet.transfer = buffer.readInt();
        }

        if(ContentFlag.PREPAID_VALUE.hasFlag(packet.flags)) {
            packet.prepaidValue = buffer.readLong();
        }

        return packet;
    }

    public BlockPos getPos() {
        return pos;
    }
}
