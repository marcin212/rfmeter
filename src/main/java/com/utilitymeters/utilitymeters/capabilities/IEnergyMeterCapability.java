package com.utilitymeters.utilitymeters.capabilities;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

//TODO
//@AutoRegisterCapability
public interface IEnergyMeterCapability {
    boolean isInCounterMode();

    void setCounterMod(Boolean inCounterMode);

    float[] getColor();

    void setTransferLimit(int transferLimit);

    int getTransferLimit();

    int getTransfer();

    long getCurrentValue();

    void setPassword(String pass);

    void removePassword();

    boolean canEdit(String pass);

    boolean isProtected();

    boolean isOn();

    long calculatedPrepaid();

    void addTopUp(long value);

    void subTopUp(long value);

    long getPrepaidValue();
}
