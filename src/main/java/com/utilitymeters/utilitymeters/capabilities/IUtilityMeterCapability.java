package com.utilitymeters.utilitymeters.capabilities;

//TODO
//@AutoRegisterCapability
public interface IUtilityMeterCapability {
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
