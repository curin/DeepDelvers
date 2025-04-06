package com.azure_drake.deep_delvers.datagen;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.dungeon.DungeonDepth;
import com.azure_drake.deep_delvers.dungeon.DungeonRegistries;
import com.azure_drake.deep_delvers.dungeon.DungeonTheme;
import com.azure_drake.deep_delvers.dungeon.DungeonTile;
import net.minecraft.client.Minecraft;
import net.minecraft.core.*;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DeepDelversRegistryProvider extends DatapackBuiltinEntriesProvider
{
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(DungeonRegistries.DUNGEON_DEPTH_KEY, DeepDelversRegistryProvider::generateDepths)
            .add(DungeonRegistries.DUNGEON_THEME_KEY, DeepDelversRegistryProvider::generateThemes);

    public DeepDelversRegistryProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, BUILDER, Set.of(DeepDelversMod.MODID));
    }

    private static void generateDepths(BootstrapContext<DungeonDepth> ctx) {
        depthTemplate(ctx, "1", new DungeonDepth(0, 5, false,
                new ArrayList<>(), new ArrayList<>(List.of(
                        DataDriven.Weighted.create(DungeonRegistries.DUNGEON_THEME_KEY, "default", 1))),
                0));
    }

    private static void depthTemplate(BootstrapContext<DungeonDepth> ctx, String name, DungeonDepth template) {
        ctx.register(createResourceKey(DungeonRegistries.DUNGEON_DEPTH_KEY, name),
                template);
    }

    private static void generateThemes(BootstrapContext<DungeonTheme> ctx) {
        themeTemplate(ctx, "default", new DungeonTheme(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), true, 1));
    }

    private static void themeTemplate(BootstrapContext<DungeonTheme> ctx, String name, DungeonTheme template) {
        ctx.register(createResourceKey(DungeonRegistries.DUNGEON_THEME_KEY, name),
                    template);
    }


    private static <T> ResourceKey<T> createResourceKey(ResourceKey<? extends Registry<T>> registryKey, String name)
    {
        return ResourceKey.create(registryKey,
                ResourceLocation.fromNamespaceAndPath(DeepDelversMod.MODID, name));
    }
}
