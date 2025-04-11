package com.utilitymeters.utilitymeters.capabilities.impl;

import com.utilitymeters.powermeter.blockentity.BaseMeterBlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorageMeter implements IEnergyStorage {
    BaseMeterBlockEntity entity;
    Boolean canReceive;

    public EnergyStorageMeter(BaseMeterBlockEntity entity, boolean canReceive) {
        this.entity = entity;
        this.canReceive = canReceive;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        var cap = entity.targetCapability(ForgeCapabilities.ENERGY);
        if (cap.isPresent()) {
            IEnergyStorage c = cap.orElse(null);
            long newMaxReceive = entity.counter().flow(maxReceive, true);
            long received = c.receiveEnergy((int) newMaxReceive, simulate);
            entity.counter().flow(received, simulate);
            return (int) received;
        }
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return 1000000;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return canReceive;
    }
}
