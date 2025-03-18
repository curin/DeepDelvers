package com.azure_drake.deep_delvers.blocks.entities;

import com.azure_drake.deep_delvers.Config;
import com.azure_drake.deep_delvers.blocks.BlockManager;
import com.azure_drake.deep_delvers.dungeon.DeepDungeon;
import com.azure_drake.deep_delvers.dungeon.DungeonID;
import com.azure_drake.deep_delvers.dungeon.DungeonManager;
import com.azure_drake.deep_delvers.portal.DungeonPortal;
import com.azure_drake.deep_delvers.portal.DungeonPortalShape;
import com.azure_drake.deep_delvers.portal.PortalID;
import com.azure_drake.deep_delvers.portal.PortalState;
import com.azure_drake.deep_delvers.world.DeepDelversData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public class DungeonPortalSpawnerTileEntity extends BlockEntity
{
    public DungeonPortalSpawnerTileEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockManager.DUNGEON_PORTAL_SPAWNER_ENTITY.get(), pPos, pBlockState);
    }

    // The signature of this method matches the signature of the BlockEntityTicker functional interface.
    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity)
    {
        if (level.isClientSide())
        {
            return;
        }

        ServerLevel sLevel = (ServerLevel) level;

        if (sLevel == null)
        {
            return;
        }

        attemptUpdate(state, sLevel, pos);
    }

    private static void attemptUpdate(BlockState pState, ServerLevel pLevel, BlockPos pPos)
    {
        if (pLevel.dimension() != DungeonManager.DEEP_DUGEON)
        {
            return;
        }

        DestroySpawnerBlocks(pLevel, pPos);

        DeepDelversData data = DeepDelversData.get(pLevel.getServer().getLevel(DungeonManager.DEEP_DUGEON));
        DungeonID id = DungeonPortal.GetDungeonId(pPos);
        DeepDungeon dungeon = data.getDungeon(id);

        Optional<DungeonPortalShape> optional = DungeonPortalShape.findEmptyPortalShape(pLevel, pPos, Direction.Axis.X);

        if (optional.isEmpty()) {
            return;
        }

        PortalID portalID = null;
        List<DungeonPortal> portals = dungeon.Portals;
        for (int i = 0, portalsSize = portals.size(); i < portalsSize; i++) {
            DungeonPortal portal = portals.get(i);
            if (portal.State == PortalState.Unconnected_To_Dungeon) {
                portalID = new PortalID(id, i);
                portal.DungeonBounds = optional.get().getRectangle();
                portal.DungeonAxis = optional.get().getAxis();
                portal.State = PortalState.Connected;

                if (portal.DungeonLink.Id != -1)
                {
                    DeepDungeon dungeon2 = data.getDungeon(portal.DungeonLink.DungeonId);
                    DungeonPortal portal2 = dungeon2.Portals.get(portal.DungeonLink.Id);
                    portal2.LevelBounds = optional.get().getRectangle();
                    portal2.LevelAxis = optional.get().getAxis();
                    portal2.State = PortalState.Connected;
                }

                break;
            }
        }

        if (portalID == null && false)
        {
            if (pLevel.random.nextInt(100) < Config.InDungeonNexusChance)
            {
                //TODO: Spawn Nexus or Connect to Nexus
            }
            else {
                int depth = dungeon.Depth + (pLevel.random.nextInt(100) > 75 ? 1 : 0);
                int tier = id.Tier;
                if (depth > 4) {
                    depth = 0;
                    tier++;
                }

                PortalID connected = optional.get().createPortalBlocks(tier, depth);

                if (portalID.Id == -1)
                {
                    return;
                }

                PortalID myPortal = dungeon.CreateNewPortalInside(pLevel, optional.get().getRectangle(), optional.get().getAxis(), connected);

                if (myPortal.Id == -1)
                {
                    return;
                }

                DeepDungeon dungeon2 = data.getDungeon(connected.DungeonId);
                dungeon2.Portals.get(connected.Id).DungeonLink = myPortal;
            }
        }
        else
        {
            optional.get().createPortalBlocks(portalID);
        }

        data.putDungeon(id, dungeon);
    }

    private static void DestroySpawnerBlocks(ServerLevel pLevel, BlockPos pPos)
    {
        BlockState state = pLevel.getBlockState(pPos);
        if (state.is(BlockManager.DUNGEON_PORTAL_SPAWNER.get()))
        {
            pLevel.destroyBlock(pPos, false);
            DestroySpawnerBlocks(pLevel, pPos.above());
            DestroySpawnerBlocks(pLevel, pPos.below());
            DestroySpawnerBlocks(pLevel, pPos.east());
            DestroySpawnerBlocks(pLevel, pPos.west());
            DestroySpawnerBlocks(pLevel, pPos.north());
            DestroySpawnerBlocks(pLevel, pPos.south());
        }
    }
}
