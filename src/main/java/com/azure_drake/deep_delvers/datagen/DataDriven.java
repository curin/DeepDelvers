package com.azure_drake.deep_delvers.datagen;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Optional;

public record DataDriven<T>(Optional<TagKey<T>> Tag, Optional<ResourceKey<T>> Key)
{
    public static <T> Codec<DataDriven<T>> DIRECT_CODEC(ResourceKey<? extends Registry<T>> registryKey)
    {
        return ExtraCodecs.catchDecoderException(
                RecordCodecBuilder.create(
                        codec -> codec.group(
                                        Codec.optionalField("tag", TagKey.codec(registryKey), true).fieldOf("tag").forGetter(DataDriven<T>::Tag),
                                    Codec.optionalField("key", ResourceKey.codec(registryKey), true).fieldOf("key").forGetter(DataDriven<T>::Key)
                                )
                                .apply(codec, DataDriven<T>::new)
                )
        );
    }

    public static <T> StreamCodec<ByteBuf, DataDriven<T>> STREAM_CODEC(ResourceKey<? extends Registry<T>> registryKey)
    {
        return StreamCodec.composite(
                ByteBufCodecs.optional(TagKey.streamCodec(registryKey)), DataDriven<T>::Tag,
                ByteBufCodecs.optional(ResourceKey.streamCodec(registryKey)), DataDriven<T>::Key,
                DataDriven<T>::new
        );
    }

    public Optional<Holder<T>> get(ResourceKey<? extends Registry<T>> registryKey, ServerLevel pLevel)
    {
        Registry<T> reg = pLevel.registryAccess().lookupOrThrow(registryKey);
        if (Key().isPresent())
        {
            return Optional.of(Holder.Reference.createStandAlone(reg, Key().get()));
        }
        if (Tag().isPresent())
        {
            Optional<HolderSet.Named<T>> set = reg.get(Tag().get());
            if (set.isPresent())
            {
                return set.get().getRandomElement(pLevel.random);
            }
        }

        return Optional.empty();
    }

    public Optional<Holder<T>> get(ResourceKey<? extends Registry<T>> registryKey)
    {

        Registry<T> reg = Minecraft.getInstance().getConnection().registryAccess().lookupOrThrow(registryKey);
        if (Key().isPresent())
        {
            return Optional.of(Holder.Reference.createStandAlone(reg, Key().get()));
        }
        if (Tag().isPresent())
        {
            Optional<HolderSet.Named<T>> set = reg.get(Tag().get());
            if (set.isPresent())
            {
                return set.get().getRandomElement(Minecraft.getInstance().level.random);
            }
        }

        return Optional.empty();
    }

    public static <T> DataDriven<T> create(ResourceKey<? extends Registry<T>> registryKey, String name)
    {
        return new DataDriven<>(Optional.empty(), Optional.of(createResourceKey(registryKey, name)));
    }

    public static <T> DataDriven<T> create(TagKey<T> tag)
    {
        return new DataDriven<>(Optional.of(tag), Optional.empty());
    }

    private static <T> ResourceKey<T> createResourceKey(ResourceKey<? extends Registry<T>> registryKey, String name)
    {
        ResourceKey<T> key = ResourceKey.create(registryKey,
                ResourceLocation.fromNamespaceAndPath(DeepDelversMod.MODID, name));
        return key;
    }

    public record Weighted<T>(DataDriven<T> Value, int Weight)
    {
        public static <T> Codec<DataDriven.Weighted<T>> DIRECT_CODEC(ResourceKey<? extends Registry<T>> registryKey)
        {
            return ExtraCodecs.catchDecoderException(
                    RecordCodecBuilder.create(
                            codec -> codec.group(
                                            DataDriven.DIRECT_CODEC(registryKey).fieldOf("value").forGetter(DataDriven.Weighted<T>::Value),
                                            Codec.INT.fieldOf("weight").forGetter(DataDriven.Weighted<T>::Weight)
                                    )
                                    .apply(codec, DataDriven.Weighted<T>::new)
                    )
            );
        }

        public static <T> StreamCodec<ByteBuf, DataDriven.Weighted<T>> STREAM_CODEC(ResourceKey<? extends Registry<T>> registryKey)
        {
            return StreamCodec.composite(
                    DataDriven.STREAM_CODEC(registryKey), DataDriven.Weighted<T>::Value,
                    ByteBufCodecs.INT, DataDriven.Weighted<T>::Weight,
                    DataDriven.Weighted<T>::new
            );
        }

        public static <T> DataDriven.Weighted<T> Get(List<Weighted<T>> list, RandomSource randomSource)
        {
            int max = 0;
            for (DataDriven.Weighted<T> weighted: list) {
                max += weighted.Weight;
            }
            int val = randomSource.nextInt(max);

            for (DataDriven.Weighted<T> weighted: list) {
                val -= weighted.Weight;

                if (val <= 0)
                {
                    return weighted;
                }
            }

            return list.getLast();
        }

        public static <T> DataDriven.Weighted<T> create(TagKey<T> tag, int weight)
        {
            return new Weighted<>(DataDriven.create(tag), weight);
        }

        public static <T> DataDriven.Weighted<T> create(ResourceKey<? extends Registry<T>> registryKey, String name, int weight)
        {
            return new Weighted<>(DataDriven.create(registryKey, name), weight);
        }
    }
}
