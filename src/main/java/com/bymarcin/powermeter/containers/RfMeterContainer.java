package com.bymarcin.powermeter.containers;

import com.bymarcin.powermeter.registry.RfMeterBlocks;
import com.bymarcin.powermeter.registry.RfMeterContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class RfMeterContainer extends AbstractContainerMenu {
    private final BlockEntity entity;

    public RfMeterContainer(int windowId, BlockPos pos, Inventory playerInventory, Player player) {
        super(RfMeterContainers.RF_METER.get(), windowId);
        this.entity = player.getCommandSenderWorld().getBlockEntity(pos);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(entity.getLevel(), entity.getBlockPos()), player, RfMeterBlocks.RF_METER.get());
    }

    public static MenuProvider createMenuProvider(BlockPos pos) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TranslatableComponent("screen.rfmeter");
            }

            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                return new RfMeterContainer(windowId, pos, playerInventory, playerEntity);
            }
        };
    }
}
