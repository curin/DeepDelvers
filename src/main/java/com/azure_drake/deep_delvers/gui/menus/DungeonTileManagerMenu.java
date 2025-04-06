package com.azure_drake.deep_delvers.gui.menus;

import com.azure_drake.deep_delvers.blocks.BlockManager;
import com.azure_drake.deep_delvers.blocks.entities.DungeonTileManagerTileEntity;
import com.azure_drake.deep_delvers.gui.MenuManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class DungeonTileManagerMenu extends AbstractContainerMenu
{
    public final DungeonTileManagerTileEntity tileEntity;
    private final Level level;

    public DungeonTileManagerMenu(int containerId, Inventory inv, FriendlyByteBuf extraData)
    {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public DungeonTileManagerMenu(int containerId, Inventory inv, BlockEntity blockEntity)
    {
        super(MenuManager.DUNGEON_TILE_MANAGER_MENU.get(), containerId);

        tileEntity = (DungeonTileManagerTileEntity) blockEntity;
        level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, tileEntity.getBlockPos()),
                player, BlockManager.DUNGEON_TILE_MANAGER.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 140 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 198));
        }
    }
}
