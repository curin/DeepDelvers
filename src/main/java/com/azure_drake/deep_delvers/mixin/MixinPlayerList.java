package com.azure_drake.deep_delvers.mixin;

import com.azure_drake.deep_delvers.dungeon.DungeonManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList
{
    @Shadow
    public abstract void broadcastAll(Packet<?> pPacket, ResourceKey<Level> pDimension);

    @Redirect(method = "sendLevelInfo(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/level/ServerLevel;)V",
            at = @At(value ="INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"),
            slice = @Slice(from = @At("HEAD"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V", shift = At.Shift.AFTER)))
    public void sendLevelInfo(ServerGamePacketListenerImpl listener, Packet<?> pPacket, @Local ServerLevel pLevel)
    {
        if (pLevel.dimension() == DungeonManager.DEEP_DUGEON || !(pPacket instanceof ClientboundInitializeBorderPacket))
        {
            listener.send(new ClientboundInitializeBorderPacket(pLevel.getWorldBorder()));
        }
        else
        {
            listener.send(pPacket);
        }
    }

    @Redirect(method = "addWorldborderListener(Lnet/minecraft/server/level/ServerLevel;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/border/WorldBorder;addListener(Lnet/minecraft/world/level/border/BorderChangeListener;)V"))
    public void addWorldborderListener(WorldBorder worldborder, BorderChangeListener pListener, @Local ServerLevel pLevel)
    {
        worldborder.addListener(new BorderChangeListener() {
            @Override
            public void onBorderSizeSet(WorldBorder pBorder, double pSize) {
                Packet<?> pPacket = new ClientboundSetBorderSizePacket(pBorder);
                sendPacket(pPacket, pLevel);
            }

            @Override
            public void onBorderSizeLerping(WorldBorder pBorder, double pOldSize, double pNewSize, long pTime) {
                Packet<?> pPacket = new ClientboundSetBorderLerpSizePacket(pBorder);
                sendPacket(pPacket, pLevel);
            }

            @Override
            public void onBorderCenterSet(WorldBorder pBorder, double pX, double pZ) {
                Packet<?> pPacket = new ClientboundSetBorderCenterPacket(pBorder);
                sendPacket(pPacket, pLevel);
            }

            @Override
            public void onBorderSetWarningTime(WorldBorder pBorder, int pWarningTime) {
                Packet<?> pPacket = new ClientboundSetBorderWarningDelayPacket(pBorder);
                sendPacket(pPacket, pLevel);
            }

            @Override
            public void onBorderSetWarningBlocks(WorldBorder pBorder, int pWarningBlocks) {
                Packet<?> pPacket = new ClientboundSetBorderWarningDistancePacket(pBorder);
                sendPacket(pPacket, pLevel);
            }

            @Override
            public void onBorderSetDamagePerBlock(WorldBorder pBorder, double pDamagePerBlock) {

            }

            @Override
            public void onBorderSetDamageSafeZOne(WorldBorder pBorder, double pDamageSafeZone) {

            }
        });
    }

    private void sendPacket(Packet<?> pPacket, @Local ServerLevel pLevel) {
        if (pLevel.dimension() == DungeonManager.DEEP_DUGEON) {
            broadcastAll(pPacket, pLevel.dimension());
        }
        else
        {
            for (ResourceKey<Level> level : pLevel.getServer().levelKeys())
            {
                if (level == DungeonManager.DEEP_DUGEON)
                {
                    continue;
                }
                broadcastAll(pPacket, level);
            }
        }
    }
}
