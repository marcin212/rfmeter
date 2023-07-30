package com.utilitymeters.powermeter.containers;

import com.utilitymeters.powermeter.RfMeterLogic;
import com.utilitymeters.powermeter.blockentity.RfMeterBlockEntity;
import com.utilitymeters.powermeter.registry.RfMeterBlocks;
import com.utilitymeters.powermeter.registry.RfMeterContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class RfMeterContainer extends AbstractContainerMenu {
    private final RfMeterBlockEntity entity;


    public RfMeterContainer(int windowId, BlockPos pos, Inventory playerInventory, Player player) {
        super(RfMeterContainers.RF_METER.get(), windowId);
        if(player.getCommandSenderWorld().getBlockEntity(pos) instanceof RfMeterBlockEntity entity) {
            this.entity = entity;
        } else {
            this.entity = null;
        }
    }

    public RfMeterBlockEntity getEntity() {
        return entity;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int p_38942_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(entity.getLevel(), entity.getBlockPos()), player, RfMeterBlocks.RF_METER.get());
    }

    public static MenuProvider createMenuProvider(BlockPos pos) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable("screen.utilitymeters.rfmeter");
            }

            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                return new RfMeterContainer(windowId, pos, playerInventory, playerEntity);
            }
        };
    }

    public RfMeterLogic getLogic() {
        return getEntity().logic;
    }

}
