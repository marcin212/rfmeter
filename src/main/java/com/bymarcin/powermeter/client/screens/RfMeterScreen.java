package com.bymarcin.powermeter.client.screens;

import com.bymarcin.powermeter.containers.RfMeterContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class RfMeterScreen extends AbstractContainerScreen<RfMeterContainer> {

    public RfMeterScreen(RfMeterContainer container, Inventory inventory, Component name) {
        super(container, inventory, name);
    }

    @Override
    protected void renderBg(PoseStack p_97787_, float p_97788_, int p_97789_, int p_97790_) {

    }
}
