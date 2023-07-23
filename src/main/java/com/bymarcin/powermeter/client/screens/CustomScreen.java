package com.bymarcin.powermeter.client.screens;

import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public abstract class CustomScreen extends Screen {

    protected CustomScreen(Component component) {
        super(component);
    }

    public <T extends GuiEventListener & Widget & NarratableEntry> void addCustomWidget(T widget) {
        this.addRenderableWidget(widget);
    }
}
