package com.utilitymeters.logic.smoothers;

import java.util.List;

public class Average implements Smoother{
    long[] values;
    int index = 0;
    long sum = 0;

    public Average(int size) {
        values = new long[size];
    }

    @Override
    public void putValue(long value) {
        long oldValue = values[index];
        values[index] = value;
        sum += value - oldValue;
        index = (index+1) % values.length;
    }

    @Override
    public float smoothedValue() {
        return sum/(float)values.length;
    }
}
