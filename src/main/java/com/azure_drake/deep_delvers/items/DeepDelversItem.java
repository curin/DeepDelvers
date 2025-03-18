package com.azure_drake.deep_delvers.items;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.item.Item;

public class DeepDelversItem extends Item
{
    public DeepDelversItem(Properties properties) {
        super(properties);
    }

    public void GenerateModel(ItemModelGenerators itemModels)
    {
        itemModels.createFlatItemModel(this, ModelTemplates.FLAT_ITEM);
        ItemModel.Unbaked model = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(this));
        itemModels.itemModelOutput.accept(this, model);
    }
}
