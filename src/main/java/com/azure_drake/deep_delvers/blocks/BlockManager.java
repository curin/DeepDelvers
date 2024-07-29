package com.azure_drake.deep_delvers.blocks;

import com.azure_drake.deep_delvers.DeepDelversMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockManager {
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DeepDelversMod.MODID);
    public static final DeferredBlock<Block> BRONZE_BLOCK = BLOCKS.registerSimpleBlock("bronze_block", BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK));
    public static final DeferredBlock<Block> RAW_BRONZE_BLOCK = BLOCKS.registerSimpleBlock("raw_bronze_block", BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_COPPER_BLOCK));
    public static final DeferredBlock<Block> TIN_BLOCK = BLOCKS.registerSimpleBlock("tin_block", BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK));
    public static final DeferredBlock<Block> TIN_ORE = BLOCKS.registerSimpleBlock("tin_ore", BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE));
    public static final DeferredBlock<Block> RAW_TIN_BLOCK = BLOCKS.registerSimpleBlock("raw_tin_block", BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_COPPER_BLOCK));
    public static final DeferredBlock<Block> DEEPSLATE_TIN_ORE = BLOCKS.registerSimpleBlock("deepslate_tin_ore", BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE));


    public static void register(IEventBus modEventBus)
    {
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
    }
}
