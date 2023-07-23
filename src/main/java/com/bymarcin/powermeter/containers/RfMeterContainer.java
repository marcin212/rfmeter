package com.bymarcin.powermeter.containers;

import com.bymarcin.powermeter.RfMeterLogic;
import com.bymarcin.powermeter.blockentity.RfMeterBlockEntity;
import com.bymarcin.powermeter.registry.RfMeterBlocks;
import com.bymarcin.powermeter.registry.RfMeterContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class RfMeterContainer extends AbstractContainerMenu {
    private final RfMeterBlockEntity entity;
    private final Player playerEntity;

    private final IItemHandler playerInventory;

    public RfMeterContainer(int windowId, BlockPos pos, Inventory playerInventory, Player player) {
        super(RfMeterContainers.RF_METER.get(), windowId);
        if(player.getCommandSenderWorld().getBlockEntity(pos) instanceof RfMeterBlockEntity entity) {
            this.entity = entity;
        } else {
            this.entity = null;
        }
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        layoutPlayerInventorySlots(8, 84);
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
                return Component.translatable("screen.rfmeter");
            }

            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                return new RfMeterContainer(windowId, pos, playerInventory, playerEntity);
            }
        };
    }


    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
}
