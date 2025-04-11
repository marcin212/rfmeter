package com.utilitymeters.powermeter.client;

import com.utilitymeters.powermeter.client.rendering.BaseMeterBlockEntityRenderer;
import com.utilitymeters.powermeter.client.screens.screens.BaseMeterScreen;
import com.utilitymeters.powermeter.registry.RfMeterBlockEntities;
import com.utilitymeters.powermeter.registry.RfMeterContainers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(RfMeterContainers.BASE_METER.get(), BaseMeterScreen::new);
            BlockEntityRenderers.register(RfMeterBlockEntities.RF_METER.get(), BaseMeterBlockEntityRenderer::new);
            BlockEntityRenderers.register(RfMeterBlockEntities.ITEM_METER.get(), BaseMeterBlockEntityRenderer::new);
            BlockEntityRenderers.register(RfMeterBlockEntities.FLUID_METER.get(), BaseMeterBlockEntityRenderer::new);
        });
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.BakingCompleted  e) {

    }

}
