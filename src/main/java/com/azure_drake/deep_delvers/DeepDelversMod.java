package com.azure_drake.deep_delvers;

import com.azure_drake.deep_delvers.blocks.BlockManager;
import com.azure_drake.deep_delvers.creativetab.CreativeTabManager;
import com.azure_drake.deep_delvers.items.ItemManager;
import com.azure_drake.deep_delvers.world.WorldEventHandler;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DeepDelversMod.MODID)
public class DeepDelversMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "deep_delvers";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public DeepDelversMod(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register Blocks, Items, and CreativeTabs
        BlockManager.register(modEventBus);
        ItemManager.register(modEventBus);
        CreativeTabManager.register(modEventBus);

        // Register event Handlers
        NeoForge.EVENT_BUS.register(new WorldEventHandler());
        // modEventBus.addListener(DatagenEventHandler::gatherData);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
    }
}
