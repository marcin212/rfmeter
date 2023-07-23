package com.bymarcin.powermeter.blocks;

import com.bymarcin.powermeter.blockentity.RfMeterBlockEntity;
import com.bymarcin.powermeter.containers.RfMeterContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RfMeterBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<FlowDirection> FLOW_DIRECTION = EnumProperty.create("flow_direction", FlowDirection.class);

    public enum FlowDirection implements StringRepresentable {
        UP_DOWN("up_down"),
        DOWN_UP("down_up");

        private final String id;
        FlowDirection(String id) {
            this.id = id;
        }

        @Override
        public String getSerializedName() {
            return id;
        }

        public FlowDirection getOpposite() {
            return switch (this) {
                case UP_DOWN -> DOWN_UP;
                case DOWN_UP -> UP_DOWN;
            };
        }
    }

    public static final VoxelShape[] boundingBox = new VoxelShape[]{
            Shapes.create(2/16F-0.001F, 0, 0, 14/16F+0.001F, 1, 14/16F+0.001F),
            Shapes.create(0, 0, 2/16F-0.001F, 14/16F+0.001F, 1, 14/16F+0.001F),
            Shapes.create(2/16F-0.001F, 0, 2/16F-0.001F, 14/16F+0.001F, 1, 1),
            Shapes.create(2/16F-0.001F, 0, 2/16F-0.001F, 1, 1, 14/16F+0.001F)
    };

    public RfMeterBlock() {
        super(Properties.of(Material.METAL).sound(SoundType.METAL));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        var dir = blockState.getValue(FACING);
        return switch (dir) {
            case SOUTH -> boundingBox[0];
            case EAST -> boundingBox[1];
            case NORTH -> boundingBox[2];
            default -> boundingBox[3];
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var direction = Arrays.stream(context.getNearestLookingDirections()).filter(Direction.Plane.HORIZONTAL).findFirst();
        return defaultBlockState().setValue(FACING, direction.orElse(Direction.SOUTH).getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(FLOW_DIRECTION);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RfMeterBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide) return InteractionResult.SUCCESS;
        if(player.isShiftKeyDown()) {
            level.setBlock(pos, state.setValue(FLOW_DIRECTION, state.getValue(FLOW_DIRECTION).getOpposite()), 3);
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RfMeterBlockEntity) {
                MenuProvider menuProvider = RfMeterContainer.createMenuProvider(pos);
                NetworkHooks.openScreen((ServerPlayer) player, menuProvider, pos);
            } else {
                throw new IllegalStateException("Invalid block entity type!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, blockState, t) -> {
            if (t instanceof RfMeterBlockEntity tile) {
                tile.tick();
            }
        };
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
        var entity = level.getBlockEntity(pos);
        if(entity instanceof RfMeterBlockEntity tile) {
            tile.logic.updateRedstone();
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return (direction!=null && direction.getOpposite()== state.getValue(FACING)) || super.canConnectRedstone(state, level, pos, direction);
    }
}
