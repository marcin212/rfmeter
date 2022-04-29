package com.bymarcin.powermeter;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;
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
                .messageBuilder(ClientSyncPacket.class, ++id, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientSyncPacket::decode)
                .encoder(ClientSyncPacket::encode)
                .consumer(ClientSyncPacket::handle)
                .add();
    }

    public static class ClientSyncPacket {
        private BlockPos pos;
        private long lastFlowValue;
        private long value;


        public ClientSyncPacket() {
        }

        public ClientSyncPacket(BlockPos pos, long lastFlowValue, long value) {
            this.pos = pos;
            this.lastFlowValue = lastFlowValue;
            this.value = value;
        }

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeBlockPos(pos);
            buffer.writeLong(lastFlowValue);
            buffer.writeLong(value);
        }

        public static ClientSyncPacket decode(FriendlyByteBuf buffer) {
            var packet = new ClientSyncPacket();
            packet.pos = buffer.readBlockPos();
            packet.lastFlowValue = buffer.readLong();
            packet.value = buffer.readLong();
            return packet;
        }

        public static void handle(ClientSyncPacket packet, Supplier<? extends Context> context) {
            context.get().enqueueWork(() -> ClientPacketHandler.handleClientSyncPacket(packet));
            context.get().setPacketHandled(true);
        }

        public BlockPos getPos() {
            return pos;
        }

        public long getLastFlowValue() {
            return lastFlowValue;
        }

        public long getValue() {
            return value;
        }
    }
}
