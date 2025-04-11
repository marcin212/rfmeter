package com.utilitymeters.powermeter.client.screens.screens;

import com.utilitymeters.powermeter.RfMeterMod;
import com.utilitymeters.powermeter.client.screens.screens.modals.TextModal;
import com.utilitymeters.powermeter.client.screens.wigets.ColorSlider;
import com.utilitymeters.powermeter.client.screens.wigets.CustomButton;
import com.utilitymeters.powermeter.client.screens.wigets.SegDisplay;
import com.utilitymeters.powermeter.containers.BaseMeterContainer;
import com.utilitymeters.powermeter.network.PacketHandler;
import com.utilitymeters.powermeter.network.RfMeterSyncC2SPacket;
import com.utilitymeters.powermeter.network.RfMeterSyncPacket;
import com.utilitymeters.utils.DisplayColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BaseMeterScreen extends CustomScreen implements MenuAccess<BaseMeterContainer> {
    public static final ResourceLocation RFMETER_LOCATION = new ResourceLocation(RfMeterMod.MODID, "textures/gui/container/rfmeter.png");
    public static final int imageWidth = 256;
    public static final int imageHeight = 256;
    private static final Component emptyLiteral = Component.literal("");

    protected final BaseMeterContainer menu;

    private SegDisplay segDisplay;

    private Font font;

    private Component transferTitle;
    private int transferTitleWidth;

    private Component totalTitle;
    private int totalWidth;

    private Component prepaidLeftTitle;
    private int prepaidLeftWidth;

    private Component prepaidToTitle;
    private int prepaidToWidth;

    private TextModal logInModal;

    public BaseMeterScreen(BaseMeterContainer container, Inventory inventory, Component name) {
        super(name);
        this.menu = container;
        this.font = Minecraft.getInstance().font;

        transferTitle = Component.translatable("screen.utilitymeters.main.currentTransfer");
        transferTitleWidth = font.width(transferTitle);

        totalTitle = Component.translatable("screen.utilitymeters.main.total");
        totalWidth = font.width(totalTitle);

        prepaidLeftTitle = Component.translatable("screen.utilitymeters.main.prepaidLeft");
        prepaidLeftWidth = font.width(prepaidLeftTitle);

        prepaidToTitle = Component.translatable("screen.utilitymeters.main.prepaidTo");
        prepaidToWidth = font.width(prepaidToTitle);

    }

    @Override
    public List<? extends GuiEventListener> children() {
        return super.children();
    }

    @Override
    public @NotNull BaseMeterContainer getMenu() {
        return this.menu;
    }


    @Override
    protected void init() {
        super.init();

        var entity = getMenu().getEntity();

        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;

        if (entity != null) {
            var color = getMenu().getDisplayColor();
            var counter = getMenu().getLogic();

            int height = 12;
            int space = height + 1;

            int startYColor = relY + 145;
            addCustomWidget(new ColorSlider(this::displayColor, ColorSlider.RGB.R, relX + 6, startYColor + 6, 120, height, emptyLiteral, Component.literal(" R"),
                    0, 255, color.r * 255, 1, 0, true, this::onColorChange));
            addCustomWidget(new ColorSlider(this::displayColor, ColorSlider.RGB.G, relX + 6, startYColor + 6 + space, 120, height, emptyLiteral, Component.literal(" G"),
                    0, 255, color.g * 255, 1, 0, true, this::onColorChange));
            addCustomWidget(new ColorSlider(this::displayColor, ColorSlider.RGB.B, relX + 6, startYColor + 6 + space * 2, 120, height, emptyLiteral, Component.literal(" B"),
                    0, 255, color.b * 255, 1, 0, true, this::onColorChange));
            addCustomWidget(new ColorSlider(this::displayColor, ColorSlider.RGB.Contrast, relX + 6, startYColor + 6 + space * 3, 120, 12, Component.translatable("screen.utilitymeters.main.contrast"), Component.literal("%"),
                    0, 100, color.contrast * 100, 1, 0, true, this::onColorChange));


            addCustomWidget(new CustomButton(relX + 6 + 120 + 6, startYColor + 6, 100, 20, () -> (counter.isOn() ? Component.translatable("screen.utilitymeters.main.state.on") : Component.translatable("screen.utilitymeters.main.state.off")), (b) -> {
                var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addOn(!counter.isOn()).build();
                PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
            }));

            addCustomWidget(Button.builder(Component.translatable("screen.utilitymeters.main.settings"), this::onSettings).bounds(relX + 6 + 120 + 6, startYColor + 6 + 20 + 1, 100, 20).build());


            logInModal = new TextModal(Component.translatable("screen.utilitymeters.main.login"), (pass) -> {
                if (counter.canEdit(pass))
                    Minecraft.getInstance().pushGuiLayer(new MeterSettingsScreen(getMenu(), emptyLiteral));
            }, (pass) -> {}, true);
            logInModal.setButtonSaveText(Component.translatable("screen.utilitymeters.modalbutton.login"));

            segDisplay = new SegDisplay(19, 0, false);
        }
    }

    private void onSettings(Button button) {
        var logic = getMenu().getLogic();
        if (logic.isProtected()) {
            logInModal.openModal();
        } else {
            Minecraft.getInstance().pushGuiLayer(new MeterSettingsScreen(getMenu(), emptyLiteral));
        }
    }

    private DisplayColor displayColor() {
        return getMenu().getDisplayColor();
    }

    private void onColorChange(DisplayColor color) {
        var packet = new RfMeterSyncPacket.Builder<>(getMenu().getEntity().getBlockPos(), RfMeterSyncC2SPacket.class).
                addColor(color.r, color.g, color.b, color.contrast).build();
        PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.pose().pushPose();
        renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        if (Minecraft.getInstance().screen != this) return;
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        var color = displayColor();

        float scale = 0.3f;
        int relX = (this.width - imageWidth) / 2;
        int relY = (this.height - imageHeight) / 2;
        relX += imageWidth / 2;
        pGuiGraphics.pose().pushPose();

        var offset = 17;
        var logic = getMenu().getLogic();
        pGuiGraphics.drawString(this.font, transferTitle,  (int)(relX - transferTitleWidth / 2f), relY + offset, 0x3F3F3F, false);
        pGuiGraphics.drawString(this.font, totalTitle, (int)(relX - totalWidth / 2f), relY + offset + 28, 0x3F3F3F, false);
        if (!logic.isInCounterMode()) {
            pGuiGraphics.drawString(this.font, prepaidLeftTitle, (int)(relX - prepaidLeftWidth / 2f), relY + offset + 28 * 2, 0x3F3F3F, false);
            pGuiGraphics.drawString(this.font, prepaidToTitle, (int)(relX - prepaidToWidth / 2f), relY + offset + 28 * 3, 0x3F3F3F, false);
        }
        pGuiGraphics.pose().scale(scale, scale, 0.0f);
        var segX = (relX - segDisplay.getTextWidth() / 2 * scale) / scale;
        segDisplay.render(pGuiGraphics, logic.getTransfer(), color.r, color.g, color.b, color.contrast, segX, (relY + 28) / scale);
        segDisplay.render(pGuiGraphics, logic.getCurrentValue(), color.r, color.g, color.b, color.contrast, segX, (relY + 28 * 2) / scale);
        if (!logic.isInCounterMode()) {
            segDisplay.render(pGuiGraphics, logic.calculatedPrepaid(), color.r, color.g, color.b, color.contrast, segX, (relY + 28 * 3) / scale);
            segDisplay.render(pGuiGraphics, logic.getPrepaidValue(), color.r, color.g, color.b, color.contrast, segX, (relY + 28 * 4) / scale);
        }
        pGuiGraphics.pose().popPose();

    }

    protected void renderBg(GuiGraphics pGuiGraphics, float p_97788_, int p_97789_, int p_97790_) {
        int relX = (this.width - imageWidth) / 2;
        int relY = (this.height - imageHeight) / 2;
        pGuiGraphics.blit(RFMETER_LOCATION, relX, relY, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        var focus = getFocused();
        var result = focus != null && focus.mouseReleased(pMouseX, pMouseY, pButton);
        var resultOriginal = super.mouseReleased(pMouseX, pMouseY, pButton);
        return result || resultOriginal;
    }
}

