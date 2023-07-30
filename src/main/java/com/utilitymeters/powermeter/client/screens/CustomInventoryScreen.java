package com.utilitymeters.powermeter.client.screens;

import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class CustomInventoryScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public CustomInventoryScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    public <V extends GuiEventListener & Widget & NarratableEntry> void addCustomWidget(V widget) {
        this.addRenderableWidget(widget);
    }
}
