package com.azure_drake.deep_delvers.datagen;

import com.azure_drake.deep_delvers.blocks.ConnectedPillarState;
import com.azure_drake.deep_delvers.blocks.DungeonPortalFrame;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DeepDelversBlockStates {
    public static BlockStateGenerator DungeonPortalFrame(Block This, Block location) {
        return MultiVariantGenerator.multiVariant(This)
                .with(
                        PropertyDispatch.properties(DungeonPortalFrame.FACING, DungeonPortalFrame.CONNECTED_STATE)
                                .select(
                                        Direction.UP,
                                        ConnectedPillarState.Single,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location))
                                )
                                .select(
                                        Direction.DOWN,
                                        ConnectedPillarState.Single,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location))
                                )
                                .select(
                                        Direction.NORTH,
                                        ConnectedPillarState.Single,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location))
                                )
                                .select(
                                        Direction.SOUTH,
                                        ConnectedPillarState.Single,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location))
                                )
                                .select(
                                        Direction.EAST,
                                        ConnectedPillarState.Single,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location))
                                )
                                .select(
                                        Direction.WEST,
                                        ConnectedPillarState.Single,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location))
                                )
                                .select(
                                        Direction.UP,
                                        ConnectedPillarState.Middle,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_middle"))
                                )
                                .select(
                                        Direction.DOWN,
                                        ConnectedPillarState.Middle,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_middle"))
                                )
                                .select(
                                        Direction.NORTH,
                                        ConnectedPillarState.Middle,
                                        Variant.variant()
                                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_middle_horizontal"))
                                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                )
                                .select(
                                        Direction.SOUTH,
                                        ConnectedPillarState.Middle,
                                        Variant.variant()
                                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_middle_horizontal"))
                                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                )
                                .select(
                                        Direction.EAST,
                                        ConnectedPillarState.Middle,
                                        Variant.variant()
                                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_middle_horizontal"))
                                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                )
                                .select(
                                        Direction.WEST,
                                        ConnectedPillarState.Middle,
                                        Variant.variant()
                                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_middle_horizontal"))
                                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                )
                                .select(
                                        Direction.UP,
                                        ConnectedPillarState.Edge,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_edge"))
                                )
                                .select(
                                        Direction.DOWN,
                                        ConnectedPillarState.Edge,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_edge"))
                                )
                                .select(
                                        Direction.NORTH,
                                        ConnectedPillarState.Edge,
                                        Variant.variant()
                                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_edge_horizontal"))
                                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                )
                                .select(
                                        Direction.SOUTH,
                                        ConnectedPillarState.Edge,
                                        Variant.variant()
                                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_edge_horizontal"))
                                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                )
                                .select(
                                        Direction.EAST,
                                        ConnectedPillarState.Edge,
                                        Variant.variant()
                                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_edge_horizontal"))
                                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                )
                                .select(
                                        Direction.WEST,
                                        ConnectedPillarState.Edge,
                                        Variant.variant()
                                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_edge_horizontal"))
                                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                )
                                .select(
                                        Direction.UP,
                                        ConnectedPillarState.Cap,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_cap"))
                                )
                                .select(
                                        Direction.DOWN,
                                        ConnectedPillarState.Cap,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_cap"))
                                )
                                .select(
                                        Direction.NORTH,
                                        ConnectedPillarState.Cap,
                                        Variant.variant()
                                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_cap_horizontal"))
                                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                )
                                .select(
                                        Direction.SOUTH,
                                        ConnectedPillarState.Cap,
                                        Variant.variant()
                                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_cap_horizontal"))
                                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)
                                )
                                .select(
                                        Direction.EAST,
                                        ConnectedPillarState.Cap,
                                        Variant.variant()
                                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_cap_horizontal"))
                                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                )
                                .select(
                                        Direction.WEST,
                                        ConnectedPillarState.Cap,
                                        Variant.variant()
                                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(location, "_cap_horizontal"))
                                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                )
                );
    }

    public static BlockStateGenerator DungeonPortal(Block This)
    {
        return MultiVariantGenerator.multiVariant(This)
                .with(
                        PropertyDispatch.property(BlockStateProperties.HORIZONTAL_AXIS)
                                .select(
                                        Direction.Axis.X,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(This, "_ns"))
                                )
                                .select(
                                        Direction.Axis.Z,
                                        Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(This, "_ew"))
                                )
                );
    }
}
