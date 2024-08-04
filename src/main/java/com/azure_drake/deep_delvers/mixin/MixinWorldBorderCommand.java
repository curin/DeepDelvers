package com.azure_drake.deep_delvers.mixin;

import com.azure_drake.deep_delvers.dungeon.DungeonManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.WorldBorderCommand;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldBorderCommand.class)
public class MixinWorldBorderCommand
{
    @ModifyVariable(method = "setDamageBuffer(Lnet/minecraft/commands/CommandSourceStack;F)I", at = @At("STORE"), ordinal = 0)
    private static WorldBorder setDamageBuffer(WorldBorder worldBorder, @Local CommandSourceStack pSource)
    {
        return pSource.getLevel().dimension() == DungeonManager.DEEP_DUGEON ? pSource.getLevel().getWorldBorder() : worldBorder;
    }

    @ModifyVariable(method = "setDamageAmount(Lnet/minecraft/commands/CommandSourceStack;F)I", at = @At("STORE"), ordinal = 0)
    private static WorldBorder setDamageAmount(WorldBorder worldBorder, @Local CommandSourceStack pSource)
    {
        return pSource.getLevel().dimension() == DungeonManager.DEEP_DUGEON ? pSource.getLevel().getWorldBorder() : worldBorder;
    }

    @ModifyVariable(method = "setWarningTime(Lnet/minecraft/commands/CommandSourceStack;I)I", at = @At("STORE"), ordinal = 0)
    private static WorldBorder setWarningTime(WorldBorder worldBorder, @Local CommandSourceStack pSource)
    {
        return pSource.getLevel().dimension() == DungeonManager.DEEP_DUGEON ? pSource.getLevel().getWorldBorder() : worldBorder;
    }

    @ModifyVariable(method = "setWarningDistance(Lnet/minecraft/commands/CommandSourceStack;I)I", at = @At("STORE"), ordinal = 0)
    private static WorldBorder setWarningDistance(WorldBorder worldBorder, @Local CommandSourceStack pSource)
    {
        return pSource.getLevel().dimension() == DungeonManager.DEEP_DUGEON ? pSource.getLevel().getWorldBorder() : worldBorder;
    }

    @ModifyVariable(method = "getSize(Lnet/minecraft/commands/CommandSourceStack;)I", at = @At("STORE"), ordinal = 0)
    private static double getSize(double worldBorderSize, @Local CommandSourceStack pSource)
    {
        return pSource.getLevel().dimension() == DungeonManager.DEEP_DUGEON ? pSource.getLevel().getWorldBorder().getSize() : worldBorderSize;
    }

    @ModifyVariable(method = "setCenter(Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/world/phys/Vec2;)I", at = @At("STORE"), ordinal = 0)
    private static WorldBorder setCenter(WorldBorder worldBorder, @Local CommandSourceStack pSource)
    {
        return pSource.getLevel().dimension() == DungeonManager.DEEP_DUGEON ? pSource.getLevel().getWorldBorder() : worldBorder;
    }

    @ModifyVariable(method = "setSize(Lnet/minecraft/commands/CommandSourceStack;DJ)I", at = @At("STORE"), ordinal = 0)
    private static WorldBorder setSize(WorldBorder worldBorder, @Local CommandSourceStack pSource)
    {
        return pSource.getLevel().dimension() == DungeonManager.DEEP_DUGEON ? pSource.getLevel().getWorldBorder() : worldBorder;
    }
}
