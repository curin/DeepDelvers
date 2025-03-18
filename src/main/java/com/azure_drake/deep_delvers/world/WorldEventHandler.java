package com.azure_drake.deep_delvers.world;

import com.azure_drake.deep_delvers.DeepDelversMod;
import com.azure_drake.deep_delvers.dungeon.DungeonManager;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class WorldEventHandler
{
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event)
    {
        if (event.getEntity().level().isClientSide || event.getEntity().getServer() == null ||
                !(event.getEntity() instanceof Player player) || player.level().dimension() != DungeonManager.DEEP_DUGEON)
        {
            return;
        }

        DeepDelversData data = DeepDelversData.get(player.getServer().getLevel(DungeonManager.DEEP_DUGEON));
        String uuid = player.getStringUUID();
        data.removePlayerFromDungeons(player.getServer(), uuid);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        if (event.getEntity().level().isClientSide || event.getEntity().getServer() == null || event.getFrom() != DungeonManager.DEEP_DUGEON)
        {
            return;
        }

        DeepDelversData data = DeepDelversData.get(event.getEntity().getServer().getLevel(DungeonManager.DEEP_DUGEON));
        String uuid = event.getEntity().getStringUUID();
        data.removePlayerFromDungeons(event.getEntity().getServer(), uuid);
    }
}
