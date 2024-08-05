package com.azure_drake.deep_delvers.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DungeonPortalFrame extends Block {

    public static final EnumProperty<ConnectedPillarState> CONNECTED_STATE = EnumProperty.create("connected_state", ConnectedPillarState.class, ConnectedPillarState.Cap, ConnectedPillarState.Single, ConnectedPillarState.Edge, ConnectedPillarState.Middle);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static DungeonPortalFrame Standard() { return new DungeonPortalFrame(true); }
    public static DungeonPortalFrame Cosmetic() { return new DungeonPortalFrame(false); }

    public DungeonPortalFrame(boolean Invulnerable) {
        super((Invulnerable ? Properties.ofFullCopy(Blocks.BEDROCK) : Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS).destroyTime(14.0f)).lightLevel(state -> state.getValue(CONNECTED_STATE) == ConnectedPillarState.Middle ? 14: 0));
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {

        Direction.Axis axis = state.getValue(FACING).getAxis();
        switch (direction) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                axis = switch (axis) {
                    case X -> Direction.Axis.Z;
                    case Z -> Direction.Axis.X;
                    default -> axis;
                };
            default:
                break;
        }
        return getBlockState(state, pos, level, axis);
    }

    @NotNull
    private BlockState getBlockState(BlockState state, BlockPos pos, LevelAccessor level, Direction.Axis axis) {
        int above = 0;
        int below = 0;

        ArrayList<StateData> statesToChange = new ArrayList<>();
        BlockPos tempPos = pos.relative(axis, above + 1);
        BlockState tempState = level.getBlockState(tempPos);
        while (tempState.is(this))
        {
            if (tempState.getValue(FACING).getAxis() == axis || tempState.getValue(CONNECTED_STATE) == ConnectedPillarState.Single)
            {
                statesToChange.add(new StateData(above + 1, tempState, tempPos));
            }
            tempPos = pos.relative(axis, ++above + 1);
            tempState = level.getBlockState(tempPos);
        }

        tempPos = pos.relative(axis, -(below + 1));
        tempState = level.getBlockState(tempPos);
        while (tempState.is(this))
        {
            if (tempState.getValue(FACING).getAxis() == axis || tempState.getValue(CONNECTED_STATE) == ConnectedPillarState.Single)
            {
                statesToChange.add(new StateData(-below - 1, tempState, tempPos));
            }

            tempPos = pos.relative(axis, -(++below + 1));
            tempState = level.getBlockState(tempPos);
        }

        state = setBlockState(state, above, below, axis);

        for(StateData stateToChange : statesToChange)
        {
            int tempAbove = above - stateToChange.DistanceAbove;
            int tempBelow = below + stateToChange.DistanceAbove;

            level.setBlock(stateToChange.Position, setBlockState(stateToChange.State, tempAbove, tempBelow, axis), UPDATE_ALL_IMMEDIATE);
        }

        return state;
    }

    private BlockState setBlockState(BlockState state, int above, int below, Direction.Axis axis)
    {
        state = state.setValue(FACING, Direction.get(below < above ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE, axis));

        if (above == 0 && below == 0)
        {
            state = state.setValue(CONNECTED_STATE, ConnectedPillarState.Single);
        }
        else if (above == 0 || below == 0)
        {
            state = state.setValue(CONNECTED_STATE, ConnectedPillarState.Cap);
        }
        else if (above == below)
        {
            state = state.setValue(CONNECTED_STATE, ConnectedPillarState.Middle);
        }
        else
        {
            state = state.setValue(CONNECTED_STATE, ConnectedPillarState.Edge);
        }
        return state;
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        super.destroy(pLevel, pPos, pState);

        Direction.Axis axis = pState.getValue(FACING).getAxis();
        int above = 0;
        int below = 0;

        ArrayList<StateData> statesToChange = new ArrayList<>();
        BlockPos tempPos = pPos.relative(axis, above + 1);
        BlockState tempState = pLevel.getBlockState(tempPos);
        while (tempState.is(this))
        {
            if (tempState.getValue(FACING).getAxis() == axis || tempState.getValue(CONNECTED_STATE) == ConnectedPillarState.Single)
            {
                statesToChange.add(new StateData(above + 1, tempState, tempPos));
            }

            tempPos = pPos.relative(axis, ++above + 1);
            tempState = pLevel.getBlockState(tempPos);
        }

        tempPos = pPos.relative(axis, -(below + 1));
        tempState = pLevel.getBlockState(tempPos);
        while (tempState.is(this))
        {
            if (tempState.getValue(FACING).getAxis() == axis || tempState.getValue(CONNECTED_STATE) == ConnectedPillarState.Single)
            {
                statesToChange.add(new StateData(-below - 1, tempState, tempPos));
            }

            tempPos = pPos.relative(axis, -(++below + 1));
            tempState = pLevel.getBlockState(tempPos);
        }

        for(StateData stateToChange : statesToChange)
        {
            int tempAbove = 0;
            int tempBelow = 0;

            if (stateToChange.DistanceAbove > 0)
            {
                tempAbove = above - stateToChange.DistanceAbove;
                tempBelow = stateToChange.DistanceAbove - 1;
            }
            else
            {
                tempBelow = below + stateToChange.DistanceAbove;
                tempAbove = -stateToChange.DistanceAbove - 1;
            }

            pLevel.setBlock(stateToChange.Position, setBlockState(stateToChange.State, tempAbove, tempBelow, axis), UPDATE_ALL_IMMEDIATE);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(CONNECTED_STATE);
        pBuilder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext)
    {
        return getBlockState(this.defaultBlockState(), pContext.getClickedPos(), pContext.getLevel(), pContext.getClickedFace().getAxis());
    }

    public class StateData
    {
        public StateData(int distAbove, BlockState state, BlockPos pos)
        {
            DistanceAbove = distAbove;
            State = state;
            Position = pos;
        }
        public BlockState State;
        public int DistanceAbove;
        public BlockPos Position;
    }
}
