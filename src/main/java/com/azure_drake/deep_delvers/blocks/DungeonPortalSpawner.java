package com.azure_drake.deep_delvers.blocks;

import com.azure_drake.deep_delvers.Config;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

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

        DeepDelversData data = DeepDelversData.get(pLevel.getServer().getLevel(DungeonManager.DEEP_DUGEON));
        DungeonID id = DungeonPortal.GetDungeonId(pPos);
        DeepDungeon dungeon = data.getDungeon(id);

        Optional<DungeonPortalShape> optional = DungeonPortalShape.findEmptyPortalShape(pLevel, pPos, Direction.Axis.X);

        if (optional.isEmpty()) {
            pLevel.destroyBlock(pPos, false);
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

        if (portalID == null)
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

                PortalID myPortal = dungeon.CreateNewPortalInside(pLevel, optional.get().getRectangle(), optional.get().getAxis(), connected);

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
}
