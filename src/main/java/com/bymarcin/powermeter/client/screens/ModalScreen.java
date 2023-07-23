package com.bymarcin.powermeter.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public abstract class ModalScreen extends CustomScreen {
    private boolean isOpen = false;

    protected ModalScreen(Component title) {
        super(title);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void openModal() {
        if (!isOpen) {
            Minecraft.getInstance().pushGuiLayer(this);
            isOpen = true;
        }
    }

    public void closeModal() {
        if (isOpen && Minecraft.getInstance().screen == this) {
            Minecraft.getInstance().popGuiLayer();
            isOpen = false;
        }
    }

}
