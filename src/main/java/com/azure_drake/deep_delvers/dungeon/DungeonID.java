package com.azure_drake.deep_delvers.dungeon;

import net.minecraft.nbt.CompoundTag;

public class DungeonID
{
    public DungeonID(int id, int tier)
    {
        Tier = tier;
        Id = id;
    }

    public int Id;
    public int Tier;
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Tier", Tier);
        tag.putInt("Id", Id);

        return tag;
    }

    public static DungeonID deserializeNbt(CompoundTag tag)
    {
        return new DungeonID(tag.getInt("Id"), tag.getInt("Tier"));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DungeonID id))
        {
            return false;
        }

        return id.Id == Id && id.Tier == Tier;
    }

    @Override
    public int hashCode() {
        return Id + (Tier * 10000000);
    }
}
