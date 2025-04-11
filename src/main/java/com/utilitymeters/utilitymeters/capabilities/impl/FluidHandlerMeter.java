package com.utilitymeters.utilitymeters.capabilities.impl;

import com.utilitymeters.powermeter.blockentity.BaseMeterBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidHandlerMeter {
    BaseMeterBlockEntity entity;
    FluidTank tankIn = new FluidTank(1000);
    FluidTank tankOut = new FluidTank(1000);

    public FluidHandlerMeter(BaseMeterBlockEntity entity) {
        this.entity = entity;
    }

    public IFluidHandler getInputTankHandler() {
        return tankIn;
    }

    public IFluidHandler getOutputTankHandler() {
        return tankOut;
    }


    public void saveData(CompoundTag tag) {
        var ioTag = new CompoundTag();
        var outputTankTag = tankIn.writeToNBT(new CompoundTag());
        var inputTankTag = tankOut.writeToNBT(new CompoundTag());

        ioTag.put("in", outputTankTag);
        ioTag.put("out", inputTankTag);
        tag.put("ioData", ioTag);
    }

    public void loadData(CompoundTag tag) {
        if (!tag.contains("ioData")) return;
        var ioTag = tag.getCompound("ioData");
        if (ioTag.contains("in")) {
            var inputTankTag = ioTag.getCompound("in");
            tankIn.readFromNBT(inputTankTag);
        }

        if (ioTag.contains("out")) {
            var outputTankTag = ioTag.getCompound("out");
            tankOut.readFromNBT(outputTankTag);
        }

    }

    public void onTick() {
        if(!entity.hasLevel() || entity.getLevel().isClientSide()) return;

        var max = tankIn.getFluidAmount();
        max = (int)entity.counter().flow(max, true);
        if(max <= 0) return;

        var stack = tankIn.drain( max, IFluidHandler.FluidAction.SIMULATE);
        var inserted = tankOut.fill(stack, IFluidHandler.FluidAction.EXECUTE);
        tankIn.drain(inserted, IFluidHandler.FluidAction.EXECUTE);
        entity.counter().flow(inserted, false);
    }

}
