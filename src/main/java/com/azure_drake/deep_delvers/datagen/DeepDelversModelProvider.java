package com.azure_drake.deep_delvers.datagen;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.blocks.BlockManager;
import com.azure_drake.deep_delvers.blocks.DeepDelversBlock;
import com.azure_drake.deep_delvers.items.DeepDelversItem;
import com.azure_drake.deep_delvers.items.ItemManager;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;

import java.util.function.Supplier;

public class DeepDelversModelProvider extends ModelProvider
{
    public DeepDelversModelProvider(PackOutput output) {
        super(output, DeepDelversMod.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        //Item Definitions
        for (Supplier<? extends DeepDelversItem> supplier : ItemManager.Datagen)
        {
            supplier.get().GenerateModel(itemModels);
        }

        //Block Definitions
        for (Supplier<? extends DeepDelversBlock> supplier : BlockManager.Datagen) {
            supplier.get().GenerateModel(blockModels);
        }
    }
}
