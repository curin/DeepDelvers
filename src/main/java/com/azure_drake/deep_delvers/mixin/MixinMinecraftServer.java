package com.azure_drake.deep_delvers.mixin;

import com.azure_drake.deep_delvers.dungeon.DungeonManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer
{
    @Redirect(method = "createLevels(Lnet/minecraft/server/level/progress/ChunkProgressListener;)V",
            at = @At(value ="INVOKE", target = "Lnet/minecraft/world/level/border/WorldBorder;addListener(Lnet/minecraft/world/level/border/BorderChangeListener;)V"))
    public void injection2(WorldBorder worldborder, BorderChangeListener pListener, @Local(ordinal = 1) ServerLevel serverlevel)
    {
        if (serverlevel.dimension() != DungeonManager.DEEP_DUGEON)
        {
            worldborder.addListener(new BorderChangeListener.DelegateBorderChangeListener(serverlevel.getWorldBorder()));
        }
    }
}
