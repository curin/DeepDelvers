package com.azure_drake.deep_delvers.blocks;

import com.azure_drake.deep_delvers.DeepDelversMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class DeepBlockTags
{
    public static final TagKey<Block> DEEP_DUNGEON_PORTAL = create("deep_dungeon_portal");
    private static TagKey<Block> create(String pName) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(DeepDelversMod.MODID, pName));
    }
}
