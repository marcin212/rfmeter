package com.utilitymeters.powermeter.client.screens.wigets;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class CustomButton extends Button {
    private final Supplier<Component> componentSupplier;

    public CustomButton(int x, int y, int width, int height, Supplier<Component> componentSupplier, OnPress onPress) {
        super(x, y, width, height, componentSupplier.get(), onPress);
        this.componentSupplier = componentSupplier;
    }

    @Override
    public Component getMessage()
    {
        return componentSupplier.get();
    }
}
