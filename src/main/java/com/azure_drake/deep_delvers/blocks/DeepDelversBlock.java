package com.azure_drake.deep_delvers.blocks;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.world.level.block.Block;

public class DeepDelversBlock extends Block {
    public DeepDelversBlock(Properties properties) {
        super(properties);
    }

    public void GenerateModel(BlockModelGenerators blockModels)
    {
        blockModels.createTrivialCube(this);
    }
}
