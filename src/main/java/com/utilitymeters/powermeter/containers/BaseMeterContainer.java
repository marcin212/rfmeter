package com.utilitymeters.powermeter.containers;

import com.utilitymeters.logic.CounterLogic;
import com.utilitymeters.powermeter.blockentity.BaseMeterBlockEntity;
import com.utilitymeters.powermeter.registry.RfMeterContainers;
import com.utilitymeters.utils.DisplayColor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;


public class  BaseMeterContainer extends AbstractContainerMenu {
    private final BaseMeterBlockEntity entity;


    public BaseMeterContainer(int windowId, BlockPos pos, Inventory playerInventory, Player player) {
        super(RfMeterContainers.BASE_METER.get(), windowId);
        var blockEntity = player.getCommandSenderWorld().getBlockEntity(pos);
        if(blockEntity instanceof BaseMeterBlockEntity entity) {
            this.entity = entity;
        } else {
            this.entity = null;
        }
    }

    public BaseMeterBlockEntity getEntity() {
        return entity;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int p_38942_) {
        return ItemStack.EMPTY;
    }


    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(entity.getLevel(), entity.getBlockPos()), player, entity.getBlockState().getBlock());
    }

    public static MenuProvider createMenuProvider(BlockPos pos) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable("screen.utilitymeters.meter");
            }

            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                return new BaseMeterContainer(windowId, pos, playerInventory, playerEntity);
            }
        };
    }

    public CounterLogic getLogic() {
        return getEntity().counter();
    }

    public DisplayColor getDisplayColor() {
        return getEntity().displayColors();
    }

}
