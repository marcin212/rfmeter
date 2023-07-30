package com.utilitymeters.powermeter.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public abstract class ModalScreen extends CustomScreen {

    protected ModalScreen(Component title) {
        super(title);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void openModal() {
        if (Minecraft.getInstance().screen != this) {
            Minecraft.getInstance().pushGuiLayer(this);
        }
    }

    public void closeModal() {
        if (Minecraft.getInstance().screen == this) {
            Minecraft.getInstance().popGuiLayer();
        }
    }

}
