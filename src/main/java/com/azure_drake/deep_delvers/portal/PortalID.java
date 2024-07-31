package com.azure_drake.deep_delvers.portal;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class PortalID
{
    public PortalID(int id, int tier)
    {
        Id = id;
    }

    public int Id;
    public int Tier;
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Id", Id);
        tag.putInt("Tier", Tier);

        return tag;
    }

    public void deserializeNBT(CompoundTag tag)
    {
        Id = tag.getInt("Id");
        Tier = tag.getInt("Tier");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PortalID id))
        {
            return false;
        }

        return id.Id == Id && id.Tier == Tier;
    }
}
