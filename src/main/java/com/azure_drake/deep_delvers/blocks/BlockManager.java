package com.azure_drake.deep_delvers.blocks;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.blocks.entities.DungeonPortalSpawnerTileEntity;
import com.azure_drake.deep_delvers.blocks.entities.DungeonPortalTileEntity;
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

    public static final DeferredBlock<DungeonPortalBlock> DUNGEON_PORTAL = RegisterBlock("dungeon_portal", DungeonPortalBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_PORTAL).lightLevel(i -> 8));

    public static final DeferredBlock<DungeonPortalSpawner> DUNGEON_PORTAL_SPAWNER = RegisterBlock("dungeon_portal_spawner", DungeonPortalSpawner::new, BlockBehaviour.Properties.ofFullCopy(Blocks.FIRE));

    public static final DeferredBlock<DungeonPortalFrame> DUNGEON_PORTAL_FRAME = RegisterBlock("dungeon_portal_frame", DungeonPortalFrame::Standard, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS));
    public static final DeferredBlock<DungeonPortalFrame> DUNGEON_PORTAL_FRAME_COSMETIC = RegisterBlock("dungeon_portal_frame_cosmetic", DungeonPortalFrame::Cosmetic, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS));
    public static final Supplier<BlockEntityType<DungeonPortalTileEntity>> DUNGEON_PORTAL_ENTITY = BLOCK_ENTITIES.register("dungeon_portal",
            () -> new BlockEntityType<>(DungeonPortalTileEntity::new, DUNGEON_PORTAL.get()));

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
