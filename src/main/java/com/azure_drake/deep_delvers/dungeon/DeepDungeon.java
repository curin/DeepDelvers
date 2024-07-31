package com.azure_drake.deep_delvers.dungeon;

import com.azure_drake.deep_delvers.portal.DungeonPortal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class DeepDungeon
{
    public DeepDungeon(int depth, DungeonPortal portal)
    {
        Portal = portal;
    }
    public boolean Tethered = false;
    public List<String> PlayersInside = new ArrayList<>();
    public DungeonPortal Portal;
    public int Depth;

    public static final String Tethered_Key = "Tethered";
    public static final String Depth_Key = "Depth";
    public static final String Players_Key = "Players";
    public static final String Portal_Key = "Portal";

    public void Destroy(MinecraftServer server, boolean ignoreTethered)
    {
        if (Tethered && !ignoreTethered)
        {
            return;
        }
        Portal.Destroy(server);
    }

    public CompoundTag saveToNbt()
    {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(Tethered_Key, Tethered);

        tag.putInt(Depth_Key, Depth);

        CompoundTag playerTag = new CompoundTag();
        for (String Player : PlayersInside)
        {
            playerTag.putString(Player, Player);
        }

        playerTag.put(Portal_Key, Portal.saveToNbt());
        return tag;
    }

    public static DeepDungeon readNbt(CompoundTag tag)
    {
        DeepDungeon dungeon = new DeepDungeon(tag.getInt(Depth_Key), DungeonPortal.readNbt(tag.getCompound(Portal_Key)));

        dungeon.Tethered = tag.getBoolean(Tethered_Key);

        tag = tag.getCompound(Players_Key);
        dungeon.PlayersInside.addAll(tag.getAllKeys());

        return dungeon;
    }
}
