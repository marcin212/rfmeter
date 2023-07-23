package com.bymarcin.powermeter.client.screens;

import com.bymarcin.powermeter.client.screens.wigets.CustomTextField;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.LongConsumer;

public class NumberModal extends BasicModal {

    CustomTextField modalValue;

    LongConsumer onSave;
    LongConsumer onCancel;

    LongConsumer onCustomAction = null;
    Component customActionName = null;

    Button customActionButton;

    public NumberModal(Component title, LongConsumer onSave, LongConsumer onCancel) {
        super(title);
        this.onCancel = onCancel;
        this.onSave = onSave;
    }

    public NumberModal(Component title, LongConsumer onSave, LongConsumer onCancel, Component customActionName, LongConsumer onCustomAction) {
        this(title, onSave, onCancel);
        this.onCustomAction = onCustomAction;
        this.customActionName = customActionName;
    }

    @Override
    protected void init() {
        super.init();
        modalValue = new CustomTextField(font, startX - 120/2, startY, 120, 20, Component.empty(), (value)->{}, this::onlyNumbersFilter);
        addCustomWidget(modalValue);
        if(hasCustomAction()) {
            customActionButton = new Button(startX - 120/2, startY+ 22, 120, 20, customActionName, this::onCustomAction);
            addCustomWidget(customActionButton);
        }
    }

    boolean hasCustomAction() {
        return onCustomAction != null && customActionName != null;
    }

    @Override
    void onSave(Button button) {
        super.onSave(button);
        this.onSave.accept(modalValue.getValueAsLong());
    }

    @Override
    void onCancel(Button button) {
        super.onCancel(button);
        this.onCancel.accept(modalValue.getValueAsLong());
    }

    void onCustomAction(Button button) {
       if(hasCustomAction()) {
           closeModal();
           onCustomAction.accept(modalValue.getValueAsLong());
       }
    }
    boolean onlyNumbersFilter(String value) {
        return value == null || value.isEmpty() || value.matches("[0-9]+");
    }
}
