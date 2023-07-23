package com.bymarcin.powermeter.network;

import com.bymarcin.powermeter.blockentity.RfMeterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class RfMeterSyncS2CPacket extends RfMeterSyncPacket{

    public RfMeterSyncC2SPacket convert() {
        RfMeterSyncC2SPacket p = new RfMeterSyncC2SPacket();
        p.pos = this.pos;
        p.r = this.r;
        p.g = this.g;
        p.b = this.b;
        p.contrast = this.contrast;
        p.value = this.value;
        p.transferLimit = this.transferLimit;
        p.password = this.password;
        p.isProtected = this.isProtected;
        p.inCounterMode = this.inCounterMode;
        p.isOn = this.isOn;
        p.transfer = this.transfer;
        p.prepaidValue = this.prepaidValue;

        p.flags = this.flags;
        return p;
    }

    public static RfMeterSyncS2CPacket decode(FriendlyByteBuf buffer) {
        return decode(buffer, RfMeterSyncS2CPacket.class);
    }
}
