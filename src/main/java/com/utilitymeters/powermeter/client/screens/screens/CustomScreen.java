package com.utilitymeters.powermeter.client.screens.screens;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public abstract class CustomScreen extends Screen {

    protected CustomScreen(Component component) {
        super(component);
    }

    public <T extends GuiEventListener & Renderable & NarratableEntry> void addCustomWidget(T widget) {
        this.addRenderableWidget(widget);
    }
}
