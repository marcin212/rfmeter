package com.utilitymeters.powermeter.client.screens.wigets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

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
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.isHoveredOrFocused() ? 2 : 1) * 20;
        pGuiGraphics.blit(WIDGETS_LOCATION, this.getX() + (int) (this.value * (double) (this.width - 8)), this.getY(), 4, this.height,
                0, 46 + i, 4, 20, 256, 256);
        pGuiGraphics.blit(WIDGETS_LOCATION, this.getX() + (int) (this.value * (double) (this.width - 8)) + 4, this.getY(), 4, this.height,
                196, 46 + i, 4, 20, 256, 256);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
