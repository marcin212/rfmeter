package com.bymarcin.powermeter.blockentity;

import com.bymarcin.powermeter.RfMeterLogic;
import com.bymarcin.powermeter.registry.RfMeterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RfMeterBlockEntity extends BlockEntity {

    public RfMeterLogic logic = new RfMeterLogic(this);

    public RfMeterBlockEntity(BlockPos pos, BlockState state) {
        super(RfMeterBlockEntities.RF_METER.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        logic.saveAdditional(tag);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        logic.load(tag);
        super.load(tag);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        logic.getUpdateTag(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        logic.handleUpdateTag(tag);
        super.handleUpdateTag(tag);
    }

    public void tick() {
        if (level == null) return;
        if (level.isClientSide()) {
            logic.clientTick();
        } else {
            logic.serverTick();
        }
    }


    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return logic.getCapability(cap, side);
    }
}
