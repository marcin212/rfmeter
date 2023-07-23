package com.bymarcin.powermeter.client;

import com.bymarcin.powermeter.client.rendering.RfMeterRenderer;
import com.bymarcin.powermeter.client.screens.RfMeterScreen;
import com.bymarcin.powermeter.registry.RfMeterBlockEntities;
import com.bymarcin.powermeter.registry.RfMeterContainers;
import com.mojang.math.Transformation;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(RfMeterContainers.RF_METER.get(), RfMeterScreen::new);
            BlockEntityRenderers.register(RfMeterBlockEntities.RF_METER.get(), RfMeterRenderer::new);
        });
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.BakingCompleted  e) {

    }

}
