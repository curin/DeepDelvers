package com.azure_drake.deep_delvers.dungeon;

import com.azure_drake.deep_delvers.datagen.DataDriven;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record DungeonDepth(int DepthValue, int TierCount,
                           boolean AllowDelvingToNextDepth, List<DataDriven.Weighted<DungeonDepth>> NextDepth,
                           List<DataDriven.Weighted<DungeonTheme>> NexusTheme, int PreferredNexusOrdinal)
{
    public static final ResourceLocation DEFAULT = ResourceLocation.parse("deep_delvers:1");

    public static final Codec<DungeonDepth> DIRECT_CODEC = ExtraCodecs.catchDecoderException(
            RecordCodecBuilder.create(
                    codec -> codec.group(
                                    Codec.INT.fieldOf("depth").forGetter(DungeonDepth::DepthValue),
                                    Codec.INT.fieldOf("tier_count").forGetter(DungeonDepth::TierCount),
                                    Codec.BOOL.fieldOf("allow_delving_to_next_depth").forGetter(DungeonDepth::AllowDelvingToNextDepth),
                                    Codec.list(DataDriven.Weighted.DIRECT_CODEC(DungeonRegistries.DUNGEON_DEPTH_KEY)).fieldOf("next_depth").forGetter(DungeonDepth::NextDepth),
                                    Codec.list(DataDriven.Weighted.DIRECT_CODEC(DungeonRegistries.DUNGEON_THEME_KEY)).fieldOf("nexus_theme").forGetter(DungeonDepth::NexusTheme),
                                    Codec.INT.fieldOf("preferred_nexus_ordinals").forGetter(DungeonDepth::PreferredNexusOrdinal)
                            )
                            .apply(codec, DungeonDepth::new)
            )
    );

    public static final Codec<Holder<DungeonDepth>> CODEC = RegistryFileCodec.create(DungeonRegistries.DUNGEON_DEPTH_KEY, DIRECT_CODEC);

    public static final Codec<HolderSet<DungeonDepth>> HOLDER_SET_CODEC = HolderSetCodec.create(DungeonRegistries.DUNGEON_DEPTH_KEY, CODEC, false);
}
