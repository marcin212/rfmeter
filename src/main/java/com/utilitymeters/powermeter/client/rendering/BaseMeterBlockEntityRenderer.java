package com.utilitymeters.powermeter.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.utilitymeters.powermeter.blockentity.BaseMeterBlockEntity;
import com.utilitymeters.powermeter.blocks.RfMeterBlock;
import com.utilitymeters.powermeter.client.screens.wigets.SegDisplay;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.joml.Math;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;

public class BaseMeterBlockEntityRenderer implements BlockEntityRenderer<BaseMeterBlockEntity> {
    SegDisplay segDisplayUp = new SegDisplay(6, 0, false);
    SegDisplay segDisplayDown = new SegDisplay(6, 2, true);
    SegDisplay.TextSegDisplay onOffText = new SegDisplay.TextSegDisplay("");
    SegDisplay.TextSegDisplay modeText = new SegDisplay.TextSegDisplay("");

    private static final Map<Direction, Quaternionf> facingRotationMap = new HashMap<>();
    private static final Quaternionf rot180ZP = new Quaternionf().rotateZ(org.joml.Math.toRadians(180));

    static {
        facingRotationMap.put(Direction.EAST, new Quaternionf().rotateY(org.joml.Math.toRadians(270)));
        facingRotationMap.put(Direction.NORTH, new Quaternionf().rotateY(org.joml.Math.toRadians(0)));
        facingRotationMap.put(Direction.SOUTH, new Quaternionf().rotateY(org.joml.Math.toRadians(180)));
        facingRotationMap.put(Direction.WEST, new Quaternionf().rotateY(Math.toRadians(90)));
    }

    public BaseMeterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(BaseMeterBlockEntity blockEntity, float partial, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay) {
        stack.pushPose();

        var facing = blockEntity.getBlockState().getValue(RfMeterBlock.FACING);

        stack.translate(0.5f, 0.5f, 0.5);


        if(facingRotationMap.containsKey(facing)) {
            stack.mulPose(facingRotationMap.get(facing));
        }
        stack.translate(-0.5f, -0.5f, -0.5);

        var displayColor = blockEntity.displayColors();
        var counter = blockEntity.counter();
        stack.pushPose();
        stack.translate(0.5f, 0.5f, 0f);
        stack.mulPose(rot180ZP);
        stack.translate(-0.5f, -0.5f, 0f);
        stack.translate(4.15/16f, 2.10f/16f, 3 / 16f - 0.001f);
        stack.scale(1/425f, 1/425f, 1);
        segDisplayUp.render(stack, counter.getTransfer(), displayColor.r, displayColor.g, displayColor.b, displayColor.contrast, 0, 0, bufferSource);
        stack.popPose();



        stack.pushPose();
        stack.translate(0.5f, 0.5f, 0f);
        stack.mulPose(rot180ZP);
        stack.translate(-0.5f, -0.5f, 0f);
        stack.translate(4.15 / 16f, 5.20 / 16f, 3 / 16f - 0.001f);
        stack.scale(1/425f, 1/425f, 1);
        segDisplayDown.render(stack, counter.isInCounterMode()?counter.getCurrentValue():counter.calculatedPrepaid(), displayColor.r, displayColor.g, displayColor.b, displayColor.contrast, 0, 0, bufferSource);
        stack.popPose();

        stack.pushPose();
        stack.translate(0.5f, 0.5f, 0f);
        stack.mulPose(rot180ZP);
        stack.translate(-0.5f, -0.5f, 0f);

        stack.translate(4.15 / 16f, 7.20 / 16f, 3 / 16f - 0.001f);
        stack.scale(1/850f, 1/850f, 1);
        modeText.render(stack, counter.isInCounterMode()?"Counter":"Prepaid", displayColor.r, displayColor.g, displayColor.b, displayColor.contrast, 0, 0, bufferSource);
        stack.popPose();


        stack.pushPose();
        stack.translate(0.5f, 0.5f, 0f);
        stack.mulPose(rot180ZP);
        stack.translate(-0.5f, -0.5f, 0f);

        stack.translate(10.0 / 16f, 7.20 / 16f, 3 / 16f - 0.001f);
        stack.scale(1/850f, 1/850f, 1);
        onOffText.render(stack, counter.isOn()?" On":"Off", displayColor.r, displayColor.g, displayColor.b, displayColor.contrast, 0, 0, bufferSource);
        stack.popPose();

        stack.popPose();
    }
}
