package com.bymarcin.powermeter.client.rendering;

import com.bymarcin.powermeter.RfMeterLogic;
import com.bymarcin.powermeter.RfMeterMod;
import com.bymarcin.powermeter.blockentity.RfMeterBlockEntity;
import com.bymarcin.powermeter.blocks.RfMeterBlock;
import com.bymarcin.powermeter.client.ClientSetup;
import com.bymarcin.powermeter.client.screens.wigets.SegDisplay;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class RfMeterRenderer implements BlockEntityRenderer<RfMeterBlockEntity> {
    SegDisplay segDisplayUp = new SegDisplay(6, 0, false);
    SegDisplay segDisplayDown = new SegDisplay(6, 2, true);
    SegDisplay.TextSegDisplay onOffText = new SegDisplay.TextSegDisplay("");
    SegDisplay.TextSegDisplay modeText = new SegDisplay.TextSegDisplay("");
    public RfMeterRenderer(Context context) {

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


        stack.pushPose();
        stack.translate(0.5f, 0.5f, 0f);
        stack.mulPose(Vector3f.ZP.rotationDegrees(180));
        stack.translate(-0.5f, -0.5f, 0f);
        stack.translate(4.15/16f, 2.10f/16f, 3 / 16f - 0.001f);
        stack.scale(1/425f, 1/425f, 1);
        segDisplayUp.render(stack, blockEntity.logic.getTransfer(), blockEntity.logic.color.r, blockEntity.logic.color.g, blockEntity.logic.color.b, blockEntity.logic.color.contrast, 0, 0);
        stack.popPose();



        stack.pushPose();
        stack.translate(0.5f, 0.5f, 0f);
        stack.mulPose(Vector3f.ZP.rotationDegrees(180));
        stack.translate(-0.5f, -0.5f, 0f);
        stack.translate(4.15 / 16f, 5.20 / 16f, 3 / 16f - 0.001f);
        stack.scale(1/425f, 1/425f, 1);
        segDisplayDown.render(stack, blockEntity.logic.isInCounterMode()?blockEntity.logic.getCurrentValue():blockEntity.logic.calculatedPrepaid(), blockEntity.logic.color.r, blockEntity.logic.color.g, blockEntity.logic.color.b, blockEntity.logic.color.contrast, 0, 0);
        stack.popPose();

        stack.pushPose();
        stack.translate(0.5f, 0.5f, 0f);
        stack.mulPose(Vector3f.ZP.rotationDegrees(180));
        stack.translate(-0.5f, -0.5f, 0f);

        stack.translate(4.15 / 16f, 7.20 / 16f, 3 / 16f - 0.001f);
        stack.scale(1/850f, 1/850f, 1);
        modeText.render(stack, blockEntity.logic.isInCounterMode()?"Counter":"Prepaid", blockEntity.logic.color.r, blockEntity.logic.color.g, blockEntity.logic.color.b, blockEntity.logic.color.contrast, 0, 0);
        stack.popPose();


        stack.pushPose();
        stack.translate(0.5f, 0.5f, 0f);
        stack.mulPose(Vector3f.ZP.rotationDegrees(180));
        stack.translate(-0.5f, -0.5f, 0f);

        stack.translate(10.0 / 16f, 7.20 / 16f, 3 / 16f - 0.001f);
        stack.scale(1/850f, 1/850f, 1);
        onOffText.render(stack, blockEntity.logic.isOn()?" On":"Off", blockEntity.logic.color.r, blockEntity.logic.color.g, blockEntity.logic.color.b, blockEntity.logic.color.contrast, 0, 0);
        stack.popPose();

        stack.popPose();
    }
}