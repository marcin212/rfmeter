package com.bymarcin.powermeter.client.screens.wigets;

import com.bymarcin.powermeter.MathUtils;
import com.bymarcin.powermeter.RfMeterMod;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class SegDisplay {
    private static final ResourceLocation fontRL = new ResourceLocation(RfMeterMod.MODID, "seg_14");
    public static final int bgHeight = 50;
    private static final int alpha = 255;
    private static final String[] metricPrefix = {"", "K", "M", "G", "T", "P", "E"};
    private final int segCount;
    private final int precision;
    private final int significant;
    private final boolean withMetricPrefix;

    private long lastValue = 0;
    private SegDisplayNum displayValue;

    private String bgText;


    public static class TextSegDisplay {
        private MutableComponent text;
        private MutableComponent textBg;
        private int textWidth;

        public TextSegDisplay(String text) {
            this.text = Component.literal(text);
        }

        private float r;
        private float g;
        private float b;
        private float contrast;

        private String oldText;

        public void render(PoseStack stack, String newText, float r, float g, float b, float contrast, float x, float y) {
            if (this.r != r || this.g != g || this.b != b || this.contrast != contrast || !newText.equals(oldText)) {
                var fontColor = MathUtils.toRGB(r, g, b);
                var fontColorDark = MathUtils.toRGB(r * contrast, g * contrast, b * contrast);
                text = Component.literal(newText).withStyle(Style.EMPTY.withFont(fontRL).withColor(fontColor));
                textBg = Component.literal("@".repeat(newText.length())).withStyle(Style.EMPTY.withFont(fontRL).withColor(fontColorDark));
                textWidth = Minecraft.getInstance().font.width(textBg);
                this.r = r;
                this.g = g;
                this.b = b;
                this.contrast = contrast;
                this.oldText = newText;
            }

            fill(stack, x - 5, y - 5, textWidth + 5, bgHeight + 5, 0, 0, 0);
            Minecraft.getInstance().font.draw(stack, textBg, x, y, 0);
            Minecraft.getInstance().font.draw(stack, text, x, y, 0);
        }
    }

    public SegDisplay(int segCount, int precision, boolean withMetricPrefix) {
        this.segCount = segCount;
        this.precision = precision;
        this.withMetricPrefix = withMetricPrefix;
        var significant = segCount - precision;
        if (withMetricPrefix) significant--;
        this.significant = significant;
        this.displayValue = convertToDisplayNum(0);
        this.bgText = "@".repeat(segCount);
    }

    public record SegDisplayNum(String numberText, String dotText) {

    }

    public SegDisplayNum convertToDisplayNum(long value) {
        long displayValue = value;
        var displayMetricPrefix = 0;
        while (Math.ceil(Math.log10(displayValue)) > significant && withMetricPrefix) {
            ++displayMetricPrefix;
            displayValue /= 1000L;
        }

        var calculatedSignificandString = String.valueOf(displayValue);
        var calculatedPrecision = new StringBuilder(String.valueOf(value)).delete(0, displayValue==0?0:calculatedSignificandString.length());

        while (calculatedPrecision.length() < precision) {
            calculatedPrecision.append("0");
        }
        var calculatedPrecisionString = precision>0?calculatedPrecision.substring(0, precision):"";

        var postfix = calculatedPrecisionString + metricPrefix[displayMetricPrefix];
        var displayText = calculatedSignificandString + postfix;

        if(displayText.length()>segCount) {
            return new SegDisplayNum("-".repeat(segCount), "");
        } else {
            displayText = " ".repeat(segCount - displayText.length()) + displayText;
        }

        return new SegDisplayNum(displayText, " ".repeat(segCount-postfix.length()-1) + ".");
    }

    public void render(PoseStack stack, long value, float r, float g, float b, float contrast, float x, float y) {
        if(lastValue != value) {
            lastValue = value;
            displayValue = convertToDisplayNum(value);
        }


        //9,223,372,036,854,775,807
        //E  P   T   G   M   K

        var fontColor = MathUtils.toRGB(r, g, b);
        var fontColorDark = MathUtils.toRGB(r * contrast, g * contrast, b * contrast);

        var numberTextToRender = Component.literal(displayValue.numberText).withStyle(Style.EMPTY.withFont(fontRL).withColor(fontColor));
        var dotTextToRender = Component.literal(displayValue.dotText).withStyle(Style.EMPTY.withFont(fontRL).withColor(fontColor));
        var bgTextToRender = Component.literal(bgText).withStyle(Style.EMPTY.withFont(fontRL).withColor(fontColorDark));

        var textWidth = Minecraft.getInstance().font.width(bgTextToRender);


        fill(stack, x - 5, y - 5, textWidth + 5, bgHeight + 5, 0, 0, 0);
        Minecraft.getInstance().font.draw(stack, bgTextToRender, x, y, 0);
        if (precision != 0) {
            Minecraft.getInstance().font.draw(stack, dotTextToRender, x, y, 0);
        }
        Minecraft.getInstance().font.draw(stack, numberTextToRender, x, y, 0);

    }

    public static void fill(PoseStack stack, float x, float y, float width, float height, float r, float g, float b) {
        var maxX = x + width;
        var maxY = y + height;
        var matrix = stack.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(matrix, x, maxY, 0.0F).color(r, g, b, 1).endVertex();
        bufferbuilder.vertex(matrix, maxX, maxY, 0.0F).color(r, g, b, 1).endVertex();
        bufferbuilder.vertex(matrix, maxX, y, 0.0F).color(r, g, b, 1).endVertex();
        bufferbuilder.vertex(matrix, x, y, 0.0F).color(r, g, b, 1).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
