package com.azure_drake.deep_delvers.blocks;

import com.azure_drake.deep_delvers.blocks.entities.HiddenTripwireHookTileEntity;
import com.google.common.base.MoreObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HiddenTripwireHook extends DeepDelversBlock implements EntityBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;

    public HiddenTripwireHook(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HiddenTripwireHookTileEntity(pos, state);
    }

    //
    //Tripwire Hook Starts
    //

    public static void calculateState(
            Level level, BlockPos pos, BlockState hookState, boolean attaching, boolean shouldNotifyNeighbours, int searchRange, @javax.annotation.Nullable BlockState state
    ) {
        for (Direction direction : Direction.values())
        {
            boolean flag = hookState.getOptionalValue(ATTACHED).orElse(false);
            boolean flag1 = hookState.getOptionalValue(POWERED).orElse(false);
            Block block = hookState.getBlock();
            boolean flag2 = !attaching;
            boolean flag3 = false;
            int i = 0;
            BlockState[] ablockstate = new BlockState[42];

            for (int j = 1; j < 42; j++) {
                BlockPos blockpos = pos.relative(direction, j);
                BlockState blockstate = level.getBlockState(blockpos);
                if (blockstate.is(DeepBlockTags.TRIPWIRE_HOOK)) {
                    i = j;
                    break;
                }

                if (!blockstate.is(DeepBlockTags.TRIPWIRE_HOOK) && j != searchRange) {
                    ablockstate[j] = null;
                    flag2 = false;
                } else {
                    if (j == searchRange) {
                        blockstate = MoreObjects.firstNonNull(state, blockstate);
                    }

                    boolean flag4 = !blockstate.getValue(TripWireBlock.DISARMED);
                    boolean flag5 = blockstate.getValue(TripWireBlock.POWERED);
                    flag3 |= flag4 && flag5;
                    ablockstate[j] = blockstate;
                    if (j == searchRange) {
                        level.scheduleTick(pos, block, 10);
                        flag2 &= flag4;
                    }
                }
            }

            flag2 &= i > 1;
            flag3 &= flag2;
            BlockState blockstate1 = block.defaultBlockState().trySetValue(ATTACHED, Boolean.valueOf(flag2)).trySetValue(POWERED, Boolean.valueOf(flag3));
            if (i > 0) {
                BlockPos blockpos1 = pos.relative(direction, i);
                Direction direction1 = direction.getOpposite();
                level.setBlock(blockpos1, blockstate1, 3);
                notifyNeighbors(block, level, blockpos1, direction1);
                emitState(level, blockpos1, flag2, flag3, flag, flag1);
            }

            emitState(level, pos, flag2, flag3, flag, flag1);
            if (!attaching) {
                level.setBlock(pos, blockstate1, 3);
                if (shouldNotifyNeighbours) {
                    notifyNeighbors(block, level, pos, direction);
                }
            }

            if (flag != flag2) {
                for (int k = 1; k < i; k++) {
                    BlockPos blockpos2 = pos.relative(direction, k);
                    BlockState blockstate2 = ablockstate[k];
                    if (blockstate2 != null) {
                        BlockState blockstate3 = level.getBlockState(blockpos2);
                        if (blockstate3.is(Blocks.TRIPWIRE) || blockstate3.is(Blocks.TRIPWIRE_HOOK)) {
                            level.setBlock(blockpos2, blockstate2.trySetValue(ATTACHED, Boolean.valueOf(flag2)), 3);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void tick(BlockState p_222610_, ServerLevel p_222611_, BlockPos p_222612_, RandomSource p_222613_) {
        calculateState(p_222611_, p_222612_, p_222610_, false, true, -1, null);
    }

    private static void emitState(Level level, BlockPos pos, boolean attached, boolean powered, boolean wasAttached, boolean wasPowered) {
        if (powered && !wasPowered) {
            level.playSound(null, pos, SoundEvents.TRIPWIRE_CLICK_ON, SoundSource.BLOCKS, 0.4F, 0.6F);
            level.gameEvent(null, GameEvent.BLOCK_ACTIVATE, pos);
        } else if (!powered && wasPowered) {
            level.playSound(null, pos, SoundEvents.TRIPWIRE_CLICK_OFF, SoundSource.BLOCKS, 0.4F, 0.5F);
            level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
        } else if (attached && !wasAttached) {
            level.playSound(null, pos, SoundEvents.TRIPWIRE_ATTACH, SoundSource.BLOCKS, 0.4F, 0.7F);
            level.gameEvent(null, GameEvent.BLOCK_ATTACH, pos);
        } else if (!attached && wasAttached) {
            level.playSound(null, pos, SoundEvents.TRIPWIRE_DETACH, SoundSource.BLOCKS, 0.4F, 1.2F / (level.random.nextFloat() * 0.2F + 0.9F));
            level.gameEvent(null, GameEvent.BLOCK_DETACH, pos);
        }
    }

    private static void notifyNeighbors(Block block, Level level, BlockPos pos, Direction p_direction) {
        Direction direction = p_direction.getOpposite();
        Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, direction, Direction.UP);
        level.updateNeighborsAt(pos, block, orientation);
        level.updateNeighborsAt(pos.relative(direction), block, orientation);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.is(newState.getBlock())) {
            boolean flag = state.getValue(ATTACHED);
            boolean flag1 = state.getValue(POWERED);
            if (flag || flag1) {
                calculateState(level, pos, state, true, false, -1, null);
            }

            if (flag1) {
                for (Direction direction : Direction.values()) {
                    notifyNeighbors(this, level, pos, direction);
                }
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    protected int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        if (!blockState.getValue(POWERED)) {
            return 0;
        } else {
            return 15;
        }
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    //
    //Tripwire Hook Ends
    //
}
