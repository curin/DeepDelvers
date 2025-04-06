package com.azure_drake.deep_delvers.dungeon;

import net.minecraft.nbt.CompoundTag;

public class DungeonID
{
    public DungeonID(int id, int depth)
    {
        Depth = depth;
        Id = id;
    }

    public int Id;
    public int Depth;
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Depth", Depth);
        tag.putInt("Id", Id);

        return tag;
    }

    public static DungeonID deserializeNbt(CompoundTag tag)
    {
        return new DungeonID(tag.getInt("Id"), tag.getInt("Depth"));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DungeonID id))
        {
            return false;
        }

        return id.Id == Id && id.Depth == Depth;
    }

    @Override
    public int hashCode() {
        return Id + (Depth * 10000000);
    }
}
