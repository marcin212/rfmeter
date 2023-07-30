package com.utilitymeters.powermeter.client.screens;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.LongConsumer;

public class IncrementNumberModal extends NumberModal {
    Button add1K;
    Button add1M;
    Button add1G;

    boolean isAdd;

    protected IncrementNumberModal(Component title, LongConsumer onSave, LongConsumer onCancel, boolean isAdd) {
        super(title, onSave, onCancel);
        this.isAdd = isAdd;
    }

    @Override
    protected void init() {
        super.init();

            add1K = new Button(startX - (3 * 32) / 2, startY + modalValue.getHeight() + 3, 30, modalValue.getHeight(),
                    isAdd?
                            Component.translatable("screen.utilitymeters.modalbutton.add1K"):
                            Component.translatable("screen.utilitymeters.modalbutton.sub1K")
                    , this::onAdd1K);
            add1M = new Button(add1K.x + add1K.getWidth() + 2, add1K.y, 30, modalValue.getHeight(),
                    isAdd?
                            Component.translatable("screen.utilitymeters.modalbutton.add1M"):
                            Component.translatable("screen.utilitymeters.modalbutton.sub1G")
                    , this::onAdd1M);
            add1G = new Button(add1M.x + add1M.getWidth() + 2, add1M.y, 30, modalValue.getHeight(),
                    isAdd?
                            Component.translatable("screen.utilitymeters.modalbutton.add1G"):
                            Component.translatable("screen.utilitymeters.modalbutton.sub1G")
                    , this::onAdd1G);

            addCustomWidget(add1K);
            addCustomWidget(add1M);
            addCustomWidget(add1G);
    }

    private void addToValue(long value) {
        modalValue.setValue(modalValue.getValueAsLong() + value);
    }

    private void onAdd1K(Button pButton) {
        addToValue(1_000);
    }

    private void onAdd1M(Button pButton) {
        addToValue(1_000_000);
    }

    private void onAdd1G(Button pButton) {
        addToValue(1_000_000_000);
    }

}
