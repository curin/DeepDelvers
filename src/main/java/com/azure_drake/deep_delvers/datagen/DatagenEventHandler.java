package com.azure_drake.deep_delvers.datagen;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.dungeon.DungeonDepth;
import com.azure_drake.deep_delvers.dungeon.DungeonRegistries;
import com.azure_drake.deep_delvers.dungeon.DungeonTheme;
import com.azure_drake.deep_delvers.dungeon.DungeonTile;
import net.minecraft.core.Cloner;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Set;

@EventBusSubscriber(modid = DeepDelversMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DatagenEventHandler
{
    @SubscribeEvent
    public static void dataPackRegister(DataPackRegistryEvent.NewRegistry event)
    {
        event.dataPackRegistry(DungeonRegistries.DUNGEON_TILE_KEY, DungeonTile.DIRECT_CODEC);
        event.dataPackRegistry(DungeonRegistries.DUNGEON_THEME_KEY, DungeonTheme.DIRECT_CODEC);
        event.dataPackRegistry(DungeonRegistries.DUNGEON_DEPTH_KEY, DungeonDepth.DIRECT_CODEC);
    }

    @SubscribeEvent
    private static void gatherData(GatherDataEvent.Client event)
    {
        event.createProvider(DeepDelversModelProvider::new);
        event.createProvider(DeepDelversRegistryProvider::new);
    }

    @SubscribeEvent
    private static void gatherData(GatherDataEvent.Server event)
    {
        event.createProvider(DeepDelversRegistryProvider::new);
    }
}
