package com.bymarcin.powermeter.client.rendering;

import com.bymarcin.powermeter.RfMeterLogic;
import com.bymarcin.powermeter.RfMeterMod;
import com.bymarcin.powermeter.blockentity.RfMeterBlockEntity;
import com.bymarcin.powermeter.blocks.RfMeterBlock;
import com.bymarcin.powermeter.client.ClientSetup;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class RfMeterRenderer implements BlockEntityRenderer<RfMeterBlockEntity> {

    public RfMeterRenderer(Context context) {

    }

    public void renderNumber(long number, boolean dot, SI si, PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay, RfMeterLogic.RgbColor color) {
        poseStack.pushPose();
        float numberOffset = 0.75f;
        if (si != null && si.hasModel()) {
            Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(poseStack.last(), vertexConsumer, null, ClientSetup.MODEL_MAP.get(si.getModel()), color.r, color.g, color.b, light, overlay);
            poseStack.translate(numberOffset / 16f, 0f, 0f);
        }

        for (int i = 0; number != 0 || i < (dot ? 2 : 1); number = number / 10, i++) {
            int dig = (int) (number % 10);

            Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(poseStack.last(), vertexConsumer, null, ClientSetup.MODEL_MAP.get(ClientSetup.DIGIT_TO_RL[dig]), color.r, color.g, color.b, light, overlay);
            poseStack.translate(numberOffset / 16f, 0f, 0f);

            if (i == 0) {
                if (dot) {
                    poseStack.translate(numberOffset / 5 / 2 / 16f, 0f, 0f);
                    Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(poseStack.last(), vertexConsumer, null, ClientSetup.MODEL_MAP.get(ClientSetup.RF_METER_DOT_MODEL_RL), color.r, color.g, color.b, light, overlay);
                    poseStack.translate((numberOffset / 5) / 16f, 0f, 0f);
                }
            }
        }
        poseStack.popPose();
    }

    public void renderUnitMultiplication(SI letter, PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay) {
        if (letter.hasModel()) {
            poseStack.pushPose();
            Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(poseStack.last(), vertexConsumer, null, ClientSetup.MODEL_MAP.get(letter.getModel()), 0f, 1f, 0f, light, overlay);
            poseStack.popPose();
        }
    }

    public void renderText(Text text, Status status, PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay) {
        poseStack.pushPose();
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(poseStack.last(), vertexConsumer, null, ClientSetup.MODEL_MAP.get(ClientSetup.TEXT_TO_RL[text.ordinal()]), status.r, status.g, status.b, light, overlay);
        poseStack.popPose();
    }

    static class Total {
        SI unit;
        long value;
    }

    Total total = new Total();

    public void updateTotal(RfMeterBlockEntity blockEntity) {
        long total = blockEntity.logic.getCurrentValue();
        int unit = 0;
        total *= 10;
        while (Math.ceil(Math.log10(total)) > 6) {
            unit++;
            total /= 1000;
        }

        this.total.value = total;
        this.total.unit = SI.reverse[unit];
    }

    @Override
    public void render(RfMeterBlockEntity blockEntity, float partial, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay) {
        stack.pushPose();

        var facing = blockEntity.getBlockState().getValue(RfMeterBlock.FACING);

        stack.translate(0.5f, 0.5f, 0.5);
        switch (facing) {
            case EAST -> stack.mulPose(Vector3f.YP.rotationDegrees(270));
            case NORTH -> stack.mulPose(Vector3f.YP.rotationDegrees(0));
            case SOUTH -> stack.mulPose(Vector3f.YP.rotationDegrees(180));
            case WEST -> stack.mulPose(Vector3f.YP.rotationDegrees(90));
        }
        stack.translate(-0.5f, -0.5f, -0.5);
        VertexConsumer vertex = bufferSource.getBuffer(RenderType.solid());

        long number = blockEntity.logic.getTransfer();
        RfMeterLogic.RgbColor color = blockEntity.logic.getColor();
        updateTotal(blockEntity);


        stack.pushPose();
        stack.translate(4 / 16f, 12.25 / 16f, 3 / 16f - 0.001f);
        renderNumber(number, false, null, stack, vertex, light, overlay, color);
        stack.popPose();

        stack.pushPose();
        stack.translate(4 / 16f, 9.5 / 16f, 3 / 16f - 0.001f);
        renderNumber(total.value, true, total.unit, stack, vertex, light, overlay, color);
        stack.popPose();

//        stack.pushPose();
//        stack.translate(4/16f, 8.25/16f, 3/16f - 0.001f);
//        renderUnitMultiplication(total.unit, stack, vertex, light, overlay);
//        stack.popPose();

        // void, counter, prepaid texts
        stack.pushPose();
        stack.translate(10.5 / 16f, 8.25 / 16f, 3 / 16f - 0.001f);
        renderText(Text.Void, Status.OFF, stack, vertex, light, overlay);
        stack.popPose();
        stack.pushPose();
        stack.translate(7.7 / 16f, 8.25 / 16f, 3 / 16f - 0.001f);
        renderText(Text.Counter, blockEntity.logic.isInCounterMode() ? Status.ON : Status.OFF, stack, vertex, light, overlay);
        stack.popPose();
        stack.pushPose();
        stack.translate(5 / 16f, 8.25 / 16f, 3 / 16f - 0.001f);
        renderText(Text.Prepaid, blockEntity.logic.isInCounterMode() ? Status.OFF : Status.ON, stack, vertex, light, overlay);
        stack.popPose();

        stack.popPose();
    }

    enum Text {
        Void,
        Counter,
        Prepaid
    }

    enum Status {
        OFF(.41f, .41f, .41f),
        ON(0, 1f, 0),
        ALMOST_EXPIRED(1f, .84f, 0),
        EXPIRED(1f, 0, 0);

        float r, g, b;

        Status(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    enum SI {
        P(ClientSetup.RF_METER_LETTER_P_MODEL_RL),
        T(ClientSetup.RF_METER_LETTER_T_MODEL_RL),
        G(ClientSetup.RF_METER_LETTER_G_MODEL_RL),
        M(ClientSetup.RF_METER_LETTER_M_MODEL_RL),
        K(ClientSetup.RF_METER_LETTER_K_MODEL_RL),
        none(null);
        public static final SI[] reverse = valuesReverse();
        private final ResourceLocation modelLocation;

        SI(ResourceLocation modelLocation) {
            this.modelLocation = modelLocation;
        }

        public ResourceLocation getModel() {
            return modelLocation;
        }

        public boolean hasModel() {
            return modelLocation != null;
        }

        private static SI[] valuesReverse() {
            int len = SI.values().length;
            SI[] arr = new SI[len];
            for (SI si : values()) {
                arr[len - si.ordinal() - 1] = si;
            }
            return arr;
        }
    }
}