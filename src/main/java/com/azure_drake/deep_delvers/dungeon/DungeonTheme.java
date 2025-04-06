package com.azure_drake.deep_delvers.dungeon;

import com.azure_drake.deep_delvers.datagen.DataDriven;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record DungeonTheme (
                            List<DataDriven.Weighted<DungeonTile>> GeneralTiles,
                            List<DataDriven.Weighted<DungeonTile>> EntranceTiles,
                            List<DataDriven.Weighted<DungeonTile>> BossTiles,
                            List<AllowedLocations> AllowedDepths,
                            boolean IsAllowedDepthsBlacklist,
                            int DesiredMaxConsecutiveCorridorTiles)
{
    public static final Codec<DungeonTheme> DIRECT_CODEC = ExtraCodecs.catchDecoderException(
            RecordCodecBuilder.create(
                    codec -> codec.group(
                                    Codec.list(DataDriven.Weighted.DIRECT_CODEC(DungeonRegistries.DUNGEON_TILE_KEY)).fieldOf("general_tiles").forGetter(DungeonTheme::GeneralTiles),
                                    Codec.list(DataDriven.Weighted.DIRECT_CODEC(DungeonRegistries.DUNGEON_TILE_KEY)).fieldOf("entrance_tiles").forGetter(DungeonTheme::EntranceTiles),
                                    Codec.list(DataDriven.Weighted.DIRECT_CODEC(DungeonRegistries.DUNGEON_TILE_KEY)).fieldOf("boss_tiles").forGetter(DungeonTheme::BossTiles),
                                    Codec.list(AllowedLocations.CODEC).fieldOf("allowed_depths").forGetter(DungeonTheme::AllowedDepths),
                                    Codec.BOOL.fieldOf("is_allowed_depths_blacklist").forGetter(DungeonTheme::IsAllowedDepthsBlacklist),
                                    Codec.INT.fieldOf("desired_max_consecutive_corridor_tiles").forGetter(DungeonTheme::DesiredMaxConsecutiveCorridorTiles)
                            )
                            .apply(codec, DungeonTheme::new)
            )
    );

    public static final Codec<Holder<DungeonTheme>> CODEC = RegistryFileCodec.create(DungeonRegistries.DUNGEON_THEME_KEY, DIRECT_CODEC);

    public static final Codec<HolderSet<DungeonTheme>> HOLDER_SET_CODEC = HolderSetCodec.create(DungeonRegistries.DUNGEON_THEME_KEY, CODEC, false);

    public record AllowedLocations(HolderSet<DungeonDepth> Depth, List<Integer> Tiers)
    {
        public static final Codec<AllowedLocations> CODEC = RecordCodecBuilder.create(
                        codec -> codec.group(
                                        DungeonDepth.HOLDER_SET_CODEC.fieldOf("depth").forGetter(DungeonTheme.AllowedLocations::Depth),
                                        Codec.list(Codec.INT).fieldOf("tiers").forGetter(DungeonTheme.AllowedLocations::Tiers)
                                )
                                .apply(codec, AllowedLocations::new)
        );
    }
}
