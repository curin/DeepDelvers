package com.azure_drake.deep_delvers.blocks.entities;

import com.azure_drake.deep_delvers.blocks.BlockManager;
import com.azure_drake.deep_delvers.gui.menus.DungeonTileManagerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DungeonTileManagerTileEntity extends BlockEntity implements MenuProvider
{
    public DungeonTileManagerTileEntity(BlockPos pos, BlockState blockState)
    {
        super(BlockManager.DUNGEON_TILE_MANAGER_ENTITY.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Dungeon Tile Manager");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new DungeonTileManagerMenu(containerId, playerInventory, this);
    }
}
