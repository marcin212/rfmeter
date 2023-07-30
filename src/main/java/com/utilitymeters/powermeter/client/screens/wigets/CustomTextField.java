package com.utilitymeters.powermeter.client.screens.wigets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class CustomTextField extends EditBox {


    public <T extends GuiEventListener & Widget & NarratableEntry> CustomTextField(Font font, int x, int y, int width, int height, Component text, Consumer<String> pResponder, Predicate<String> filter) {
        super(font, x, y, width, height, text);
        this.setResponder(pResponder);
        this.setFilter(filter);
    }

    public long getValueAsLong() {
        try {
            return Long.parseLong(getValue());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setValue(long num) {
        setValue(String.valueOf(num));
    }

}
