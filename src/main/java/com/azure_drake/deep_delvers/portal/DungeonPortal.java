package com.azure_drake.deep_delvers.portal;

import com.azure_drake.deep_delvers.blocks.DeepBlockTags;
import com.azure_drake.deep_delvers.dungeon.DeepDungeon;
import com.azure_drake.deep_delvers.dungeon.DungeonID;
import com.azure_drake.deep_delvers.dungeon.DungeonManager;
import com.azure_drake.deep_delvers.world.DeepDelversData;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;

public class DungeonPortal
{
    public DungeonPortal(ResourceKey<Level> level, BlockUtil.FoundRectangle levelBounds, Direction.Axis levelAxis,
                         BlockUtil.FoundRectangle dungeonBounds, Direction.Axis dungeonAxis)
    {
        Level = level;
        LevelBounds = levelBounds;
        LevelAxis = levelAxis;
        DungeonBounds = dungeonBounds;
        DungeonAxis = dungeonAxis;
        DungeonLink = new PortalID(new DungeonID(-1, -1), -1);
    }

    public DungeonPortal(ResourceKey<Level> level, BlockUtil.FoundRectangle levelBounds, Direction.Axis levelAxis,
                         BlockUtil.FoundRectangle dungeonBounds, Direction.Axis dungeonAxis, PortalID dungeonLink)
    {
        Level = level;
        LevelBounds = levelBounds;
        LevelAxis = levelAxis;
        DungeonBounds = dungeonBounds;
        DungeonAxis = dungeonAxis;
        DungeonLink = dungeonLink;
    }

    public ResourceKey<Level> Level;
    public BlockUtil.FoundRectangle LevelBounds;
    public Direction.Axis LevelAxis;
    public BlockUtil.FoundRectangle DungeonBounds;
    public Direction.Axis DungeonAxis;
    public PortalID DungeonLink;
    public PortalState State = PortalState.Unconnected_To_Dungeon;
    public boolean Tethered = false;

    public void Destroy(MinecraftServer server)
    {
        if (State == PortalState.Destroyed)
        {
            return;
        }
        State = PortalState.Destroyed;
        ServerLevel level = server.getLevel(Level);
        Direction rightDir = LevelAxis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        BlockPos.betweenClosed(LevelBounds.minCorner, LevelBounds.minCorner.relative(Direction.UP, LevelBounds.axis1Size - 1).relative(rightDir, LevelBounds.axis2Size - 1))
                .forEach(p_77725_ ->
                {
                    level.destroyBlock(p_77725_, false);
                });

        ServerLevel deeplevel = server.getLevel(DungeonManager.DEEP_DUGEON);
        rightDir = DungeonAxis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        BlockPos.betweenClosed(DungeonBounds.minCorner, DungeonBounds.minCorner.relative(Direction.UP, DungeonBounds.axis1Size - 1).relative(rightDir, DungeonBounds.axis2Size - 1))
                .forEach(p_77725_ ->
                {
                    deeplevel.destroyBlock(p_77725_, false);
                });

        if (DungeonLink.Id != -1)
        {
            DeepDelversData data = DeepDelversData.get(server.getLevel(DungeonManager.DEEP_DUGEON));
            data.DestroyPortal(server, DungeonLink);
        }
    }

    private static final int DungeonDistance = 10000;
    private static final int DungeonMaxIDX = 15000000 / 10000;

    public static DungeonID GetDungeonId(BlockPos pPos)
    {
        return new DungeonID(((5000 + pPos.getX()) / DungeonDistance) + ((5000 + pPos.getZ()) / DungeonDistance * DungeonMaxIDX), 4 - ((pPos.getY() + 415) / 75));
    }

    public static BlockUtil.FoundRectangle GetDungeonRectFromId(PortalID id)
    {
        return new BlockUtil.FoundRectangle(new BlockPos((id.DungeonId.Id % DungeonMaxIDX) * DungeonDistance, -415 + ((4 - id.DungeonId.Tier) * 75),(id.DungeonId.Id / DungeonDistance) * DungeonDistance), 3, 3);
    }

    public static DimensionTransition GetTransition(PortalID portalId, ServerLevel pLevel, Entity pEntity, BlockPos pPos)
    {
        DeepDungeon dungeonData = DeepDelversData.get(pLevel.getServer().getLevel(DungeonManager.DEEP_DUGEON)).getDungeon(portalId.DungeonId);

        if (dungeonData == null)
        {
            if (pLevel.dimension() == DungeonManager.DEEP_DUGEON)
            {
                return getDimensionTransitionFromExit(pEntity, pPos,
                        new BlockUtil.FoundRectangle(new BlockPos(0,0,0), 5, 5), pLevel.getServer().getLevel(net.minecraft.world.level.Level.OVERWORLD),
                        DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET));
            }
            else
            {
                return getDimensionTransitionFromExit(pEntity, pPos,
                        new BlockUtil.FoundRectangle(new BlockPos(0,0,0), 5, 5), pLevel.getServer().getLevel(DungeonManager.DEEP_DUGEON),
                        DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET));
            }
        }

        DungeonPortal portal = dungeonData.Portals.get(portalId.Id);
        if (pLevel.dimension() == DungeonManager.DEEP_DUGEON && IsInPortal(pPos, portal.DungeonBounds, new Vec3(1, 0, 0)))
        {
            return getDimensionTransitionFromExit(pEntity, pPos,
                    portal.LevelBounds, pLevel.getServer().getLevel(portal.Level),
                    DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET));
        }

        return getDimensionTransitionFromExit(pEntity, pPos,
                portal.DungeonBounds, pLevel.getServer().getLevel(DungeonManager.DEEP_DUGEON),
                DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET));
    }

    public static void BeginTeleport(PortalID portalId, Level pLevel, BlockPos pPos, Entity pEntity, Portal portal)
    {
        if (!pEntity.canUsePortal(false) || pLevel.getServer() == null || pLevel.isClientSide)
        {
            return;
        }

        DeepDelversData data = DeepDelversData.get(pLevel.getServer().getLevel(DungeonManager.DEEP_DUGEON));

        if (!data.containsDungeon(portalId.DungeonId))
        {
            pLevel.destroyBlock(pPos, false);
        }

        DeepDungeon dungeonData = data.getDungeon(portalId.DungeonId);

        if (dungeonData.Portals.size() <= portalId.Id)
        {
            pLevel.destroyBlock(pPos, false);
        }

        DungeonPortal dungeonPortal = dungeonData.Portals.get(portalId.Id);

        if (dungeonData == null || dungeonPortal.Level != DungeonManager.DEEP_DUGEON)
        {
            if (pLevel.dimension() != DungeonManager.DEEP_DUGEON && pEntity instanceof Player player && dungeonData != null && !dungeonData.PlayersInside.contains(player.getStringUUID())) {
                dungeonData.PlayersInside.add(player.getStringUUID());
                data.putDungeon(portalId.DungeonId, dungeonData);
            }
            if (portal != null && pEntity.canUsePortal(false)) {
                pEntity.setAsInsidePortal(portal, pPos);
            }
            return;
        }

        if (pEntity.isOnPortalCooldown()) {
            pEntity.setPortalCooldown();
        }

        BlockPos origin, target;
        float scaleHoriz, scaleVert;

        if (dungeonPortal.Level == pLevel.dimension() && dungeonPortal.WithinLevel(pPos))
        {
            if (pEntity instanceof Player player) {
                if (!dungeonData.PlayersInside.contains(player.getStringUUID()))
                {
                    dungeonData.PlayersInside.add(player.getStringUUID());
                    data.putDungeon(portalId.DungeonId, dungeonData);
                }

                if (dungeonPortal.DungeonLink.Id != -1 && dungeonPortal.DungeonLink.DungeonId.Id > 0)
                {
                    DeepDungeon dungeon2 = data.getDungeon(dungeonPortal.DungeonLink.DungeonId);
                    if (!dungeon2.PlayersInside.contains(player.getStringUUID())) {
                        dungeon2.PlayersInside.add(player.getStringUUID());
                        data.putDungeon(portalId.DungeonId, dungeonData);
                    }
                }
            }
            origin = dungeonPortal.LevelBounds.minCorner;
            target = dungeonPortal.DungeonBounds.minCorner;
            scaleVert = (float)dungeonPortal.DungeonBounds.axis1Size / dungeonPortal.LevelBounds.axis1Size;
            scaleHoriz = (float)dungeonPortal.DungeonBounds.axis2Size / dungeonPortal.LevelBounds.axis2Size;
        }
        else
        {
            if (pEntity instanceof Player player && dungeonData.PlayersInside.contains(player.getStringUUID()) && dungeonPortal.DungeonLink.Id == -1)
            {
                data.removePlayerFromDungeons(pLevel.getServer(), player.getStringUUID());
            }
            origin = dungeonPortal.DungeonBounds.minCorner;
            target = dungeonPortal.LevelBounds.minCorner;
            scaleVert = (float)dungeonPortal.LevelBounds.axis1Size / dungeonPortal.DungeonBounds.axis1Size;
            scaleHoriz = (float)dungeonPortal.LevelBounds.axis2Size / dungeonPortal.DungeonBounds.axis2Size;
        }

        if (dungeonPortal.LevelAxis == dungeonPortal.DungeonAxis) {
            pEntity.teleportTo((pEntity.getX() - origin.getX()) * scaleHoriz + target.getX(),
                    (pEntity.getY() - origin.getY()) * scaleVert + target.getY(),
                    (pEntity.getZ() - origin.getZ()) * scaleHoriz + target.getZ());
        }
        else
        {
            pEntity.teleportTo((pEntity.getZ() - origin.getZ()) * scaleHoriz + target.getZ(),
                    (pEntity.getY() - origin.getY()) * scaleVert + target.getY(),
                    (pEntity.getX() - origin.getX()) * scaleHoriz + target.getX());
        }

        pEntity.setPortalCooldown();
    }

    private boolean WithinLevel(BlockPos pPos)
    {
        return IsInPortal(pPos, LevelBounds, LevelAxis == Direction.Axis.X ? new Vec3(1.0, 0.0, 0.0) :  new Vec3(0.0, 0.0, 1.0));
    }

    public static boolean IsInPortal(BlockPos position, BlockUtil.FoundRectangle portalBounds, Vec3 axis)
    {
        BlockPos maxCorner = new BlockPos((int)(portalBounds.axis2Size * axis.x) + portalBounds.minCorner.getX(), (int)(portalBounds.axis2Size * axis.y) + portalBounds.minCorner.getY() + portalBounds.axis1Size, (int)(portalBounds.axis2Size * axis.z) + portalBounds.minCorner.getZ());
        return position.getX() >= portalBounds.minCorner.getX() && position.getX() <= maxCorner.getX() &&
                position.getY() >= portalBounds.minCorner.getY() && position.getY() <= maxCorner.getY() &&
                position.getZ() >= portalBounds.minCorner.getZ() && position.getZ() <= maxCorner.getZ();
    }

    private static DimensionTransition getDimensionTransitionFromExit(
            Entity pEntity, BlockPos pPos, BlockUtil.FoundRectangle pRectangle, ServerLevel pLevel, DimensionTransition.PostDimensionTransition pPostDimensionTransition
    ) {
        BlockState blockstate = pEntity.level().getBlockState(pPos);
        Direction.Axis direction$axis;
        Vec3 vec3;
        if (blockstate.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            direction$axis = blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS);
            BlockUtil.FoundRectangle blockutil$foundrectangle = BlockUtil.getLargestRectangleAround(
                    pPos, direction$axis, 21, Direction.Axis.Y, 21, p_351016_ -> pEntity.level().getBlockState(p_351016_) == blockstate
            );
            vec3 = pEntity.getRelativePortalPosition(direction$axis, blockutil$foundrectangle);
        } else {
            direction$axis = Direction.Axis.X;
            vec3 = new Vec3(0.5, 0.0, 0.0);
        }

        return createDimensionTransition(
                pLevel, pRectangle, direction$axis, vec3, pEntity, pEntity.getDeltaMovement(), pEntity.getYRot(), pEntity.getXRot(), pPostDimensionTransition
        );
    }

    private static DimensionTransition createDimensionTransition(
            ServerLevel pLevel,
            BlockUtil.FoundRectangle pRectangle,
            Direction.Axis pAxis,
            Vec3 pOffset,
            Entity pEntity,
            Vec3 pSpeed,
            float pYRot,
            float pXRot,
            DimensionTransition.PostDimensionTransition pPostDimensionTransition
    ) {
        BlockPos blockpos = pRectangle.minCorner;
        BlockState blockstate = pLevel.getBlockState(blockpos);
        Direction.Axis direction$axis = blockstate.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        double d0 = (double)pRectangle.axis1Size;
        double d1 = (double)pRectangle.axis2Size;
        EntityDimensions entitydimensions = pEntity.getDimensions(pEntity.getPose());
        int i = pAxis == direction$axis ? 0 : 90;
        Vec3 vec3 = pAxis == direction$axis ? pSpeed : new Vec3(pSpeed.z, pSpeed.y, -pSpeed.x);
        double d2 = (double)entitydimensions.width() / 2.0 + (d0 - (double)entitydimensions.width()) * pOffset.x();
        double d3 = (d1 - (double)entitydimensions.height()) * pOffset.y();
        double d4 = 0.5 + pOffset.z();
        boolean flag = direction$axis == Direction.Axis.X;
        Vec3 vec31 = new Vec3((double)blockpos.getX() + (flag ? d2 : d4), (double)blockpos.getY() + d3, (double)blockpos.getZ() + (flag ? d4 : d2));
        Vec3 vec32 = PortalShape.findCollisionFreePosition(vec31, pLevel, pEntity, entitydimensions);
        return new DimensionTransition(pLevel, vec32, vec3, pYRot + (float)i, pXRot, pPostDimensionTransition);
    }

    public static boolean isPortalFrame(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos)
    {
        return blockState.is(DeepBlockTags.DEEP_DUNGEON_PORTAL);
    }

    public static final String LevelBoundsMinX = "LevelBounds.Min.X";
    public static final String LevelBoundsMinY = "LevelBounds.Min.Y";
    public static final String LevelBoundsMinZ = "LevelBounds.Min.Z";
    public static final String LevelBoundsAxis1 = "LevelBounds.Axis1";
    public static final String LevelBoundsAxis2 = "LevelBounds.Axis2";
    public static final String Level_Key = "Level";
    public static final String LevelAxis_Key = "LevelAxis";
    public static final String DungeonBoundsMinX = "DungeonBounds.Min.X";
    public static final String DungeonBoundsMinY = "DungeonBounds.Min.Y";
    public static final String DungeonBoundsMinZ = "DungeonBounds.Min.Z";
    public static final String DungeonBoundsAxis1 = "DungeonBounds.Axis1";
    public static final String DungeonBoundsAxis2 = "DungeonBounds.Axis2";
    public static final String DungeonAxis_Key = "LevelAxis";
    public static final String Tethered_Key = "Tethered";

    public CompoundTag saveToNbt()
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt(LevelBoundsMinX, LevelBounds.minCorner.getX());
        tag.putInt(LevelBoundsMinY, LevelBounds.minCorner.getY());
        tag.putInt(LevelBoundsMinZ, LevelBounds.minCorner.getZ());
        tag.putInt(LevelBoundsAxis1, LevelBounds.axis1Size);
        tag.putInt(LevelBoundsAxis2, LevelBounds.axis2Size);
        tag.putString(Level_Key ,Level.location().toString());
        tag.putInt(LevelAxis_Key, LevelAxis.ordinal());
        tag.putInt(DungeonBoundsMinX, DungeonBounds.minCorner.getX());
        tag.putInt(DungeonBoundsMinY, DungeonBounds.minCorner.getY());
        tag.putInt(DungeonBoundsMinZ, DungeonBounds.minCorner.getZ());
        tag.putInt(DungeonBoundsAxis1, DungeonBounds.axis1Size);
        tag.putInt(DungeonBoundsAxis2, DungeonBounds.axis2Size);
        tag.putInt(DungeonAxis_Key, DungeonAxis.ordinal());
        tag.putBoolean(Tethered_Key, Tethered);
        return tag;
    }

    public static DungeonPortal readNbt(CompoundTag tag)
    {
        String level = tag.getString(Level_Key);
        int index = level.indexOf(':');
        DungeonPortal portal = new DungeonPortal(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(level.substring(0,index - 1), level.substring(index + 1))),
                new BlockUtil.FoundRectangle(new BlockPos(tag.getInt(LevelBoundsMinX), tag.getInt(LevelBoundsMinY), tag.getInt(LevelBoundsMinZ)),
                        tag.getInt(LevelBoundsAxis1), tag.getInt(LevelBoundsAxis2)), Direction.Axis.values()[tag.getInt(LevelAxis_Key)],
                new BlockUtil.FoundRectangle(new BlockPos(tag.getInt(DungeonBoundsMinX), tag.getInt(DungeonBoundsMinY), tag.getInt(DungeonBoundsMinZ)),
                        tag.getInt(DungeonBoundsAxis1), tag.getInt(DungeonBoundsAxis2)), Direction.Axis.values()[tag.getInt(DungeonAxis_Key)]);
        portal.Tethered = tag.getBoolean(Tethered_Key);
        return portal;
    }
}
