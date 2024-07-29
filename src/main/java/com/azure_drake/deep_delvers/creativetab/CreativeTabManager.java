package com.azure_drake.deep_delvers.creativetab;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.items.ItemManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTabManager {
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DeepDelversMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register("deep_delvers_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.deep_delvers")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ItemManager.BRONZE_INGOT.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                ItemManager.addToCreativeTab(output);
            }).build());

    public static void register(IEventBus modEventBus)
    {
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}