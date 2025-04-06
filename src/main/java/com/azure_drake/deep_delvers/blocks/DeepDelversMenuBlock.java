package com.azure_drake.deep_delvers.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class DeepDelversMenuBlock extends DeepDelversBlock
{
    public DeepDelversMenuBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity entity = level.getBlockEntity(blockPos);
        if (!IsValidEntity(entity))
            return InteractionResult.FAIL;

        OpenMenu(player, blockPos, entity);

        return InteractionResult.SUCCESS;
    }

    public abstract void OpenMenu(Player player, BlockPos blockPos, BlockEntity entity);

    public abstract boolean IsValidEntity(BlockEntity blockEntity);
}
