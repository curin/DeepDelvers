package com.azure_drake.deep_delvers.dungeon;

import com.azure_drake.deep_delvers.portal.DungeonPortal;
import com.azure_drake.deep_delvers.portal.PortalID;
import com.azure_drake.deep_delvers.portal.PortalState;
import com.azure_drake.deep_delvers.world.DeepDelversData;
import net.minecraft.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeepDungeon
{
    public DeepDungeon(DungeonID id, Holder<DungeonDepth> depthData, int tier, List<DungeonPortal> portals)
    {
        Id = id;
        Tier = tier;
        Portals = portals;
        DepthData =depthData;
    }

    public DeepDungeon(DungeonID id, Holder<DungeonDepth> depthData, int tier, List<DungeonPortal> portals, boolean isNexus)
    {
        Id = id;
        Tier = tier;
        Portals = portals;
        DepthData =depthData;
        IsNexus = isNexus;
    }

    public boolean Tethered()
    {
        for (DungeonPortal portal : Portals)
        {
            if (portal.State != PortalState.Destroyed && portal.Tethered)
            {
                return  true;
            }
        }
        return false;
    }
    public List<String> PlayersInside = new ArrayList<>();
    public List<DungeonPortal> Portals;
    public Holder<DungeonDepth> DepthData;
    public int Tier;
    public DungeonID Id;
    public boolean IsNexus = false;

    public static final String Tier_Key = "Tier";
    public static final String Id_Key = "Id";
    public static final String Players_Key = "Players";
    public static final String Portal_Key = "Portals";

    public static final String Depth_Key = "Depth";

    public void Destroy(MinecraftServer server, boolean ignoreTethered)
    {
        if (Tethered() && !ignoreTethered)
        {
            return;
        }

        for (DungeonPortal portal: Portals)
        {
            portal.Destroy(server);
        }
    }

    public boolean Destroy(MinecraftServer server, PortalID portalId, boolean ignoreTethered)
    {
        if (PlayersInside.size() > 0 || IsNexus)
        {
            return false;
        }

        DungeonPortal portal = Portals.get(portalId.Id);
        portal.Destroy(server);

        return Tethered() && !ignoreTethered && Portals.size() > 0;
    }

    public CompoundTag saveToNbt()
    {
        CompoundTag tag = new CompoundTag();

        tag.put(Id_Key, Id.serializeNBT());

        tag.putInt(Tier_Key, Tier);

        CompoundTag playerTag = new CompoundTag();
        for (String Player : PlayersInside)
        {
            playerTag.putString(Player, Player);
        }
        tag.put(Players_Key, playerTag);

        tag.putString(Depth_Key, DepthData.getKey().location().toString());

        CompoundTag portalTag = new CompoundTag();
        for (int portalId = 0; portalId < Portals.size(); portalId++)
        {
            portalTag.put(String.valueOf(portalId), Portals.get(portalId).saveToNbt());
        }
        tag.put(Portal_Key, portalTag);

        return tag;
    }

    public static DeepDungeon readNbt(CompoundTag tag)
    {
        CompoundTag portals = tag.getCompound(Portal_Key);
        List<DungeonPortal> Portals = new ArrayList<>();

        for (int i = 0; portals.contains(String.valueOf(i)); i++)
        {
            Portals.add(DungeonPortal.readNbt(portals.getCompound(String.valueOf(i))));
        }

        String location = tag.getString(Depth_Key);
        Optional<Holder.Reference<DungeonDepth>> depth = DungeonRegistries.DUNGEON_DEPTHS().get(ResourceLocation.parse(location));

        DeepDungeon dungeon = new DeepDungeon(DungeonID.deserializeNbt(tag.getCompound(Id_Key)), depth.get(), tag.getInt(Tier_Key), Portals);

        tag = tag.getCompound(Players_Key);
        dungeon.PlayersInside.addAll(tag.getAllKeys());

        return dungeon;
    }

    public static PortalID CreateNewDungeon(int tier, Holder<DungeonDepth> depth, ServerLevel level, BlockUtil.FoundRectangle levelBounds, Direction.Axis levelAxis)
    {
        PortalID portalId = new PortalID(DeepDelversData.GetNextDungeonId(level, depth.value().DepthValue()), 0);

        BlockUtil.FoundRectangle dungeonPortalRect = DungeonPortal.GetDungeonRectFromId(level.getServer().getLevel(DungeonManager.DEEP_DUGEON).getWorldBorder(), portalId);

        if (dungeonPortalRect.axis1Size == 0)
        {
            return new PortalID(new DungeonID(-1, -1), -1);
        }

        DungeonPortal portal = new DungeonPortal(level.dimension(), levelBounds, levelAxis, dungeonPortalRect, Direction.Axis.Y);

        List<DungeonPortal> portals = new ArrayList<>();
        portals.add(portal);

        DeepDelversData.get(level.getServer().getLevel(DungeonManager.DEEP_DUGEON)).putDungeon(portalId.DungeonId, new DeepDungeon(portalId.DungeonId, depth, tier, portals));

        //build portal in dungeon here
        DungeonManager.SpawnNewDungeon(level.getServer(), portalId, portal);

        return portalId;
    }

    public PortalID CreateNewPortal(ServerLevel level, BlockUtil.FoundRectangle levelBounds, Direction.Axis levelAxis)
    {
        PortalID portalId = new PortalID(Id, getNextAvailablePortal());

        BlockUtil.FoundRectangle dungeonPortalRect = DungeonPortal.GetDungeonRectFromId(level.getServer().getLevel(DungeonManager.DEEP_DUGEON).getWorldBorder(), portalId);

        if (dungeonPortalRect.axis1Size == 0)
        {
            return new PortalID(new DungeonID(-1, -1), -1);
        }

        DungeonPortal portal = new DungeonPortal(level.dimension(), levelBounds, levelAxis, dungeonPortalRect, Direction.Axis.Y);

        Portals.add(portal);
        return portalId;
    }

    public PortalID CreateNewPortalInside(ServerLevel level, BlockUtil.FoundRectangle levelBounds, Direction.Axis levelAxis, PortalID connected)
    {
        PortalID portalId = new PortalID(Id, getNextAvailablePortal());

        BlockUtil.FoundRectangle dungeonPortalRect = DungeonPortal.GetDungeonRectFromId(level.getServer().getLevel(DungeonManager.DEEP_DUGEON).getWorldBorder(), portalId);

        if (dungeonPortalRect.axis1Size == 0)
        {
            return new PortalID(new DungeonID(-1, -1), -1);
        }

        DungeonPortal portal = new DungeonPortal(level.dimension(), levelBounds, levelAxis, dungeonPortalRect, Direction.Axis.Y);
        portal.State = PortalState.Unconnected_To_Level;
        portal.DungeonLink = connected;

        Portals.add(portal);
        return portalId;
    }

    private int getNextAvailablePortal()
    {
        int i;
        for (i = 0; i < Portals.size(); i++)
        {
            if (Portals.get(i).State == PortalState.Destroyed)
            {
                break;
            }
        }
        return i;
    }
}
