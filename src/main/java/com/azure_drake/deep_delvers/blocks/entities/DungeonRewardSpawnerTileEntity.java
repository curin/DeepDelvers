package com.azure_drake.deep_delvers.blocks.entities;

import com.azure_drake.deep_delvers.blocks.BlockManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DungeonRewardSpawnerTileEntity extends BlockEntity {
    public DungeonRewardSpawnerTileEntity(BlockPos pos, BlockState blockState) {
        super(BlockManager.DUNGEON_REWARD_SPAWNER_ENTITY.get(), pos, blockState);
    }
}
