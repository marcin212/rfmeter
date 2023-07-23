package com.bymarcin.powermeter.client.screens.wigets;

import com.bymarcin.powermeter.RfMeterLogic;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import java.util.function.Consumer;

public class ColorSlider extends ForgeSlider {
    RfMeterLogic.DisplayColor color;
    RGB rgb;
    Consumer<RfMeterLogic.DisplayColor> onChange;

    public enum RGB {
        R, G, B
    }

    public ColorSlider(RfMeterLogic.DisplayColor color, RGB rgb, int x, int y, int width, int height,
                       Component prefix, Component suffix,
                       double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString,
                       Consumer<RfMeterLogic.DisplayColor> onChange
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
                color.r = (float) this.value;
            }
            case G -> {
                color.g = (float) this.value;
            }
            case B -> {
                color.b = (float) this.value;
            }
        }
        onChange.accept(color);
    }

    @Override
    public void render(PoseStack p_93657_, int p_93658_, int p_93659_, float p_93660_) {
        super.render(p_93657_, p_93658_, p_93659_, p_93660_);
    }

    protected void renderBg(PoseStack p_93600_, Minecraft p_93601_, int p_93602_, int p_93603_) {
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.isHoveredOrFocused() ? 2 : 1) * 20;
        this.blit(p_93600_,
                this.x + (int) (this.value * (double) (this.width - 8)), this.y, 4, this.height,
                0, 46 + i,  4,20 , 256, 256);
        this.blit(p_93600_,
                this.x + (int) (this.value * (double) (this.width - 8)) + 4 , this.y, 4, this.height,
                196, 46 + i,  4,20 , 256, 256);

    }
}