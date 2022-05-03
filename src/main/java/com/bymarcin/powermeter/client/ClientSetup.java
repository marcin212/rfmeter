package com.bymarcin.powermeter.client;

import com.bymarcin.powermeter.client.rendering.RfMeterRenderer;
import com.bymarcin.powermeter.client.screens.RfMeterScreen;
import com.bymarcin.powermeter.registry.RfMeterBlockEntities;
import com.bymarcin.powermeter.registry.RfMeterContainers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static final Map<ResourceLocation, BakedModel> MODEL_MAP = new HashMap<>();
    public static final ResourceLocation RF_METER_DIGIT_0_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_digit_0");
    public static final ResourceLocation RF_METER_DIGIT_1_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_digit_1");
    public static final ResourceLocation RF_METER_DIGIT_2_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_digit_2");
    public static final ResourceLocation RF_METER_DIGIT_3_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_digit_3");
    public static final ResourceLocation RF_METER_DIGIT_4_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_digit_4");
    public static final ResourceLocation RF_METER_DIGIT_5_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_digit_5");
    public static final ResourceLocation RF_METER_DIGIT_6_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_digit_6");
    public static final ResourceLocation RF_METER_DIGIT_7_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_digit_7");
    public static final ResourceLocation RF_METER_DIGIT_8_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_digit_8");
    public static final ResourceLocation RF_METER_DIGIT_9_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_digit_9");
    public static final ResourceLocation RF_METER_LETTER_K_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_letter_k");
    public static final ResourceLocation RF_METER_LETTER_M_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_letter_m");
    public static final ResourceLocation RF_METER_LETTER_G_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_letter_g");
    public static final ResourceLocation RF_METER_LETTER_T_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_letter_t");
    public static final ResourceLocation RF_METER_LETTER_P_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_letter_p");
    public static final ResourceLocation RF_METER_TEXT_VOID_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_text_void");
    public static final ResourceLocation RF_METER_TEXT_COUNTER_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_text_counter");
    public static final ResourceLocation RF_METER_TEXT_PREPAID_MODEL_RL = new ResourceLocation("rfmeter:block/rfmeter_text_prepaid");

    public static final ResourceLocation[] DIGIT_TO_RL = new ResourceLocation[] {
            RF_METER_DIGIT_0_MODEL_RL,
            RF_METER_DIGIT_1_MODEL_RL,
            RF_METER_DIGIT_2_MODEL_RL,
            RF_METER_DIGIT_3_MODEL_RL,
            RF_METER_DIGIT_4_MODEL_RL,
            RF_METER_DIGIT_5_MODEL_RL,
            RF_METER_DIGIT_6_MODEL_RL,
            RF_METER_DIGIT_7_MODEL_RL,
            RF_METER_DIGIT_8_MODEL_RL,
            RF_METER_DIGIT_9_MODEL_RL
    };

    public static final ResourceLocation[] SI_TO_RL = new ResourceLocation[] {
            RF_METER_LETTER_K_MODEL_RL,
            RF_METER_LETTER_M_MODEL_RL,
            RF_METER_LETTER_G_MODEL_RL,
            RF_METER_LETTER_T_MODEL_RL,
            RF_METER_LETTER_P_MODEL_RL
    };

    public static final ResourceLocation[] TEXT_TO_RL = new ResourceLocation[] {
            RF_METER_TEXT_VOID_MODEL_RL,
            RF_METER_TEXT_COUNTER_MODEL_RL,
            RF_METER_TEXT_PREPAID_MODEL_RL
    };

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(RfMeterContainers.RF_METER.get(), RfMeterScreen::new);
            BlockEntityRenderers.register(RfMeterBlockEntities.RF_METER.get(), RfMeterRenderer::new);
        });
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent e) {
        for(ResourceLocation rl : DIGIT_TO_RL) {
            loadModel(e, rl);
        }
        for(ResourceLocation rl : SI_TO_RL) {
            loadModel(e, rl);
        }
        for(ResourceLocation rl : TEXT_TO_RL) {
            loadModel(e, rl);
        }
    }

    private static void loadModel(ModelBakeEvent e, ResourceLocation rl) {
        var model = e.getModelLoader().getModel(rl);
        var bakedModel = model.bake(e.getModelLoader(), Material::sprite, SimpleModelState.IDENTITY, rl);
        MODEL_MAP.put(rl, bakedModel);
    }
}
