package com.azure_drake.deep_delvers.datagen;

import com.azure_drake.deep_delvers.DeepDelversMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = DeepDelversMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DatagenEventHandler
{
    @SubscribeEvent
    private static void gatherData(GatherDataEvent.Server event)
    {

    }

    @SubscribeEvent
    private static void gatherData(GatherDataEvent.Client event)
    {
        event.createProvider(DeepDelversModelProvider::new);
    }
}
