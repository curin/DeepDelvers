package com.azure_drake.deep_delvers.dungeon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.List;

public record DungeonTile (Integer Height, Integer Width, Integer Depth, List<Exit> Exits, TileType Type,
                           BlockPos PortalLocation, List<Integer> AllowedLayers, List<DungeonAbilityModifier> Modifiers)
{
    public static final Codec<DungeonTile> DIRECT_CODEC = ExtraCodecs.catchDecoderException(
            RecordCodecBuilder.create(
                    codec -> codec.group(
                                    Codec.INT.fieldOf("height").forGetter(DungeonTile::Height),
                                    Codec.INT.fieldOf("width").forGetter(DungeonTile::Width),
                                    Codec.INT.fieldOf("depth").forGetter(DungeonTile::Depth),
                                    Codec.list(Exit.CODEC).fieldOf("exits").forGetter(DungeonTile::Exits),
                                    TileType.CODEC.fieldOf("type").forGetter(DungeonTile::Type),
                                    BlockPos.CODEC.fieldOf("portal_location").forGetter(DungeonTile::PortalLocation),
                                    Codec.list(Codec.INT).fieldOf("allowed_layers").forGetter(DungeonTile::AllowedLayers),
                                    Codec.list(Codec.lazyInitialized(() -> DungeonAbilityModifier.CODEC)).fieldOf("modifiers").forGetter(DungeonTile::Modifiers)
                            )
                            .apply(codec, DungeonTile::new)
            )
    );

    public static final Codec<Holder<DungeonTile>> CODEC = RegistryFileCodec.create(DungeonRegistries.DUNGEON_TILE_KEY, DIRECT_CODEC);
    public static final Codec<HolderSet<DungeonTile>> HOLDER_SET_CODEC = HolderSetCodec.create(DungeonRegistries.DUNGEON_TILE_KEY, CODEC, false);

    public record Exit (Direction Face, Integer X, Integer Y)
    {
        public static final Codec<Exit> CODEC = RecordCodecBuilder.create(
                codec -> codec.group(
                        Direction.CODEC.fieldOf("face").forGetter(Exit::Face),
                        Codec.INT.fieldOf("x").forGetter(Exit::X),
                        Codec.INT.fieldOf("y").forGetter(Exit::Y)
                )
                .apply(codec, Exit::new)
        );
    }

    public enum TileType implements StringRepresentable {
        General("general"),
        Boss("boss"),
        Entrance("entrance"),
        Corridor("corridor");

        public static final EnumCodec<TileType> CODEC = StringRepresentable.fromEnum(TileType::values);
        private final String name;

        private TileType(String name)
        {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
