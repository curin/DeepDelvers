package com.azure_drake.deep_delvers.world;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.dungeon.DeepDungeon;
import com.azure_drake.deep_delvers.dungeon.DungeonID;
import com.azure_drake.deep_delvers.dungeon.DungeonManager;
import com.azure_drake.deep_delvers.portal.PortalID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class DeepDelversData extends SavedData {
    public static final String Dungeon_Key ="Dungeons";
    public static final String NEXUS_KEY ="NexusIds";

    private Map<DungeonID, DeepDungeon> DungeonRegistry = new HashMap<>();
    private List<Integer> Nexus_Ids = new ArrayList<Integer>();

    public DeepDungeon getDungeon(DungeonID id)
    {
        return DungeonRegistry.get(id);
    }

    public boolean containsDungeon(DungeonID id)
    {
        return DungeonRegistry.containsKey(id);
    }

    public void putDungeon(DungeonID id, DeepDungeon dungeon)
    {
        DungeonRegistry.put(id, dungeon);
        setDirty();
    }

    public void removePlayerFromDungeons(MinecraftServer server, String playerUUID)
    {
        boolean changed = false;
        DungeonID[] keys = new DungeonID[DungeonRegistry.size()];
        DungeonRegistry.keySet().toArray(keys);
        for(DungeonID id : keys)
        {
            DeepDungeon dungeon = DungeonRegistry.get(id);
            int index = dungeon.PlayersInside.indexOf(playerUUID);
            if (index != -1)
            {
                changed = true;
                dungeon.PlayersInside.remove(index);

                if (dungeon.PlayersInside.size() == 0 && !dungeon.IsNexus)
                {
                    DungeonRegistry.remove(id);
                    dungeon.Destroy(server, false);
                }
                else
                {
                    DungeonRegistry.put(id, dungeon);
                }
            }
        }

        if (changed)
        {
            setDirty();
        }
    }

    public void AttemptDestroyDungeon(MinecraftServer server, PortalID portalId)
    {
        if (!DungeonRegistry.containsKey(portalId.DungeonId))
        {
            return;
        }

        DeepDungeon dungeon = DungeonRegistry.get(portalId.DungeonId);

        if (dungeon.PlayersInside.size() > 0)
        {
            DestroyPortal(server, portalId);
            return;
        }

        DungeonRegistry.remove(portalId.DungeonId);
        setDirty();
        dungeon.Destroy(server, false);
    }

    public void DestroyPortal(MinecraftServer server, PortalID portalId)
    {
        if (!DungeonRegistry.containsKey(portalId.DungeonId))
        {
            return;
        }

        DeepDungeon dungeon = DungeonRegistry.get(portalId.DungeonId);

        if (dungeon.Destroy(server, portalId, false))
        {
            DungeonRegistry.remove(portalId.DungeonId);
            dungeon.Destroy(server, false);
        }
        setDirty();
    }

    public Set<DungeonID> getAllDungeonIds()
    {
        return  DungeonRegistry.keySet();
    }

    @Override
    public CompoundTag save(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        CompoundTag saveTag = new CompoundTag();
        for (DungeonID dungeonID : DungeonRegistry.keySet())
        {
            String TierString = String.valueOf(dungeonID.Depth);
            if (!saveTag.contains(TierString))
                saveTag.put(TierString, new CompoundTag());

            DeepDungeon dungeon = DungeonRegistry.get(dungeonID);
            CompoundTag tierTag = saveTag.getCompound(TierString);
            tierTag.put(String.valueOf(dungeonID.Id), dungeon.saveToNbt());
        }

        CompoundTag mainTag = new CompoundTag();
        mainTag.put(Dungeon_Key, saveTag);
        mainTag.putIntArray(NEXUS_KEY, Nexus_Ids);
        pTag.put(DeepDelversMod.MODID, mainTag);
        return pTag;
    }

    public static DeepDelversData readNbt(CompoundTag pTag, HolderLookup.Provider provider)
    {
        DeepDelversData data = new DeepDelversData();
        if (!pTag.contains(DeepDelversMod.MODID))
        {
            return data;
        }

        CompoundTag mainTag = pTag.getCompound(DeepDelversMod.MODID);

        if (!mainTag.contains(Dungeon_Key))
        {
            return data;
        }

        CompoundTag saveTag = mainTag.getCompound(Dungeon_Key);

        for (String tier : saveTag.getAllKeys())
        {
            CompoundTag tierTag = saveTag.getCompound(tier);

            for (String id : tierTag.getAllKeys())
            {
                DungeonID dungeonID = new DungeonID(Integer.parseInt(id), Integer.parseInt(tier));
                data.DungeonRegistry.put(dungeonID, DeepDungeon.readNbt(tierTag.getCompound(id)));
            }
        }

        int[] ids = mainTag.getIntArray(NEXUS_KEY);
        for (Integer id : ids)
        {
            data.Nexus_Ids.add(id);
        }

        return data;
    }

    public static DungeonID GetNextDungeonId(ServerLevel level, int tier)
    {
        DungeonID ret = new DungeonID(1, tier);

        DeepDelversData deepData = DeepDelversData.get(level.getServer().getLevel(DungeonManager.DEEP_DUGEON));
        while (deepData.containsDungeon(ret) || deepData.Nexus_Ids.contains(ret.Id))
        {
            ret.Id++;
        }

        return ret;
    }

    public static DeepDelversData get(ServerLevel world) {
        DeepDelversData dungeonData = world.getDataStorage().computeIfAbsent(
                new SavedData.Factory<DeepDelversData>(
                        () -> new DeepDelversData(),
                        DeepDelversData::readNbt
                ),
                DeepDelversMod.MODID
        );

        dungeonData.setDirty();
        return dungeonData;
    }
}
