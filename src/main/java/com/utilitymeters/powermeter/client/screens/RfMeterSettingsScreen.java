package com.utilitymeters.powermeter.client.screens;

import com.utilitymeters.powermeter.client.screens.wigets.CustomButton;
import com.utilitymeters.powermeter.containers.RfMeterContainer;
import com.utilitymeters.powermeter.network.PacketHandler;
import com.utilitymeters.powermeter.network.RfMeterSyncC2SPacket;
import com.utilitymeters.powermeter.network.RfMeterSyncPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class RfMeterSettingsScreen extends ModalScreen implements MenuAccess<RfMeterContainer>  {
    protected final RfMeterContainer menu;
    public static final int imageWidth = 256;
    public static final int imageHeight = 256;

    private NumberModal addModal;
    private NumberModal subtractModal;

    private NumberModal transferLimitModal;
    private TextModal passwordModal;

    protected RfMeterSettingsScreen(RfMeterContainer container, Component title) {
        super(title);
        this.menu = container;
    }

    @Override
    public @NotNull RfMeterContainer getMenu() {
        return this.menu;
    }

    @Override
    protected void init() {
        super.init();
        int relX = (this.width - imageWidth) / 2;
        int relY = (this.height - imageHeight) / 2;
        var entity = getMenu().getEntity();

        var buttonAdd  = new Button(relX + 6, relY+6 + 22 * 4, 45, 20, Component.literal("+"), this::openAddModal );
        var buttonSub  = new Button(relX + 6 + 55,  relY+6 + 22 * 4, 45, 20, Component.literal("-"), this::openSubtractModal );
        addCustomWidget(buttonAdd);
        addCustomWidget(buttonSub);

        addCustomWidget(new CustomButton(relX + 6 , relY+6, 100, 20,
                ()-> (getMenu().getEntity().logic.isInCounterMode() ?
                        Component.translatable("screen.utilitymeters.settings.mode.counter.button")
                        :Component.translatable("screen.utilitymeters.settings.mode.prepaid.button")),
                (b)->{
                    getMenu().getEntity().logic.setCounterMod(!getMenu().getEntity().logic.isInCounterMode());
                    var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addCounterMode(entity.logic.isInCounterMode()).build();
                    PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
                }
        ));


        addCustomWidget(new Button(relX + 6, relY+6 + 22 * 1, 100, 20, Component.translatable("screen.utilitymeters.settings.reset.button"), (b)-> {
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addValue(0).addPrepaidValue(0).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }));


        addCustomWidget(new CustomButton(relX + 6 , relY+6 + 22 * 2, 100, 20, ()->Component.translatable("screen.utilitymeters.settings.password.button"), (b)-> {
            passwordModal.openModal();
        }));

        var transferLimit  = new Button(relX + 6 , relY+6 + 22 * 3, 100, 20, Component.translatable("screen.utilitymeters.settings.transfer_limit.button"), (a)->transferLimitModal.openModal() );
        addCustomWidget(transferLimit);

        addCustomWidget(new Button(relX + imageWidth - 20 - 10, relY + 10, 20, 20, Component.literal("X"), this::onCloseSettings));

        addModal = new IncrementNumberModal(Component.translatable("screen.utilitymeters.modalbutton.addToPrepaidTitle"), (val)->{
            getMenu().getEntity().logic.addTopUp(val);
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addPrepaidValue(getMenu().getEntity().logic.getPrepaidValue()).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }, (val)->{

        }, true);
        subtractModal = new IncrementNumberModal(Component.translatable("screen.utilitymeters.modalbutton.subFromPrepaid"), (val)->{
            getMenu().getEntity().logic.subTopUp(val);
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addPrepaidValue(getMenu().getEntity().logic.getPrepaidValue()).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }, (val)-> {
        }, false);

        transferLimitModal = new NumberModal(Component.translatable("screen.utilitymeters.settings.transfer_limit.title"), (val)->{
            getMenu().getEntity().logic.setTransferLimit((int) val);
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addTransferLimit((int) val).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }, (val)->{

        }, Component.translatable("screen.utilitymeters.settings.set_unlimited.button"), (val)->{
            getMenu().getLogic().setTransferLimit(-1);
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addTransferLimit(-1).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }, ()-> String.valueOf(getMenu().getLogic().getTransferLimit()));

        passwordModal = new TextModal(Component.translatable("screen.utilitymeters.settings.set_password.title"),  (pass) -> {
            getMenu().getLogic().setPassword(pass);
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addPassword(getMenu().getLogic().getPassword(), getMenu().getLogic().isProtected()).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }, (pass)->{}, Component.translatable("screen.utilitymeters.settings.remove_password.button"), (pas)->{
            getMenu().getLogic().removePassword();
            var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addPassword(getMenu().getLogic().getPassword(), getMenu().getLogic().isProtected()).build();
            PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
        }, true);


    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
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
