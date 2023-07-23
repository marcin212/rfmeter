package com.bymarcin.powermeter.network;

import com.bymarcin.powermeter.RfMeterMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PacketHandler {
    private static final ResourceLocation ID = new ResourceLocation("network");
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ID,
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );


    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        // initialize packet handler
        RfMeterMod.LOGGER.debug("Initialize packet handler.");
        init();
    }

    public static void init() {
        var id = -1;

        CHANNEL
                .messageBuilder(RfMeterSyncS2CPacket.class, ++id, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RfMeterSyncS2CPacket::decode)
                .encoder(RfMeterSyncS2CPacket::encode)
                .consumerMainThread(PacketHandler::handleRfMeterSyncPacketOnClient)
                .add();
        CHANNEL
                .messageBuilder(RfMeterSyncC2SPacket.class, ++id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(RfMeterSyncC2SPacket::decode)
                .encoder(RfMeterSyncC2SPacket::encode)
                .consumerMainThread(PacketHandler::handleRfMeterSyncPacketOnServer)
                .add();
    }

    public static void handleRfMeterSyncPacketOnClient(RfMeterSyncPacket rfMeterSyncPacket, Supplier<NetworkEvent.Context> contextSupplier) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()-> ()-> ClientPacketHandler.handleRfMeterSyncPacket(rfMeterSyncPacket, contextSupplier));
    }

    public static void handleRfMeterSyncPacketOnServer(RfMeterSyncPacket rfMeterSyncPacket, Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPacketHandler.handleRfMeterSyncPacket(rfMeterSyncPacket, contextSupplier);
    }
}
