package com.azure_drake.deep_delvers.portal;

import com.azure_drake.deep_delvers.dungeon.DungeonID;
import net.minecraft.nbt.CompoundTag;

public class PortalID
{
    public PortalID(DungeonID dungeonId, int id)
    {
        DungeonId = dungeonId;
        Id = id;
    }

    public DungeonID DungeonId;
    public int Id;
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        tag.put("Dungeon", DungeonId.serializeNBT());
        tag.putInt("Id", Id);

        return tag;
    }

    public static PortalID deserializeNbt(CompoundTag tag)
    {
        return new PortalID(DungeonID.deserializeNbt(tag.getCompound("Dungeon")), tag.getInt("Id"));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PortalID id))
        {
            return false;
        }

        return id.DungeonId == DungeonId && id.Id == Id;
    }
}
