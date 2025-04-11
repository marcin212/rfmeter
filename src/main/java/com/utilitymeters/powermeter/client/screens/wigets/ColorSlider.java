package com.utilitymeters.powermeter.client.screens.wigets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.utilitymeters.utils.DisplayColor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ColorSlider extends ForgeSlider {
    Supplier<DisplayColor> color;
    RGB rgb;
    Consumer<DisplayColor> onChange;

    public enum RGB {
        R, G, B, Contrast
    }


    public ColorSlider(Supplier<DisplayColor> color, RGB rgb, int x, int y, int width, int height,
                       Component prefix, Component suffix,
                       double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString,
                       Consumer<DisplayColor> onChange
    ) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, stepSize, precision, drawString);
        this.color = color;
        this.rgb = rgb;
        this.onChange = onChange;
    }

    @Override
    protected void updateMessage() {
        super.updateMessage();
        if (rgb == null) return;
        switch (rgb) {
            case R -> {
                color.get().r = (float) this.value;
            }
            case G -> {
                color.get().g = (float) this.value;
            }
            case B -> {
                color.get().b = (float) this.value;
            }
            case Contrast -> {
                color.get().contrast = (float) this.value;
            }
        }
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        onChange.accept(color.get());
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.isHoveredOrFocused() ? 2 : 1) * 20;
        pGuiGraphics.blit(WIDGETS_LOCATION,
                this.getX() + (int) (this.value * (double) (this.width - 8)), this.getY(), 4, this.height,
                0, 46 + i, 4, 20, 256, 256);
        pGuiGraphics.blit(WIDGETS_LOCATION,
                this.getX() + (int) (this.value * (double) (this.width - 8)) + 4, this.getY(), 4, this.height,
                196, 46 + i, 4, 20, 256, 256);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}