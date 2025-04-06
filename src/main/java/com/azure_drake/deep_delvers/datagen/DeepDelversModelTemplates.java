package com.azure_drake.deep_delvers.datagen;

import com.azure_drake.deep_delvers.blocks.ConnectedPillarState;
import com.azure_drake.deep_delvers.blocks.DungeonPortalFrame;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

public class DeepDelversModelTemplates
{

    public static final ModelTemplate PORTAL_NS_TEMPLATE = new ExtendedModelTemplateBuilder()
            .requiredTextureSlot(DeepDelversTextureSlots.PORTAL)
            .requiredTextureSlot(TextureSlot.PARTICLE)
            .element(elementBuilder -> elementBuilder
                .from(0,0,6)
                .to(16,16,10)
                .face(Direction.NORTH, faceBuilder -> faceBuilder
                    .uvs(0,0,16,16)
                    .texture(DeepDelversTextureSlots.PORTAL))
                .face(Direction.SOUTH, faceBuilder -> faceBuilder
                    .uvs(0,0,16,16)
                    .texture(DeepDelversTextureSlots.PORTAL)))
            .build();

    public static final TexturedModel.Provider PORTAL_NS_TEMPLATE_PROVIDER = TexturedModel.createDefault(
            // Block to texture mapping
            block -> new TextureMapping()
                    .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block))
                    .put(DeepDelversTextureSlots.PORTAL, TextureMapping.getBlockTexture(block)),
            // The template to generate from
            PORTAL_NS_TEMPLATE
    );

    public static final ModelTemplate PORTAL_EW_TEMPLATE = new ExtendedModelTemplateBuilder()
            .requiredTextureSlot(DeepDelversTextureSlots.PORTAL)
            .requiredTextureSlot(TextureSlot.PARTICLE)
            .element(elementBuilder -> elementBuilder
                    .from(6,0,0)
                    .to(10,16,16)
                    .face(Direction.EAST, faceBuilder -> faceBuilder
                            .uvs(0,0,16,16)
                            .texture(DeepDelversTextureSlots.PORTAL))
                    .face(Direction.WEST, faceBuilder -> faceBuilder
                            .uvs(0,0,16,16)
                            .texture(DeepDelversTextureSlots.PORTAL)))
            .build();

    public static final TexturedModel.Provider PORTAL_EW_TEMPLATE_PROVIDER = TexturedModel.createDefault(
            // Block to texture mapping
            block -> new TextureMapping()
                    .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block))
                    .put(DeepDelversTextureSlots.PORTAL, TextureMapping.getBlockTexture(block)),
            // The template to generate from
            PORTAL_EW_TEMPLATE
    );


}
