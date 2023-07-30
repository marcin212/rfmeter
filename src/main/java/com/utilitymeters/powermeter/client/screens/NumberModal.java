package com.utilitymeters.powermeter.client.screens;

import com.utilitymeters.powermeter.client.screens.wigets.CustomTextField;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.LongConsumer;
import java.util.function.Supplier;

public class NumberModal extends BasicModal {

    CustomTextField modalValue;

    LongConsumer onSave;
    LongConsumer onCancel;

    LongConsumer onCustomAction = null;
    Component customActionName = null;

    Button customActionButton;

    Supplier<String> initializer;

    public NumberModal(Component title, LongConsumer onSave, LongConsumer onCancel) {
        super(title);
        this.onCancel = onCancel;
        this.onSave = onSave;
        this.initializer = ()->"";
    }

    public NumberModal(Component title, LongConsumer onSave, LongConsumer onCancel, Component customActionName, LongConsumer onCustomAction, Supplier<String> initializer) {
        this(title, onSave, onCancel);
        this.onCustomAction = onCustomAction;
        this.customActionName = customActionName;
        this.initializer = initializer;
    }

    @Override
    protected void init() {
        super.init();
        modalValue = new CustomTextField(font, startX - 120/2, startY, 120, 20, Component.empty(), (value)->{}, this::onlyNumbersFilter);
        modalValue.setValue(initializer.get());
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
