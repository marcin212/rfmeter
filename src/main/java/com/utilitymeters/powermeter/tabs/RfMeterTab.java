package com.utilitymeters.powermeter.tabs;

import com.utilitymeters.powermeter.registry.RfMeterBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RfMeterTab extends CreativeModeTab {

    public RfMeterTab(String label) {
        super(label);
    }

    @Override
    public @NotNull ItemStack makeIcon() {
        return new ItemStack(RfMeterBlocks.RF_METER.get());
    }
}
