package com.utilitymeters.powermeter.network;

import com.utilitymeters.powermeter.blockentity.BaseMeterBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handleRfMeterSyncPacket(RfMeterSyncPacket rfMeterSyncPacket, Supplier<NetworkEvent.Context> contextSupplier) {
        Level level = net.minecraft.client.Minecraft.getInstance().level;
        if (level == null) return;
        var entity = level.getBlockEntity(rfMeterSyncPacket.getPos());
        if (entity instanceof BaseMeterBlockEntity tile) {
            tile.onRfMeterSyncPacket(rfMeterSyncPacket);
        }
    }
}
