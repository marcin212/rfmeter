package com.utilitymeters.powermeter.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public abstract class BasicModal extends ModalScreen {
    int titleWidth;
    Font font;
    Button save;
    Button cancel;

    int modalWidth = 200;
    int modalHeight = 100;
    int modalX;
    int modalY;

    int startX;

    int startY;

    Component buttonSaveText = Component.translatable("screen.utilitymeters.modalbutton.save");
    Component buttonCancelText = Component.translatable("screen.utilitymeters.modalbutton.cancel");


    protected BasicModal(Component title) {
        super(title);
        font = Minecraft.getInstance().font;
        titleWidth = font.width(title);

    }

    @Override
    protected void init() {
        super.init();

        modalX = (this.width - this.modalWidth) / 2;
        modalY = (this.height - this.modalHeight) / 2;
        startX = modalX + modalWidth / 2;
        startY = modalY + (modalHeight - 2 * 20) / 2;
        save = Button.builder(buttonSaveText, this::onSave).bounds(startX - 41, modalY + this.modalHeight - 20 - 2, 40, 20).build();
        cancel = Button.builder(buttonCancelText, this::onCancel).bounds((startX - 41) + save.getWidth() + 2, modalY + this.modalHeight - 20 - 2, 40, 20).build();
        addCustomWidget(save);
        addCustomWidget(cancel);
    }

    void onSave(Button button) {
        closeModal();
    }

    void onCancel(Button button) {
        closeModal();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.fill(modalX - 1, modalY - 1, modalX + modalWidth + 1, modalY + modalHeight + 1, 0xFF000000);
        pGuiGraphics.fill(modalX, modalY, modalX + modalWidth, modalY + modalHeight, 0xFFC6C6C6);
        var startTitleX = (modalWidth - titleWidth) / 2;
        pGuiGraphics.drawString(Minecraft.getInstance().font, title, modalX + startTitleX, modalY + 2, 0xFF3F3F3F, false);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
