package com.azure_drake.deep_delvers.blocks;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.blocks.entities.*;
import com.azure_drake.deep_delvers.items.DeepDelversItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockManager {
    public static List<Supplier<? extends DeepDelversBlock>> Datagen = new ArrayList<>();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DeepDelversMod.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, DeepDelversMod.MODID);

    public static final DeferredBlock<DeepDelversBlock> DURALUMIN_BLOCK = RegisterBlock("duralumin_block", BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK));
    public static final DeferredBlock<DeepDelversBlock> RAW_DURALUMIN_BLOCK = RegisterBlock("raw_duralumin_block", BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_COPPER_BLOCK));
    public static final DeferredBlock<DeepDelversBlock> ALUMINUM_BLOCK = RegisterBlock("aluminum_block", BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK));
    public static final DeferredBlock<DeepDelversBlock> ALUMINUM_ORE = RegisterBlock("aluminum_ore", BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE));
    public static final DeferredBlock<DeepDelversBlock> RAW_ALUMINUM_BLOCK = RegisterBlock("raw_aluminum_block", BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_COPPER_BLOCK));
    public static final DeferredBlock<DeepDelversBlock> DEEPSLATE_ALUMINUM_ORE = RegisterBlock("deepslate_aluminum_ore", BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE));

    public static final DeferredBlock<DeepDelversBlock> DEEP_ROCK = RegisterBlock("deep_rock", BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));

    public static final DeferredBlock<DungeonPortalFrame> DUNGEON_PORTAL_FRAME = RegisterBlock("dungeon_portal_frame", DungeonPortalFrame::Standard, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS));
    public static final DeferredBlock<DungeonPortalFrame> DUNGEON_PORTAL_FRAME_COSMETIC = RegisterBlock("dungeon_portal_frame_cosmetic", DungeonPortalFrame::Cosmetic, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS));

    public static final DeferredBlock<DungeonPressurePlate> DUNGEON_PRESSURE_PLATE = RegisterBlock("dungeon_pressure_plate", DungeonPressurePlate::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS));

    public static final DeferredBlock<DungeonBlockRandomizer> DUNGEON_BLOCK_RANDOMIZER = RegisterBlock("dungeon_block_randomizer", DungeonBlockRandomizer::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));
    public static final Supplier<BlockEntityType<DungeonBlockRandomizerTileEntity>> DUNGEON_BLOCK_RANDOMIZER_ENTITY = BLOCK_ENTITIES.register("dungeon_block_randomizer",
            () -> new BlockEntityType<>(DungeonBlockRandomizerTileEntity::new, DUNGEON_BLOCK_RANDOMIZER.get()));

    public static final DeferredBlock<DungeonRewardSpawner> DUNGEON_REWARD_SPAWNER = RegisterBlock("dungeon_reward_spawner", DungeonRewardSpawner::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));
    public static final Supplier<BlockEntityType<DungeonRewardSpawnerTileEntity>> DUNGEON_REWARD_SPAWNER_ENTITY = BLOCK_ENTITIES.register("dungeon_reward_spawner",
            () -> new BlockEntityType<>(DungeonRewardSpawnerTileEntity::new, DUNGEON_REWARD_SPAWNER.get()));

    public static final DeferredBlock<DungeonRewardSpawner> DUNGEON_SPAWNER = RegisterBlock("dungeon_spawner", DungeonRewardSpawner::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));
    public static final Supplier<BlockEntityType<DungeonSpawnerTileEntity>> DUNGEON_SPAWNER_ENTITY = BLOCK_ENTITIES.register("dungeon_spawner",
            () -> new BlockEntityType<>(DungeonSpawnerTileEntity::new, DUNGEON_SPAWNER.get()));

    public static final DeferredBlock<DungeonThemeManager> DUNGEON_THEME_MANAGER = RegisterBlock("dungeon_theme_manager", DungeonThemeManager::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));
    public static final Supplier<BlockEntityType<DungeonThemeManagerTileEntity>> DUNGEON_THEME_MANAGER_ENTITY = BLOCK_ENTITIES.register("dungeon_theme_manager",
            () -> new BlockEntityType<>(DungeonThemeManagerTileEntity::new, DUNGEON_THEME_MANAGER.get()));

    public static final DeferredBlock<DungeonTileManager> DUNGEON_TILE_MANAGER = RegisterBlock("dungeon_tile_manager", DungeonTileManager::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));
    public static final Supplier<BlockEntityType<DungeonTileManagerTileEntity>> DUNGEON_TILE_MANAGER_ENTITY = BLOCK_ENTITIES.register("dungeon_tile_manager",
            () -> new BlockEntityType<>(DungeonTileManagerTileEntity::new, DUNGEON_TILE_MANAGER.get()));

    public static final DeferredBlock<HiddenTripwireHook> HIDDEN_TRIPWIRE_HOOK = RegisterBlock("hidden_tripwire_hook", HiddenTripwireHook::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));
    public static final Supplier<BlockEntityType<HiddenTripwireHookTileEntity>> HIDDEN_TRIPWIRE_HOOK_ENTITY = BLOCK_ENTITIES.register("hidden_tripwire_hook",
            () -> new BlockEntityType<>(HiddenTripwireHookTileEntity::new, HIDDEN_TRIPWIRE_HOOK.get()));

    public static final DeferredBlock<DungeonPortalBlock> DUNGEON_PORTAL = RegisterBlock("dungeon_portal", DungeonPortalBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_PORTAL).lightLevel(i -> 8));
    public static final Supplier<BlockEntityType<DungeonPortalTileEntity>> DUNGEON_PORTAL_ENTITY = BLOCK_ENTITIES.register("dungeon_portal",
            () -> new BlockEntityType<>(DungeonPortalTileEntity::new, DUNGEON_PORTAL.get()));

    public static final DeferredBlock<DungeonPortalSpawner> DUNGEON_PORTAL_SPAWNER = RegisterBlock("dungeon_portal_spawner", DungeonPortalSpawner::new, BlockBehaviour.Properties.ofFullCopy(Blocks.FIRE));
    public static final Supplier<BlockEntityType<DungeonPortalSpawnerTileEntity>> DUNGEON_PORTAL_SPAWNER_ENTITY = BLOCK_ENTITIES.register("dungeon_portal_spawner",
            () -> new BlockEntityType<>(DungeonPortalSpawnerTileEntity::new, DUNGEON_PORTAL_SPAWNER.get()));

    public static void register(IEventBus modEventBus)
    {
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
    }

    public static <T extends DeepDelversBlock> DeferredBlock<T> RegisterBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties props)
    {
        DeferredBlock<T> block = BLOCKS.registerBlock(name, func, props);
        Datagen.add(block);
        return block;
    }

    public static DeferredBlock<DeepDelversBlock> RegisterBlock(String name, BlockBehaviour.Properties props)
    {
        DeferredBlock<DeepDelversBlock> block = BLOCKS.registerBlock(name, DeepDelversBlock::new, props);
        Datagen.add(block);
        return block;
    }
}
