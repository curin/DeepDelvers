package com.azure_drake.deep_delvers.blocks;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.world.level.block.Block;

public class DeepDelversBlock extends Block {
    public DeepDelversBlock(Properties p_49795_) {
        super(p_49795_);
    }

    public void GenerateModel(BlockModelGenerators blockModels)
    {
        blockModels.createTrivialCube(this);
    }
}
