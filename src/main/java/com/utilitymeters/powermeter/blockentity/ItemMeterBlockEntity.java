package com.utilitymeters.powermeter.blockentity;

import com.utilitymeters.logic.CounterLogic;
import com.utilitymeters.logic.smoothers.Average;
import com.utilitymeters.powermeter.registry.RfMeterBlockEntities;
import com.utilitymeters.utilitymeters.capabilities.impl.ItemHandlerMeter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemMeterBlockEntity extends BaseMeterBlockEntity {
    ItemHandlerMeter itemHandler = new ItemHandlerMeter(this);

    public ItemMeterBlockEntity(BlockPos pos, BlockState state) {
        super(RfMeterBlockEntities.ITEM_METER.get(), pos, state, new CounterLogic(new Average(40)));
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (ForgeCapabilities.ITEM_HANDLER.equals(cap) && flowDirection().outputDirection().getOpposite() == side) {
            return LazyOptional.of(() -> itemHandler.getInputStackHandler()).cast();
        } else if(ForgeCapabilities.ITEM_HANDLER.equals(cap) && flowDirection().outputDirection() == side) {
            return LazyOptional.of(() -> itemHandler.getOutputStackHandler()).cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void saveData(CompoundTag tag) {
        itemHandler.saveData(tag);
    }

    @Override
    public void loadData(CompoundTag tag) {
        itemHandler.loadData(tag);
    }

    @Override
    public void tick() {
        itemHandler.onTick();
        super.tick();
    }

    public void onBreak(Level level, BlockPos pos) {
        itemHandler.onBreak(level, pos);
    }
}
