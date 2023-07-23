package com.bymarcin.powermeter.network;

import com.bymarcin.powermeter.blockentity.RfMeterBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handleRfMeterSyncPacket(RfMeterSyncPacket rfMeterSyncPacket, Supplier<NetworkEvent.Context> contextSupplier) {
        Level level = net.minecraft.client.Minecraft.getInstance().level;
        if (level == null) return;
        var entity = level.getBlockEntity(rfMeterSyncPacket.getPos());
        if (entity instanceof RfMeterBlockEntity tile) {
            tile.logic.onRfMeterSyncPacket(rfMeterSyncPacket);
        }
    }
}
