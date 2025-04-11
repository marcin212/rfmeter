package com.utilitymeters.utils;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum FlowDirection implements StringRepresentable {
    UP_DOWN("up_down", Direction.DOWN),
    DOWN_UP("down_up", Direction.UP);

    private final String id;
    private final Direction outputDirection;

    FlowDirection(@NotNull String id, @NotNull Direction outputDirection) {
        this.id = id;
        this.outputDirection = outputDirection;
    }

    @Override
    public @NotNull String getSerializedName() {
        return id;
    }

    public FlowDirection getOpposite() {
        return switch (this) {
            case UP_DOWN -> DOWN_UP;
            case DOWN_UP -> UP_DOWN;
        };
    }

    public Direction outputDirection() {
        return outputDirection;
    }

    public Direction inputDirection() {
        return outputDirection().getOpposite();
    }
}