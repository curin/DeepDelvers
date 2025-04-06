package com.azure_drake.deep_delvers.gui.screens;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.gui.menus.DungeonTileManagerMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DungeonTileManagerScreen extends AbstractContainerScreen<DungeonTileManagerMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(DeepDelversMod.MODID, "textures/gui/tile_manager/bg.png");

    public DungeonTileManagerScreen(DungeonTileManagerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        inventoryLabelY = 129;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderType::guiTextured, GUI_TEXTURE, x, y, 0,0, 176, 222, 176, 222);
    }
}
