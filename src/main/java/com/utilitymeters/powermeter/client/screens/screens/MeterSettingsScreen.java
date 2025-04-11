package com.utilitymeters.powermeter.client.screens.screens;

import com.utilitymeters.powermeter.client.screens.screens.modals.IncrementNumberModal;
import com.utilitymeters.powermeter.client.screens.screens.modals.ModalScreen;
import com.utilitymeters.powermeter.client.screens.screens.modals.NumberModal;
import com.utilitymeters.powermeter.client.screens.screens.modals.TextModal;
import com.utilitymeters.powermeter.client.screens.wigets.CustomButton;
import com.utilitymeters.powermeter.containers.BaseMeterContainer;
import com.utilitymeters.powermeter.network.PacketHandler;
import com.utilitymeters.powermeter.network.RfMeterSyncC2SPacket;
import com.utilitymeters.powermeter.network.RfMeterSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class MeterSettingsScreen extends ModalScreen implements MenuAccess<BaseMeterContainer> {
    protected final BaseMeterContainer menu;
    public static final int imageWidth = 256;
    public static final int imageHeight = 256;

    private NumberModal addModal;
    private NumberModal subtractModal;

    private NumberModal transferLimitModal;
    private TextModal passwordModal;

    protected MeterSettingsScreen(BaseMeterContainer container, Component title) {
        super(title);
        this.menu = container;
    }

    @Override
    public @NotNull BaseMeterContainer getMenu() {
        return this.menu;
    }

    @Override
    protected void init() {
        super.init();
        int relX = (this.width - imageWidth) / 2;
        int relY = (this.height - imageHeight) / 2;
        var entity = getMenu().getEntity();
        var logic = getMenu().getLogic();


        var buttonAdd = Button.builder(Component.literal("+"), this::openAddModal).bounds(relX + 6, relY + 6 + 22 * 4, 45, 20).build();
        var buttonSub = Button.builder(Component.literal("-"), this::openSubtractModal).bounds(relX + 6 + 55, relY + 6 + 22 * 4, 45, 20).build();

        addCustomWidget(buttonAdd);
        addCustomWidget(buttonSub);

        addCustomWidget(new CustomButton(relX + 6, relY + 6, 100, 20,
                () -> (logic.isInCounterMode() ?
                        Component.translatable("screen.utilitymeters.settings.mode.counter.button")
                        : Component.translatable("screen.utilitymeters.settings.mode.prepaid.button")),
                (b) -> {
                    logic.setCounterMod(!logic.isInCounterMode());
                    var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addCounterMode(logic.isInCounterMode()).build();
                    PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
                }
        ));


        addCustomWidget(Button.builder(Component.translatable("screen.utilitymeters.settings.reset.button"), (b) -> {
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addValue(0).addPrepaidValue(0).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }).bounds(relX + 6, relY + 6 + 22 * 1, 100, 20).build());


        addCustomWidget(new CustomButton(relX + 6, relY + 6 + 22 * 2, 100, 20, () -> Component.translatable("screen.utilitymeters.settings.password.button"), (b) -> {
            passwordModal.openModal();
        }));

        var transferLimit = Button.builder(Component.translatable("screen.utilitymeters.settings.transfer_limit.button"), (a) -> transferLimitModal.openModal()).bounds(relX + 6, relY + 6 + 22 * 3, 100, 20).build();
        addCustomWidget(transferLimit);


        addCustomWidget(Button.builder(Component.literal("X"), this::onCloseSettings).bounds(relX + imageWidth - 20 - 10, relY + 10, 20, 20).build());

        addModal = new IncrementNumberModal(Component.translatable("screen.utilitymeters.modalbutton.addToPrepaidTitle"), (val) -> {
            logic.addTopUp(val);
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addPrepaidValue(logic.getPrepaidValue()).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }, (val) -> {

        }, true);
        subtractModal = new IncrementNumberModal(Component.translatable("screen.utilitymeters.modalbutton.subFromPrepaid"), (val) -> {
            logic.subTopUp(val);
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addPrepaidValue(logic.getPrepaidValue()).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }, (val) -> {
        }, false);

        transferLimitModal = new NumberModal(Component.translatable("screen.utilitymeters.settings.transfer_limit.title"), (val) -> {
            logic.setTransferLimit((int) val);
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addTransferLimit((int) val).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }, (val) -> {

        }, Component.translatable("screen.utilitymeters.settings.set_unlimited.button"), (val) -> {
            getMenu().getLogic().setTransferLimit(-1);
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addTransferLimit(-1).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }, () -> String.valueOf(getMenu().getLogic().getTransferLimit()));

        passwordModal = new TextModal(Component.translatable("screen.utilitymeters.settings.set_password.title"), (pass) -> {
            getMenu().getLogic().setPassword(pass);
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addPassword(getMenu().getLogic().getPassword(), getMenu().getLogic().isProtected()).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }, (pass) -> {
        }, Component.translatable("screen.utilitymeters.settings.remove_password.button"), (pas) -> {
            getMenu().getLogic().removePassword();
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addPassword(getMenu().getLogic().getPassword(), getMenu().getLogic().isProtected()).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }, true);


    }

    private void onCloseSettings(Button button) {
        if (Minecraft.getInstance().screen == this) {
            Minecraft.getInstance().popGuiLayer();
        }
    }

    private void openAddModal(Button btn) {
        addModal.openModal();
    }

    private void openSubtractModal(Button btn) {
        subtractModal.openModal();
    }
}
