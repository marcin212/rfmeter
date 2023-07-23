package com.bymarcin.powermeter.client.screens;

import com.bymarcin.powermeter.client.screens.wigets.CustomTextField;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class TextModal extends BasicModal {

    CustomTextField modalValue;
    Button customActionButton;
    Consumer<String> onSave;
    Consumer<String> onCancel;

    Component customActionName;

    Consumer<String> onCustomAction;

    protected TextModal(Component title, Consumer<String> onSave, Consumer<String> onCancel,Component customActionName, Consumer<String> onCustomAction) {
        super(title);
        this.onSave = onSave;
        this.onCancel = onCancel;
        this.onCustomAction = onCustomAction;
        this.customActionName = customActionName;
    }

    @Override
    protected void init() {
        super.init();

        modalValue = new CustomTextField(font, startX - 120/2, startY, 120, 20, Component.empty(), (value)->{}, (s)->true);
        customActionButton = new Button(startX - 120/2, startY+ 22, 120, 20, customActionName, this::onCustomAction);
        addCustomWidget(modalValue);
        addCustomWidget(customActionButton);

    }

    @Override
    void onSave(Button button) {
        super.onSave(button);
        this.onSave.accept(modalValue.getValue());
    }

    @Override
    void onCancel(Button button) {
        super.onCancel(button);
        this.onCancel.accept(modalValue.getValue());
    }

    private void onCustomAction(Button button) {
        closeModal();
        this.onCustomAction.accept(modalValue.getValue());
    }
}