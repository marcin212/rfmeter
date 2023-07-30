package com.utilitymeters.powermeter.network;

import com.utilitymeters.powermeter.blockentity.RfMeterBlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerPacketHandler {
    public static void handleRfMeterSyncPacket(RfMeterSyncPacket rfMeterSyncPacket, Supplier<NetworkEvent.Context> contextSupplier) {
        var sender = contextSupplier.get().getSender();
        if (sender == null) return;
        var level = sender.level;
        var entity = level.getBlockEntity(rfMeterSyncPacket.getPos());
        if (entity instanceof RfMeterBlockEntity tile) {
            tile.logic.onRfMeterSyncPacket(rfMeterSyncPacket);
        }
    }
}
