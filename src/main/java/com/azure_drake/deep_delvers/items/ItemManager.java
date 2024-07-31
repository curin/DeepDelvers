package com.azure_drake.deep_delvers.items;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.blocks.BlockManager;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ItemManager {
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DeepDelversMod.MODID);
    public static final DeferredItem<BlockItem> BRONZE_BLOCK = ITEMS.registerSimpleBlockItem("bronze_block", BlockManager.BRONZE_BLOCK);
    public static final DeferredItem<Item> BRONZE_INGOT = ITEMS.registerSimpleItem("bronze_ingot", new Item.Properties());
    public static final DeferredItem<Item> RAW_BRONZE = ITEMS.registerSimpleItem("raw_bronze", new Item.Properties());
    public static final DeferredItem<BlockItem> RAW_BRONZE_BLOCK = ITEMS.registerSimpleBlockItem("raw_bronze_block", BlockManager.RAW_BRONZE_BLOCK);

    public static final DeferredItem<BlockItem> TIN_BLOCK = ITEMS.registerSimpleBlockItem("tin_block", BlockManager.TIN_BLOCK);
    public static final DeferredItem<BlockItem> TIN_ORE = ITEMS.registerSimpleBlockItem("tin_ore", BlockManager.TIN_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_TIN_ORE = ITEMS.registerSimpleBlockItem("deepslate_tin_ore", BlockManager.DEEPSLATE_TIN_ORE);
    public static final DeferredItem<Item> RAW_TIN = ITEMS.registerSimpleItem("raw_tin", new Item.Properties());

    public static final DeferredItem<BlockItem> RAW_TIN_BLOCK = ITEMS.registerSimpleBlockItem("raw_tin_block", BlockManager.RAW_TIN_BLOCK);
    public static final DeferredItem<Item> TIN_INGOT = ITEMS.registerSimpleItem("tin_ingot", new Item.Properties());

    public static final DeferredItem<BlockItem> DEEP_ROCK = ITEMS.registerSimpleBlockItem("deep_rock", BlockManager.DEEP_ROCK);

    public static final DeferredItem<PortalCatalyst> PORTAL_CATALYST = ITEMS.register("portal_catalyst", PortalCatalyst::new);

    public static final DeferredItem<BlockItem> DUNGEON_PORTAL = ITEMS.registerSimpleBlockItem("dungeon_portal", BlockManager.DUNGEON_PORTAL);

    public static final DeferredItem<BlockItem> DUNGEON_PORTAL_SPAWNER = ITEMS.registerSimpleBlockItem("dungeon_portal_spawner", BlockManager.DUNGEON_PORTAL_SPAWNER);

    public static List<ItemLike> Items = new ArrayList<ItemLike>()
    {{
        add(RAW_TIN);
        add(RAW_TIN_BLOCK);
        add(TIN_INGOT);
        add(TIN_BLOCK);
        add(TIN_ORE);
        add(DEEPSLATE_TIN_ORE);
        add(DEEP_ROCK);
        add(PORTAL_CATALYST);
        add(RAW_BRONZE);
        add(RAW_BRONZE_BLOCK);
        add(BRONZE_INGOT);
        add(BRONZE_BLOCK);
    }};


    // The specialized DeferredRegister.DataComponents simplifies data component registration and avoids some generic inference issues with the `DataComponentType.Builder` within a `Supplier`
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(DeepDelversMod.MODID);

    public static final Supplier<DataComponentType<Integer>> CATALYST_TIER = COMPONENTS.registerComponentType("catalyst_tier",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static final Supplier<DataComponentType<Integer>> CATALYST_DEPTH = COMPONENTS.registerComponentType("catalyst_depth",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static void register(IEventBus modEventBus)
    {
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        COMPONENTS.register(modEventBus);
    }

    public static void addToCreativeTab(CreativeModeTab.Output output)
    {
        for (ItemLike item : Items) {
            output.accept(item);
        }
    }
}
