package com.bymarcin.powermeter.client.screens.wigets;

import com.bymarcin.powermeter.RfMeterLogic;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class CustomSlider extends ForgeSlider {
    private final DoubleConsumer onChange;

    public CustomSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString, DoubleConsumer onChange) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, stepSize, precision, drawString);
        this.onChange = onChange;
    }

    @Override
    protected void applyValue() {
        super.applyValue();
        this.onChange.accept(getValue());
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, Minecraft pMinecraft, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.isHoveredOrFocused() ? 2 : 1) * 20;
        this.blit(pPoseStack,
                this.x + (int) (this.value * (double) (this.width - 8)), this.y, 4, this.height,
                0, 46 + i, 4, 20, 256, 256);
        this.blit(pPoseStack,
                this.x + (int) (this.value * (double) (this.width - 8)) + 4, this.y, 4, this.height,
                196, 46 + i, 4, 20, 256, 256);

    }
}
