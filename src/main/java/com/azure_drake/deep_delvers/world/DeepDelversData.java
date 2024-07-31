package com.azure_drake.deep_delvers.world;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.dungeon.DeepDungeon;
import com.azure_drake.deep_delvers.portal.PortalID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DeepDelversData extends SavedData {
    public static final String Dungeon_Key ="Dungeons";

    private Map<PortalID, DeepDungeon> PortalRegistry = new HashMap<>();

    public DeepDungeon getDungeon(PortalID id)
    {
        return PortalRegistry.get(id);
    }

    public boolean containsDungeon(PortalID id)
    {
        for (PortalID key: PortalRegistry.keySet())
        {
            if (id.equals(key))
            {
                return  true;
            }
        }
        return false;
    }

    public void putDungeon(PortalID id, DeepDungeon dungeon)
    {
        PortalRegistry.put(id, dungeon);
        setDirty();
    }

    public void removeDungeon(PortalID id)
    {
        PortalRegistry.remove(id);
        setDirty();
    }

    public Set<PortalID> getAllDungeonIds()
    {
        return  PortalRegistry.keySet();
    }

    @Override
    public CompoundTag save(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        CompoundTag saveTag = new CompoundTag();
        for (var portalId : PortalRegistry.keySet())
        {
            String TierString = String.valueOf(portalId.Tier);
            if (!saveTag.contains(TierString))
                saveTag.put(TierString, new CompoundTag());

            DeepDungeon dungeon = PortalRegistry.get(portalId);
            CompoundTag tierTag = saveTag.getCompound(TierString);
            tierTag.put(String.valueOf(portalId.Id), dungeon.saveToNbt());
        }
        CompoundTag mainTag = new CompoundTag();
        mainTag.put(Dungeon_Key, saveTag);
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
                PortalID portalId = new PortalID(Integer.parseInt(id), Integer.parseInt(tier));
                data.PortalRegistry.put(portalId, DeepDungeon.readNbt(tierTag.getCompound(id)));
            }
        }

        return data;
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
