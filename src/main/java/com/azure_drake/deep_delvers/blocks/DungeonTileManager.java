package com.azure_drake.deep_delvers.blocks;

import com.azure_drake.deep_delvers.blocks.entities.DungeonTileManagerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DungeonTileManager extends DeepDelversMenuBlock implements EntityBlock
{
    public DungeonTileManager(Properties properties) {
        super(properties);
    }

    @Override
    public void OpenMenu(Player player, BlockPos blockPos, BlockEntity entity) {
        player.openMenu(new SimpleMenuProvider((DungeonTileManagerTileEntity)entity, Component.literal("Dungeon Tile Manager")), blockPos);
    }

    @Override
    public boolean IsValidEntity(BlockEntity blockEntity) {
        return blockEntity instanceof DungeonTileManagerTileEntity;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DungeonTileManagerTileEntity(pos, state);
    }
}
