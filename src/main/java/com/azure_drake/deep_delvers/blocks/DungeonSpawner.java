package com.azure_drake.deep_delvers.blocks;

import com.azure_drake.deep_delvers.blocks.entities.DungeonSpawnerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DungeonSpawner extends DeepDelversBlock implements EntityBlock {
    public DungeonSpawner(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DungeonSpawnerTileEntity(pos, state);
    }
}
