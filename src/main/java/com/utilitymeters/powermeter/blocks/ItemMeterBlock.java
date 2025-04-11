package com.utilitymeters.powermeter.blocks;

import com.utilitymeters.powermeter.blockentity.ItemMeterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ItemMeterBlock extends BaseMeterBlock {

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ItemMeterBlockEntity(blockPos, blockState);
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        var entity = (ItemMeterBlockEntity)level.getBlockEntity(pos);
        entity.onBreak(level, pos);
        super.onBlockExploded(state, level, pos, explosion);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        var entity = (ItemMeterBlockEntity) blockEntity;
        entity.onBreak(level, pos);
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

}
