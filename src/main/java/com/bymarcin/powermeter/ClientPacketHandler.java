package com.bymarcin.powermeter;

import com.bymarcin.powermeter.blockentity.RfMeterBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class ClientPacketHandler {
    static void handleClientSyncPacket(PacketHandler.ClientSyncPacket packet){
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        var entity = level.getBlockEntity(packet.getPos());
        if (entity instanceof RfMeterBlockEntity tile) {
            tile.logic.onPacket(packet.getValue(), packet.getLastFlowValue(), packet.getInCounterMode());
        }
    }
}
