package com.utilitymeters.powermeter.blockentity;

import com.utilitymeters.logic.CounterLogic;
import com.utilitymeters.logic.smoothers.Average;
import com.utilitymeters.powermeter.registry.RfMeterBlockEntities;
import com.utilitymeters.utilitymeters.capabilities.impl.FluidHandlerMeter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidMeterBlockEntity extends BaseMeterBlockEntity {
    FluidHandlerMeter fluidHandlerMeter = new FluidHandlerMeter(this);

    public FluidMeterBlockEntity(BlockPos pos, BlockState state) {
        super(RfMeterBlockEntities.FLUID_METER.get(), pos, state, new CounterLogic(new Average(40)));
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (ForgeCapabilities.FLUID_HANDLER.equals(cap) && flowDirection().outputDirection().getOpposite() == side) {
            return LazyOptional.of(() -> fluidHandlerMeter.getInputTankHandler()).cast();
        } else if(ForgeCapabilities.FLUID_HANDLER.equals(cap) && flowDirection().outputDirection() == side) {
            return LazyOptional.of(() -> fluidHandlerMeter.getOutputTankHandler()).cast();
        }
        return LazyOptional.empty();
    }


    @Override
    public void saveData(CompoundTag tag) {
        fluidHandlerMeter.saveData(tag);
    }

    @Override
    public void loadData(CompoundTag tag) {
        fluidHandlerMeter.loadData(tag);
    }

    @Override
    public void tick() {
        fluidHandlerMeter.onTick();
        super.tick();
    }
}
