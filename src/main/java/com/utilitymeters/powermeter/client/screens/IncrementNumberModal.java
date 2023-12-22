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

        add1K = Button.builder(text1K(), this::onAdd1K)
                .bounds(startX - (3 * 32) / 2, startY + modalValue.getHeight() + 3, 30, modalValue.getHeight())
                .build();
        add1M = Button.builder(text1M(), this::onAdd1M)
                .bounds(add1K.getX() + add1K.getWidth() + 2, add1K.getY(), 30, modalValue.getHeight())
                .build();
        add1G = Button.builder(text1G(), this::onAdd1G)
                .bounds(add1M.getX() + add1M.getWidth() + 2, add1M.getY(), 30, modalValue.getHeight())
                .build();

        addCustomWidget(add1K);
        addCustomWidget(add1M);
        addCustomWidget(add1G);
    }

    private Component text1K() {
        return isAdd ?
                Component.translatable("screen.utilitymeters.modalbutton.add1K") :
                Component.translatable("screen.utilitymeters.modalbutton.sub1K");
    }

    private Component text1M() {
        return isAdd ?
                Component.translatable("screen.utilitymeters.modalbutton.add1M") :
                Component.translatable("screen.utilitymeters.modalbutton.sub1M");
    }

    private Component text1G() {
        return isAdd ?
                Component.translatable("screen.utilitymeters.modalbutton.add1G") :
                Component.translatable("screen.utilitymeters.modalbutton.sub1G");
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
