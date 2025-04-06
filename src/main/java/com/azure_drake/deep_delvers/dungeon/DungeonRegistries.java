package com.azure_drake.deep_delvers.dungeon;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DungeonRegistries
{
    public static final ResourceKey<Registry<DungeonTile>> DUNGEON_TILE_KEY = createRegistryKey("dungeon/tile");
    public static final ResourceKey<Registry<DungeonTheme>> DUNGEON_THEME_KEY = createRegistryKey("dungeon/theme");
    public static final ResourceKey<Registry<DungeonDepth>> DUNGEON_DEPTH_KEY = createRegistryKey("dungeon/depth");
    public static final ResourceKey<Registry<MapCodec<? extends DungeonAbilityModifier>>> DUNGEON_ABILITY_MODIFIER_KEY = createRegistryKey("dungeon/ability_modifier");

    public static Registry<DungeonTile> DUNGEON_TILES(ServerLevel level)
    {
        return level.registryAccess().lookupOrThrow(DUNGEON_TILE_KEY);
    }

    public static Registry<DungeonTile> DUNGEON_TILES()
    {
        return Minecraft.getInstance().getConnection().registryAccess().lookupOrThrow(DUNGEON_TILE_KEY);
    }

    public static Registry<DungeonTheme> DUNGEON_THEMES(ServerLevel level)
    {
        return level.registryAccess().lookupOrThrow(DUNGEON_THEME_KEY);
    }

    public static Registry<DungeonTheme> DUNGEON_THEMES()
    {
        return Minecraft.getInstance().getConnection().registryAccess().lookupOrThrow(DUNGEON_THEME_KEY);
    }

    public static Registry<DungeonDepth> DUNGEON_DEPTHS(ServerLevel level)
    {
        return level.registryAccess().lookupOrThrow(DUNGEON_DEPTH_KEY);
    }

    public static Registry<DungeonDepth> DUNGEON_DEPTHS()
    {
        return Minecraft.getInstance().getConnection().registryAccess().lookupOrThrow(DUNGEON_DEPTH_KEY);
    }

    //private static final DeferredRegister<DungeonTile> dungeon_tiles = DeferredRegister.create(DUNGEON_TILE_KEY, DeepDelversMod.MODID);
    //private static final DeferredRegister<DungeonTheme> DUNGEON_THEMES = DeferredRegister.create(DUNGEON_THEME_KEY, DeepDelversMod.MODID);
    //private static final DeferredRegister<DungeonDepth> DUNGEON_DEPTHS = DeferredRegister.create(DUNGEON_DEPTH_KEY, DeepDelversMod.MODID);
    public static final DeferredRegister<MapCodec<? extends DungeonAbilityModifier>> DUNGEON_ABILITY_MODIFIERS = DeferredRegister.create(DUNGEON_ABILITY_MODIFIER_KEY, DeepDelversMod.MODID);

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace(name));
    }

    public static void register(IEventBus bus)
    {
        //dungeon_tiles.register(bus);
        //DUNGEON_THEMES.register(bus);
        //DUNGEON_DEPTHS.register(bus);
        DUNGEON_ABILITY_MODIFIERS.register(bus);
    }
}
