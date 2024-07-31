package com.azure_drake.deep_delvers.blocks;

import com.azure_drake.deep_delvers.blocks.entities.DungeonPortalTileEntity;
import com.azure_drake.deep_delvers.dungeon.DeepDungeon;
import com.azure_drake.deep_delvers.dungeon.DungeonManager;
import com.azure_drake.deep_delvers.portal.DungeonPortal;
import com.azure_drake.deep_delvers.world.DeepDelversData;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DungeonPortalSpawner extends Block
{
    public DungeonPortalSpawner() {
        super(Properties.ofFullCopy(Blocks.STRUCTURE_BLOCK));
    }

    @Override
    protected void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);

        if (pLevel.dimension() != DungeonManager.DEEP_DUGEON)
        {
            return;
        }

        Direction.Axis axis = Direction.Axis.X;
        if (pLevel.getBlockState(pPos.offset(0,0,1)).is(BlockManager.DUNGEON_PORTAL) ||
                pLevel.getBlockState(pPos.offset(0,0,1)).is(BlockManager.DUNGEON_PORTAL_SPAWNER) ||
                pLevel.getBlockState(pPos.offset(0,0,-1)).is(BlockManager.DUNGEON_PORTAL) ||
                pLevel.getBlockState(pPos.offset(0,0,-1)).is(BlockManager.DUNGEON_PORTAL_SPAWNER))
        {
            axis = Direction.Axis.Z;
        }

        BlockState blockstate = BlockManager.DUNGEON_PORTAL.get().defaultBlockState().setValue(DungeonPortalBlock.AXIS, axis);

        pLevel.setBlock(pPos, blockstate, 18);
        DungeonPortalTileEntity tile = (DungeonPortalTileEntity)pLevel.getBlockEntity(pPos);
        tile.setPortalId(DungeonPortal.GetPortalId(pPos));

        DeepDelversData data = DeepDelversData.get(pLevel.getServer().getLevel(DungeonManager.DEEP_DUGEON));
        DeepDungeon dungeon = data.getDungeon(tile.getPortalId());

        BlockPos offset = dungeon.Portal.DungeonBounds.minCorner.offset(-pPos.getX(), -pPos.getY(), -pPos.getZ());
        if (dungeon.Portal.DungeonAxis == Direction.Axis.Y)
        {
            if (dungeon.Portal.DungeonBounds.minCorner == DungeonPortal.GetDungeonRectFromId(tile.getPortalId()).minCorner) {
                dungeon.Portal.DungeonBounds = new BlockUtil.FoundRectangle(pPos.offset(0, -1, 0), 3, 1);
                data.putDungeon(tile.getPortalId(), dungeon);
                return;
            }

            if (offset.getX() != 0 && offset.getZ() != 0)
            {
                pLevel.destroyBlock(pPos, false);
            }

            if (offset.getX() != 0)
            {
                dungeon.Portal.DungeonAxis = Direction.Axis.X;
                dungeon.Portal.DungeonBounds = new BlockUtil.FoundRectangle(dungeon.Portal.DungeonBounds.minCorner.offset(-1, 0, 0), dungeon.Portal.DungeonBounds.axis1Size, dungeon.Portal.DungeonBounds.axis2Size + 2);
            }

            if (offset.getZ() != 0)
            {
                dungeon.Portal.DungeonAxis = Direction.Axis.X;
                dungeon.Portal.DungeonBounds = new BlockUtil.FoundRectangle(dungeon.Portal.DungeonBounds.minCorner.offset(0, 0, -1), dungeon.Portal.DungeonBounds.axis1Size, dungeon.Portal.DungeonBounds.axis2Size + 2);
            }
        }

        offset = dungeon.Portal.DungeonBounds.minCorner.offset(-pPos.getX(), -pPos.getY(), -pPos.getZ());
        if (dungeon.Portal.DungeonAxis == Direction.Axis.X) {
            if (offset.getX() < 0) {
                dungeon.Portal.DungeonBounds = new BlockUtil.FoundRectangle(pPos.offset(-1, 0, 0), dungeon.Portal.DungeonBounds.axis1Size, dungeon.Portal.DungeonBounds.axis2Size + offset.getX() + 1);
            }
            else
            {
                dungeon.Portal.DungeonBounds = new BlockUtil.FoundRectangle(dungeon.Portal.DungeonBounds.minCorner, dungeon.Portal.DungeonBounds.axis1Size, dungeon.Portal.DungeonBounds.axis2Size + offset.getX());
            }
        }

        if (dungeon.Portal.DungeonAxis == Direction.Axis.Z) {
            if (offset.getZ() < 0) {
                dungeon.Portal.DungeonBounds = new BlockUtil.FoundRectangle(pPos.offset(0, 0, -1), dungeon.Portal.DungeonBounds.axis1Size, dungeon.Portal.DungeonBounds.axis2Size + offset.getZ() + 1);
            }
            else
            {
                dungeon.Portal.DungeonBounds = new BlockUtil.FoundRectangle(dungeon.Portal.DungeonBounds.minCorner, dungeon.Portal.DungeonBounds.axis1Size, dungeon.Portal.DungeonBounds.axis2Size + offset.getZ());
            }
        }

        if (offset.getY() < 0) {
            dungeon.Portal.DungeonBounds = new BlockUtil.FoundRectangle(pPos.offset(0, -1, 0), dungeon.Portal.DungeonBounds.axis1Size + offset.getY() + 1, dungeon.Portal.DungeonBounds.axis2Size);
        }
        else
        {
            dungeon.Portal.DungeonBounds = new BlockUtil.FoundRectangle(dungeon.Portal.DungeonBounds.minCorner, dungeon.Portal.DungeonBounds.axis1Size + offset.getY(), dungeon.Portal.DungeonBounds.axis2Size);
        }

        data.putDungeon(tile.getPortalId(), dungeon);
    }
}
