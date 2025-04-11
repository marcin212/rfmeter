package com.utilitymeters.powermeter.blocks;

import com.utilitymeters.powermeter.blockentity.RfMeterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RfMeterBlock extends BaseMeterBlock {

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RfMeterBlockEntity(pos, state);
    }
}
