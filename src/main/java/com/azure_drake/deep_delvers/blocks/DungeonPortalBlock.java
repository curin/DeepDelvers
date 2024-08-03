package com.azure_drake.deep_delvers.blocks;

import com.azure_drake.deep_delvers.blocks.entities.DungeonPortalTileEntity;
import com.azure_drake.deep_delvers.dungeon.DeepDungeon;
import com.azure_drake.deep_delvers.dungeon.DungeonManager;
import com.azure_drake.deep_delvers.portal.DungeonPortal;
import com.azure_drake.deep_delvers.world.DeepDelversData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DungeonPortalBlock extends Block implements Portal, EntityBlock
{
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    public DungeonPortalBlock()
    {
        super(Properties.ofFullCopy(Blocks.NETHER_PORTAL));
        //PortalId = portalId;
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch ((Direction.Axis)pState.getValue(AXIS)) {
            case Z:
                return Z_AXIS_AABB;
            case X:
            default:
                return X_AXIS_AABB;
        }
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    @Override
    protected BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        Direction.Axis direction$axis = pFacing.getAxis();
        Direction.Axis direction$axis1 = pState.getValue(AXIS);
        boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
        return !flag && !pFacingState.is(this) && !new PortalShape(pLevel, pCurrentPos, direction$axis1).isComplete()
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    protected void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        DungeonPortalTileEntity titleEntity = (DungeonPortalTileEntity)pLevel.getBlockEntity(pPos);
        DungeonPortal.BeginTeleport(titleEntity.getPortalId(), pLevel, pPos, pEntity, this);
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        super.destroy(pLevel, pPos, pState);

        if (pLevel.getServer() == null)
        {
            return;
        }

        DungeonPortalTileEntity titleEntity = (DungeonPortalTileEntity)pLevel.getBlockEntity(pPos);
        DeepDelversData data =DeepDelversData.get(pLevel.getServer().getLevel(DungeonManager.DEEP_DUGEON));
        DeepDungeon dungeon = data.getDungeon(titleEntity.getPortalId().DungeonId);

        if (dungeon != null)
        {
            data.AttemptDestroyDungeon(pLevel.getServer(), titleEntity.getPortalId());
        }
    }

    @Override
    public int getPortalTransitionTime(ServerLevel pLevel, Entity pEntity) {
        return pEntity instanceof Player player
                ? 1 : 0;
    }

    @Nullable
    @Override
    public DimensionTransition getPortalDestination(ServerLevel pLevel, Entity pEntity, BlockPos pPos)
    {
        DungeonPortalTileEntity titleEntity = (DungeonPortalTileEntity)pLevel.getBlockEntity(pPos);
        return DungeonPortal.GetTransition(titleEntity.getPortalId(), pLevel, pEntity, pPos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DungeonPortalTileEntity(pPos, pState);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextInt(100) == 0) {
            pLevel.playLocalSound(
                    (double)pPos.getX() + 0.5,
                    (double)pPos.getY() + 0.5,
                    (double)pPos.getZ() + 0.5,
                    SoundEvents.PORTAL_AMBIENT,
                    SoundSource.BLOCKS,
                    0.1F,
                    pRandom.nextFloat() * 0.4F + 0.8F,
                    false
            );
        }

        for (int i = 0; i < 4; i++) {
            double d0 = (double)pPos.getX() + pRandom.nextDouble();
            double d1 = (double)pPos.getY() + pRandom.nextDouble();
            double d2 = (double)pPos.getZ() + pRandom.nextDouble();
            double d3 = ((double)pRandom.nextFloat() - 0.5) * 0.5;
            double d4 = ((double)pRandom.nextFloat() - 0.5) * 0.5;
            double d5 = ((double)pRandom.nextFloat() - 0.5) * 0.5;
            int j = pRandom.nextInt(2) * 2 - 1;
            if (!pLevel.getBlockState(pPos.west()).is(this) && !pLevel.getBlockState(pPos.east()).is(this)) {
                d0 = (double)pPos.getX() + 0.5 + 0.25 * (double)j;
                d3 = (double)(pRandom.nextFloat() * 2.0F * (float)j);
            } else {
                d2 = (double)pPos.getZ() + 0.5 + 0.25 * (double)j;
                d5 = (double)(pRandom.nextFloat() * 2.0F * (float)j);
            }

            pLevel.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader pLevel, BlockPos pPos, BlockState pState) {
        return ItemStack.EMPTY;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed blockstate.
     * @deprecated call via {@link net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase#rotate} whenever possible. Implementing/overriding is fine.
     */
    @Override
    protected BlockState rotate(BlockState pState, Rotation pRot) {
        switch (pRot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((Direction.Axis)pState.getValue(AXIS)) {
                    case Z:
                        return pState.setValue(AXIS, Direction.Axis.X);
                    case X:
                        return pState.setValue(AXIS, Direction.Axis.Z);
                    default:
                        return pState;
                }
            default:
                return pState;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AXIS);
    }
}
