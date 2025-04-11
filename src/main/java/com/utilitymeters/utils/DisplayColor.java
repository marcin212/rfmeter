package com.utilitymeters.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;

public class DisplayColor {
    public float contrast;
    public float r;
    public float g;
    public float b;
    private static final float[] defaultColor = DyeColor.LIME.getTextureDiffuseColors();
    public DisplayColor() {
        this(defaultColor[0], defaultColor[1], defaultColor[2], 0.2f);
    }
    public DisplayColor(CompoundTag tag) {
        this(getOrDefault(tag, "r", defaultColor[0]),
                getOrDefault(tag, "g", defaultColor[1]),
                getOrDefault(tag, "b", defaultColor[2]),
                getOrDefault(tag, "contrast", 0.2f));
    }

    private static float getOrDefault(CompoundTag tag, String color, float defaultColor) {
        if (tag.contains(color)) {
            return tag.getFloat(color);
        }
        return defaultColor;
    }

    public DisplayColor(float r, float g, float b, float contrast) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.contrast = contrast;
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putFloat("r", r);
        tag.putFloat("g", g);
        tag.putFloat("b", b);
        tag.putFloat("contrast", contrast);
        return tag;
    }

    public void save(CompoundTag tag) {
        tag.put("color", save());
    }

    public void load(CompoundTag tag) {
        if (!tag.contains("color")) return;
        CompoundTag colorTag = tag.getCompound("color");
        r = colorTag.getFloat("r");
        g = colorTag.getFloat("g");
        b = colorTag.getFloat("b");
        contrast = colorTag.getFloat("contrast");
    }

}
