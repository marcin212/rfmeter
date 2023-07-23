package com.bymarcin.powermeter.network;

import net.minecraft.network.FriendlyByteBuf;

public class RfMeterSyncC2SPacket extends RfMeterSyncPacket {

    public RfMeterSyncS2CPacket convert() {
        RfMeterSyncS2CPacket p = new RfMeterSyncS2CPacket();
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

    public static RfMeterSyncC2SPacket decode(FriendlyByteBuf buffer) {
        return RfMeterSyncPacket.decode(buffer, RfMeterSyncC2SPacket.class);
    }
}
