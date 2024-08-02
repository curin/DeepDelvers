package com.azure_drake.deep_delvers.blocks;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.blocks.entities.DungeonPortalTileEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockManager {
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DeepDelversMod.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, DeepDelversMod.MODID);
    public static final DeferredBlock<Block> DURALUMIN_BLOCK = BLOCKS.registerSimpleBlock("duralumin_block", BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK));
    public static final DeferredBlock<Block> RAW_DURALUMIN_BLOCK = BLOCKS.registerSimpleBlock("raw_duralumin_block", BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_COPPER_BLOCK));
    public static final DeferredBlock<Block> ALUMINUM_BLOCK = BLOCKS.registerSimpleBlock("aluminum_block", BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK));
    public static final DeferredBlock<Block> ALUMINUM_ORE = BLOCKS.registerSimpleBlock("aluminum_ore", BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE));
    public static final DeferredBlock<Block> RAW_ALUMINUM_BLOCK = BLOCKS.registerSimpleBlock("raw_aluminum_block", BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_COPPER_BLOCK));
    public static final DeferredBlock<Block> DEEPSLATE_ALUMINUM_ORE = BLOCKS.registerSimpleBlock("deepslate_aluminum_ore", BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE));

    public static final DeferredBlock<Block> DEEP_ROCK = BLOCKS.registerSimpleBlock("deep_rock", BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));

    public static final DeferredBlock<DungeonPortalBlock> DUNGEON_PORTAL = BLOCKS.register("dungeon_portal", DungeonPortalBlock::new);

    public static final DeferredBlock<DungeonPortalSpawner> DUNGEON_PORTAL_SPAWNER = BLOCKS.register("dungeon_portal_spawner", DungeonPortalSpawner::new);
    public static final Supplier<BlockEntityType<DungeonPortalTileEntity>> DUNGEON_PORTAL_ENTITY = BLOCK_ENTITIES.register("dungeon_portal",
            () -> BlockEntityType.Builder.of(DungeonPortalTileEntity::new, DUNGEON_PORTAL.get()).build(null));

    public static void register(IEventBus modEventBus)
    {
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
    }
}
