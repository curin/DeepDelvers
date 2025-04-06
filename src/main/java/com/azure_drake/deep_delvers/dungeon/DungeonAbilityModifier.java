package com.azure_drake.deep_delvers.dungeon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.function.Function;

public abstract class DungeonAbilityModifier
{
    public static final Codec<DungeonAbilityModifier> CODEC = DungeonRegistries.DUNGEON_ABILITY_MODIFIERS
            .getRegistry().get().byNameCodec()
            .dispatchStable(DungeonAbilityModifier::GetCodec, Function.identity());


    public String ModifierType;

    public abstract void Apply(ServerPlayer player);

    protected abstract MapCodec<? extends DungeonAbilityModifier> GetCodec();
}
