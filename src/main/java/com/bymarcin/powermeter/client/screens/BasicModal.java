package com.bymarcin.powermeter.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
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
        startX = modalX + modalWidth /2;
        startY = modalY + (modalHeight - 2*20) / 2;

        save = new Button(startX - 41, modalY + this.modalHeight - 20 - 2, 40, 20, Component.translatable("screen.rfmeter.modalbutton.save"), this::onSave);
        cancel = new Button(save.x + save.getWidth() + 2, modalY + this.modalHeight - 20 - 2, 40, 20, Component.translatable("screen.rfmeter.modalbutton.cancel"), this::onCancel);
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
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        fill(pPoseStack, modalX -1, modalY -1, modalX + modalWidth +1, modalY + modalHeight +1, 0xFF000000);
        fill(pPoseStack, modalX, modalY, modalX + modalWidth, modalY + modalHeight, 0xFFC6C6C6);
        var startTitleX = (modalWidth - titleWidth)/2;
        font.draw(pPoseStack, title, modalX + startTitleX, modalY + 2, 0xFF3F3F3F);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}
