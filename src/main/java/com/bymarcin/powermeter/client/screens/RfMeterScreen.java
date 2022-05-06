package com.bymarcin.powermeter.client.screens;

import com.bymarcin.powermeter.RfMeterLogic;
import com.bymarcin.powermeter.RfMeterMod;
import com.bymarcin.powermeter.containers.RfMeterContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class RfMeterScreen extends AbstractContainerScreen<RfMeterContainer> {

    public RfMeterScreen(RfMeterContainer container, Inventory inventory, Component name) {
        super(container, inventory, name);
    }

    class ColorSlider extends ForgeSlider {
        RfMeterLogic.RgbColor color;
        RGB rgb;
        enum RGB {
            R,G,B
        }
        public ColorSlider(RfMeterLogic.RgbColor color, RGB rgb, int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString) {
            super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, stepSize, precision, drawString);
            this.color = color;
            this.rgb = rgb;
        }

        @Override
        protected void updateMessage() {
            super.updateMessage();
            if(rgb == null) return;
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
        }
    }

    @Override
    protected void init() {
        super.init();
        var entity = getMenu().getEntity();
        if (entity != null) {
            var color = entity.logic.getColor();

            int height = 20;
            int space = height + 1;
            int startY = 10;
            addRenderableWidget(new ColorSlider(color, ColorSlider.RGB.R,10, startY, 200, 20, TextComponent.EMPTY, new TextComponent(" R"),
                    0, 255, color.r * 255, 1, 0, true));
            addRenderableWidget(new ColorSlider(color, ColorSlider.RGB.G,10, startY + space, 200, 20, TextComponent.EMPTY, new TextComponent(" G"),
                    0, 255, color.g * 255, 1, 0, true));
            addRenderableWidget(new ColorSlider(color, ColorSlider.RGB.B, 10, startY + space * 2, 200, 20, TextComponent.EMPTY, new TextComponent(" B"),
                    0, 255, color.b * 255, 1, 0, true));
        }
    }

    @Override
    public void render(PoseStack stack, int p_97796_, int p_97797_, float p_97798_) {
        super.render(stack, p_97796_, p_97797_, p_97798_);
        stack.pushPose();

        stack.popPose();
    }

    @Override
    protected void renderBg(PoseStack stack, float p_97788_, int p_97789_, int p_97790_) {

    }
}
