package com.azure_drake.deep_delvers.blocks;

import com.azure_drake.deep_delvers.Config;
import com.azure_drake.deep_delvers.blocks.entities.DungeonPortalSpawnerTileEntity;
import com.azure_drake.deep_delvers.blocks.entities.DungeonPortalTileEntity;
import com.azure_drake.deep_delvers.dungeon.DeepDungeon;
import com.azure_drake.deep_delvers.dungeon.DungeonID;
import com.azure_drake.deep_delvers.dungeon.DungeonManager;
import com.azure_drake.deep_delvers.portal.DungeonPortal;
import com.azure_drake.deep_delvers.portal.DungeonPortalShape;
import com.azure_drake.deep_delvers.portal.PortalID;
import com.azure_drake.deep_delvers.portal.PortalState;
import com.azure_drake.deep_delvers.world.DeepDelversData;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DungeonPortalSpawner extends DeepDelversBlock implements EntityBlock
{
    public DungeonPortalSpawner(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DungeonPortalSpawnerTileEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == BlockManager.DUNGEON_PORTAL_SPAWNER_ENTITY.get() ? (BlockEntityTicker<T>) DungeonPortalSpawnerTileEntity::tick : null;
    }

    @Override
    public void GenerateModel(BlockModelGenerators blockModels)
    {
        blockModels.createTrivialBlock(this, TexturedModel.createDefault(block -> TextureMapping.cube(BlockManager.DUNGEON_PORTAL.get()), ModelTemplates.CUBE_ALL));
    }
}
