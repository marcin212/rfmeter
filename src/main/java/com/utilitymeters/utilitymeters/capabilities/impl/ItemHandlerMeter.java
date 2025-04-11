package com.utilitymeters.utilitymeters.capabilities.impl;

import com.utilitymeters.powermeter.blockentity.BaseMeterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerMeter {
    BaseMeterBlockEntity entity;
    ItemStackHandler inputStack = new ItemStackHandler();
    ItemStackHandler outputStack = new ItemStackHandler();

    public ItemHandlerMeter(BaseMeterBlockEntity entity) {
        this.entity = entity;
    }

    public ItemStackHandler getInputStackHandler() {
        return inputStack;
    }

    public ItemStackHandler getOutputStackHandler() {
        return outputStack;
    }

    public void saveData(CompoundTag tag) {
        var ioTag = new CompoundTag();
        var outputStackTag = outputStack.serializeNBT();
        var inputStackTag = inputStack.serializeNBT();

        ioTag.put("in", inputStackTag);
        ioTag.put("out", outputStackTag);
        tag.put("ioData", ioTag);
    }

    public void loadData(CompoundTag tag) {
        if(!tag.contains("ioData")) return;
        var ioTag = tag.getCompound("ioData");
        if(ioTag.contains("in")) {
            var inputStackTag = ioTag.getCompound("in");
            inputStack.deserializeNBT(inputStackTag);
        }

        if(ioTag.contains("out")) {
            var outputStackTag = ioTag.getCompound("out");
            outputStack.deserializeNBT(outputStackTag);
        }

    }

    public void onTick() {
        if(!entity.hasLevel() || entity.getLevel().isClientSide()) return;
        var max = inputStack.getStackInSlot(0).getCount();
        max = (int)entity.counter().flow(max, true);
        if(max <= 0) return;

        var stack = inputStack.extractItem(0, max, true);
        var rem = outputStack.insertItem(0, stack, false);
        inputStack.extractItem(0, max - rem.getCount(), false);
        entity.counter().flow(max - rem.getCount(), false);

    }

    public void onBreak(Level level, BlockPos pos) {
        Block.popResource(level, pos, inputStack.getStackInSlot(0));
        Block.popResource(level, pos, outputStack.getStackInSlot(0));
    }

}