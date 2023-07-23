package com.bymarcin.powermeter.client.screens;

import com.bymarcin.powermeter.RfMeterMod;
import com.bymarcin.powermeter.client.screens.wigets.*;
import com.bymarcin.powermeter.network.PacketHandler;
import com.bymarcin.powermeter.RfMeterLogic;
import com.bymarcin.powermeter.containers.RfMeterContainer;
import com.bymarcin.powermeter.network.RfMeterSyncC2SPacket;
import com.bymarcin.powermeter.network.RfMeterSyncPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RfMeterScreen extends CustomScreen implements MenuAccess<RfMeterContainer> {
    protected final RfMeterContainer menu;
    public static final ResourceLocation RFMETER_LOCATION = new ResourceLocation(RfMeterMod.MODID,"textures/gui/container/rfmeter.png");
    public static final int imageWidth = 256;
    public static final int imageHeight = 256;

    private NumberModal addModal;
    private NumberModal subtractModal;

    private NumberModal transferLimitModal;
    private TextModal passwordModal;

    public ForgeSlider contrastSlider;

    SegDisplay segDisplay;
    SegDisplay segDisplayTransfer;

    public RfMeterScreen(RfMeterContainer container, Inventory inventory, Component name) {
        super(name);
        this.menu = container;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return super.children();
    }

    @Override
    public @NotNull RfMeterContainer getMenu() {
        return this.menu;
    }


    @Override
    protected void init() {
        super.init();
         var emptyLiteral = Component.literal("");
         var entity = getMenu().getEntity();

        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;

        if (entity != null) {
            var color = entity.logic.getColor();

            int height = 12;
            int space = height + 1;


            addCustomWidget(new ColorSlider(color, ColorSlider.RGB.R,relX+6, relY+6, 120, 12, emptyLiteral, Component.literal(" R"),
                    0, 255, color.r * 255, 1, 0, true, this::onColorChange));
            addCustomWidget(new ColorSlider(color, ColorSlider.RGB.G,relX+6, relY+6 + space, 120, 12, emptyLiteral, Component.literal(" G"),
                    0, 255, color.g * 255, 1, 0, true, this::onColorChange));
            addCustomWidget(new ColorSlider(color, ColorSlider.RGB.B, relX+6, relY+6 + space * 2, 120, 12, emptyLiteral, Component.literal(" B"),
                    0, 255, color.b * 255, 1, 0, true, this::onColorChange));

            contrastSlider = new CustomSlider(relX+6, relY+6 + space * 3, 120, 12, Component.literal("Contrast: "), Component.literal("%"), 0, 100, 20, 1, 1,true, (val) -> {
                color.contrast = (float) (val/100f);
                this.onColorChange(color);
            });
            addCustomWidget(contrastSlider);
//top up
            var buttonAdd  = new Button(relX + 6 + 190, (int) (relY + 13*6 + (56*0.4f*2) + 2*0.4f/2), 20, 20, Component.literal("+"), this::openAddModal );
            var buttonSub  = new Button(relX + 6 + 190 + 22,  (int) (relY + 13*6 + (56*0.4f*2) + 2*0.4f/2), 20, 20, Component.literal("-"), this::openSubtractModal );
            addCustomWidget(buttonAdd);
            addCustomWidget(buttonSub);


            var transferLimit  = new Button(relX + 6 + 190 - 102, (int) (relY + 13*6 + (56*0.4f*2) + 2*0.4f/2), 100, 20, Component.literal("Transfer Limit"), (a)->transferLimitModal.openModal() );
            addCustomWidget(transferLimit);

            addCustomWidget(new CustomButton(relX + 6 + 120 + 6, relY+6, 100, 20, ()-> Component.literal("Mode: " + (getMenu().getEntity().logic.isInCounterMode()?"Counter":"Prepaid")), (b)->{
                getMenu().getEntity().logic.setCounterMod(!getMenu().getEntity().logic.isInCounterMode());
                var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addCounterMode(entity.logic.isInCounterMode()).build();
                PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
            }));


            addCustomWidget(new Button(relX + 6 + 120 + 6, relY+6 + 22 * 1, 100, 20, Component.literal("Reset"), (b)-> {
                var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addValue(0).addPrepaidValue(0).build();
                PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
            }));


            addCustomWidget(new CustomButton(relX + 6 + 120 + 6, relY+6 + 22 * 2, 100, 20, ()->Component.literal("State: " + (getMenu().getEntity().logic.isOn()?"On":"Off") ), (b)-> {
                var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addOn(!getMenu().getEntity().logic.isOn()).build();
                PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
            }));


            addCustomWidget(new CustomButton(relX + 6 + 120 + 6, relY+6 + 22 * 3, 100, 20, ()->Component.literal("Password"), (b)-> {
                passwordModal.openModal();
            }));




//            addRenderableWidget(new CustomButton(10, relY+6 + space * 3, 120, 20, () -> Component.literal(entity.logic.isInCounterMode()?"Prepaid":"Counter"), (Button btn)->{
//                entity.logic.setCounterMod(!entity.logic.isInCounterMode());
//                var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addCounterMode(entity.logic.isInCounterMode()).build();
//                btn.setMessage(Component.literal(entity.logic.isInCounterMode()?"Prepaid":"Counter"));
//                PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
//            } ));


           // var box= new EditBox(Minecraft.getInstance().font, 10, 10 , 150, 20, Component.literal("XD"));
            //box.setFilter((val)-> val.matches("[0-9]+"));
           // box.setResponder((newValue)-> System.out.println(newValue));
            //addRenderableWidget(box);

            addModal = new IncrementNumberModal(Component.translatable("screen.rfmeter.modalbutton.addToPrepaidTitle"), (val)->{
                System.out.println(val);
                getMenu().getEntity().logic.addTopUp(val);
                var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addPrepaidValue(getMenu().getEntity().logic.getPrepaidValue()).build();
                PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
            }, (val)->{

            }, true);
            subtractModal = new IncrementNumberModal(Component.translatable("screen.rfmeter.modalbutton.subFromPrepaid"), (val)->{
                System.out.println(val);
                getMenu().getEntity().logic.subTopUp(val);
                var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addPrepaidValue(getMenu().getEntity().logic.getPrepaidValue()).build();
                PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
            }, (val)-> {
            }, false);

            transferLimitModal = new NumberModal(Component.literal("Transfer limit"), (val)->{
                getMenu().getEntity().logic.setTransferLimit((int) val);
                var packet = new RfMeterSyncPacket.Builder<>(entity.getBlockPos(), RfMeterSyncC2SPacket.class).addTransferLimit((int) val).build();
                PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
            }, (val)->{

            }, Component.literal("Set Unlimited"), (val)->{});

            passwordModal = new TextModal(Component.literal("Set Password"),  (pass) -> {

            }, (pass)->{}, Component.literal("Remove password"), (pas)->{});
            segDisplay = new SegDisplay(19, 0, false);
            segDisplayTransfer = new SegDisplay(19, 0, false);
        }
    }



    private void openAddModal(Button btn) {
        addModal.openModal();
    }

    private void openSubtractModal(Button btn) {
        subtractModal.openModal();
    }

    private void onColorChange(RfMeterLogic.DisplayColor color) {
        var packet = new RfMeterSyncPacket.Builder<>(getMenu().getEntity().getBlockPos(), RfMeterSyncC2SPacket.class).
                addColor(color.r, color.g, color.b, color.contrast).build();
        PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
    }

    @Override
    public void render(PoseStack stack, int p_97796_, int p_97797_, float p_97798_) {
        stack.pushPose();
        renderBg(stack, p_97798_, p_97796_, p_97797_);
        super.render(stack, p_97796_, p_97797_, p_97798_);
        var color = getMenu().getEntity().logic.getColor();

        float scale = 0.2f;
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        relY = relY + 13 * 6;
        stack.pushPose();

        var w1s = "Current Transfer:";
        var w2s = "Total:";
        var w3s = "Prepaid Left:";
        var w4s = "Prepaid To:";

        var w1 = Component.literal(w1s);
        var w2 = Component.literal(w2s);
        var w3 = Component.literal(w3s);
        var w4 = Component.literal(w4s);
        var maxWidth = Minecraft.getInstance().font.width(w1);
        var w2Width = Minecraft.getInstance().font.width(w2);
        var w3Width = Minecraft.getInstance().font.width(w3);
        var w4Width = Minecraft.getInstance().font.width(w4);



        Minecraft.getInstance().font.draw(stack, w1, relX+6, relY + (50*scale*0) + 25*scale/2, 0x3F3F3F);
        Minecraft.getInstance().font.draw(stack, w2, relX+6 + maxWidth-w2Width, relY + (56*scale*1) + 25*scale/2, 0x3F3F3F);
        Minecraft.getInstance().font.draw(stack, w3, relX+6 + maxWidth-w3Width, relY + (56*scale*2) + 25*scale/2, 0x3F3F3F);
        Minecraft.getInstance().font.draw(stack, w4, relX+6 + maxWidth-w4Width, relY + (56*scale*3) + 25*scale/2, 0x3F3F3F);
        stack.scale(scale, scale, 0.0f);
        segDisplayTransfer.render(stack, getMenu().getEntity().logic.getTransfer(), color.r, color.g, color.b, color.contrast, (relX+12+maxWidth)/scale, relY/scale);
        segDisplay.render(stack, getMenu().getEntity().logic.getCurrentValue(), color.r, color.g, color.b, color.contrast, (relX+12+maxWidth)/scale, (relY + 56*scale)/scale );
        segDisplay.render(stack, getMenu().getEntity().logic.calculatedPrepaid(), color.r, color.g, color.b, color.contrast, (relX+12+maxWidth)/scale, (relY + 56*2*scale)/scale);
        segDisplay.render(stack, getMenu().getEntity().logic.getPrepaidValue(), color.r, color.g, color.b, color.contrast, (relX+12+maxWidth)/scale, (relY + 56*3*scale)/scale);
        stack.popPose();

    }

    protected void renderBg(PoseStack stack, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShaderTexture(0, RFMETER_LOCATION);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(stack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
