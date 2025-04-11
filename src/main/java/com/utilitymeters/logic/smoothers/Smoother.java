package com.utilitymeters.logic.smoothers;

public interface Smoother {
    void putValue(long value);
    float smoothedValue();
}
