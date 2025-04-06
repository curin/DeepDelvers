package com.azure_drake.deep_delvers.gui;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.gui.menus.DungeonTileManagerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuManager
{
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, DeepDelversMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<DungeonTileManagerMenu>> DUNGEON_TILE_MANAGER_MENU =
            registerMenuType("dungeon_tile_manager_menu", DungeonTileManagerMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(
            String name, IContainerFactory<T> factory)
    {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus bus)
    {
        MENUS.register(bus);
    }
}
