package com.azure_drake.deep_delvers.items;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.blocks.BlockManager;
import com.azure_drake.deep_delvers.datagen.DataDriven;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemManager {
    public static List<ItemLike> CreativeItems = new ArrayList<ItemLike>();
    public static List<Supplier<? extends DeepDelversItem>> Datagen = new ArrayList<>();
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DeepDelversMod.MODID);

    public static final DeferredItem<BlockItem> ALUMINUM_ORE = RegisterBlockItem(BlockManager.ALUMINUM_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_ALUMINUM_ORE = RegisterBlockItem(BlockManager.DEEPSLATE_ALUMINUM_ORE);

    public static final DeferredItem<DeepDelversItem> RAW_ALUMINUM = RegisterItem("raw_aluminum", new Item.Properties());
    public static final DeferredItem<BlockItem> RAW_ALUMINUM_BLOCK = RegisterBlockItem(BlockManager.RAW_ALUMINUM_BLOCK);

    public static final DeferredItem<DeepDelversItem> ALUMINUM_INGOT = RegisterItem("aluminum_ingot", new Item.Properties());
    public static final DeferredItem<BlockItem> ALUMINUM_BLOCK = RegisterBlockItem(BlockManager.ALUMINUM_BLOCK);

    public static final DeferredItem<DeepDelversItem> RAW_DURALUMIN = RegisterItem("raw_duralumin", new Item.Properties());
    public static final DeferredItem<BlockItem> RAW_DURALUMIN_BLOCK = RegisterBlockItem(BlockManager.RAW_DURALUMIN_BLOCK);

    public static final DeferredItem<DeepDelversItem> DURALUMIN_INGOT = RegisterItem("duralumin_ingot", new Item.Properties());
    public static final DeferredItem<BlockItem> DURALUMIN_BLOCK = RegisterBlockItem(BlockManager.DURALUMIN_BLOCK);

    public static final DeferredItem<BlockItem> DEEP_ROCK = RegisterBlockItem(BlockManager.DEEP_ROCK);

    public static final DeferredItem<PortalCatalyst> PORTAL_CATALYST = RegisterItem("portal_catalyst", PortalCatalyst::new, new Item.Properties().fireResistant().stacksTo(1));

    public static final DeferredItem<BlockItem> DUNGEON_PORTAL_FRAME = RegisterBlockItem(BlockManager.DUNGEON_PORTAL_FRAME);
    public static final DeferredItem<BlockItem> DUNGEON_PORTAL_FRAME_COSMETIC = RegisterBlockItem(BlockManager.DUNGEON_PORTAL_FRAME_COSMETIC, BlockManager.DUNGEON_PORTAL_FRAME);

    public static final DeferredItem<BlockItem> DUNGEON_PORTAL_SPAWNER = RegisterBlockItem(BlockManager.DUNGEON_PORTAL_SPAWNER, false);

    public static final DeferredItem<BlockItem> DUNGEON_BLOCK_RANDOMIZER = RegisterBlockItem(BlockManager.DUNGEON_BLOCK_RANDOMIZER);
    public static final DeferredItem<BlockItem> DUNGEON_PRESSURE_PLATE = RegisterBlockItem(BlockManager.DUNGEON_PRESSURE_PLATE);
    public static final DeferredItem<BlockItem> DUNGEON_REWARD_SPAWNER = RegisterBlockItem(BlockManager.DUNGEON_REWARD_SPAWNER);
    public static final DeferredItem<BlockItem> DUNGEON_SPAWNER = RegisterBlockItem(BlockManager.DUNGEON_SPAWNER);
    public static final DeferredItem<BlockItem> DUNGEON_THEME_MANAGER = RegisterBlockItem(BlockManager.DUNGEON_THEME_MANAGER);
    public static final DeferredItem<BlockItem> DUNGEON_TILE_MANAGER = RegisterBlockItem(BlockManager.DUNGEON_TILE_MANAGER);
    public static final DeferredItem<BlockItem> HIDDEN_TRIPWIRE_HOOK = RegisterBlockItem(BlockManager.HIDDEN_TRIPWIRE_HOOK);

    // The specialized DeferredRegister.DataComponents simplifies data component registration and avoids some generic inference issues with the `DataComponentType.Builder` within a `Supplier`
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, DeepDelversMod.MODID);

    public static final Supplier<DataComponentType<Integer>> CATALYST_TIER = COMPONENTS.registerComponentType("catalyst_tier",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static final Supplier<DataComponentType<ResourceLocation>> CATALYST_DEPTH = COMPONENTS.registerComponentType("catalyst_depth",
            builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));

    public static final Supplier<DataComponentType<DataDriven<Block>>> CATALYST_FRAME_BLOCKS = COMPONENTS.registerComponentType("catalyst_frame_blocks",
            builder -> builder.persistent(DataDriven.DIRECT_CODEC(Registries.BLOCK)).networkSynchronized(DataDriven.STREAM_CODEC(Registries.BLOCK)));

    public static void register(IEventBus modEventBus)
    {
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        COMPONENTS.register(modEventBus);
    }

    public static void addToCreativeTab(CreativeModeTab.Output output)
    {
        for (ItemLike item : CreativeItems) {
            output.accept(item);
        }
    }

    public static <T extends Block> DeferredItem<BlockItem> RegisterBlockItem(DeferredBlock<T> block, boolean addToCreative)
    {
        DeferredItem<BlockItem> item = ITEMS.registerSimpleBlockItem(block.getKey().location().getPath(), block);
        if (addToCreative)
        {
            CreativeItems.add(item);
        }
        return item;
    }

    public static <T extends Block> DeferredItem<BlockItem> RegisterBlockItem(DeferredBlock<T> block)
    {
        return RegisterBlockItem(block, true);
    }

    public static <T extends Block, V extends Block> DeferredItem<BlockItem> RegisterBlockItem(DeferredBlock<T> block, DeferredBlock<V> visual, boolean addToCreative)
    {
        DeferredItem<BlockItem> item = ITEMS.registerSimpleBlockItem(block.getKey().location().getPath(), visual);
        if (addToCreative)
        {
            CreativeItems.add(item);
        }
        return item;
    }

    public static <T extends Block, V extends Block> DeferredItem<BlockItem> RegisterBlockItem(DeferredBlock<T> block, DeferredBlock<V> visual)
    {
        return RegisterBlockItem(block, visual,true);
    }

    public static <T extends DeepDelversItem> DeferredItem<T> RegisterItem(String name, Function<Item.Properties, T> constructor, Item.Properties properties, boolean addToCreative)
    {
        DeferredItem<T> item = ITEMS.registerItem(name, constructor, properties);
        if (addToCreative)
        {
            CreativeItems.add(item);
        }
        Datagen.add(item);
        return item;
    }

    public static <T extends DeepDelversItem> DeferredItem<T> RegisterItem(String name, Function<Item.Properties, T> constructor, Item.Properties properties)
    {
        return RegisterItem(name, constructor, properties, true);
    }

    public static DeferredItem<DeepDelversItem> RegisterItem(String name, Item.Properties properties, boolean addToCreative)
    {
        DeferredItem<DeepDelversItem> item = ITEMS.registerItem(name, DeepDelversItem::new, properties);
        if (addToCreative)
        {
            CreativeItems.add(item);
        }
        Datagen.add(item);
        return item;
    }

    public static DeferredItem<DeepDelversItem> RegisterItem(String name, Item.Properties properties)
    {
        return RegisterItem(name, DeepDelversItem::new, properties);
    }
}
