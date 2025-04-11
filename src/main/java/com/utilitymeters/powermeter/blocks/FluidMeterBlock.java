package com.utilitymeters.powermeter.blocks;

import com.utilitymeters.powermeter.blockentity.FluidMeterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FluidMeterBlock extends BaseMeterBlock {
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidMeterBlockEntity(blockPos, blockState);
    }


}
