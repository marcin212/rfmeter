package com.utilitymeters.powermeter.blockentity;

import com.utilitymeters.logic.CounterLogic;
import com.utilitymeters.logic.smoothers.Average;
import com.utilitymeters.powermeter.registry.RfMeterBlockEntities;
import com.utilitymeters.utilitymeters.capabilities.impl.EnergyStorageMeter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RfMeterBlockEntity extends BaseMeterBlockEntity {
    EnergyStorageMeter energyStorageInput = new EnergyStorageMeter(this, true);
    EnergyStorageMeter energyStorageOutput = new EnergyStorageMeter(this, false);

    public RfMeterBlockEntity(BlockPos pos, BlockState state) {
        super(RfMeterBlockEntities.RF_METER.get(), pos, state, new CounterLogic(new Average(10)));
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(!ForgeCapabilities.ENERGY.equals(cap)) {
            return LazyOptional.empty();
        }

        if (flowDirection().outputDirection().getOpposite() == side) {
            return LazyOptional.of(() -> energyStorageInput).cast();
        }

        if (flowDirection().outputDirection() == side) {
            return LazyOptional.of(() -> energyStorageOutput).cast();
        }

        return LazyOptional.empty();
    }


    @Override
    public void saveData(CompoundTag tag) {

    }

    @Override
    public void loadData(CompoundTag tag) {

    }
}
