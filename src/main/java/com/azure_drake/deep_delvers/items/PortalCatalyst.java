package com.azure_drake.deep_delvers.items;

import com.azure_drake.deep_delvers.blocks.DeepBlockTags;
import com.azure_drake.deep_delvers.portal.DungeonPortalShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PortalCatalyst extends Item
{
    public PortalCatalyst() {
        super(new Properties().fireResistant().stacksTo(1));
    }

    @Override
    @NotNull
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Integer tier_int = stack.get(ItemManager.CATALYST_TIER);
        Integer depth_int = stack.get(ItemManager.CATALYST_DEPTH);
        int tier = tier_int == null ? 0 : tier_int;
        int depth = depth_int == null ? 0 : depth_int;

        if (!context.getLevel().getBlockState(context.getClickedPos()).is(DeepBlockTags.DEEP_DUNGEON_PORTAL)) {
            return super.onItemUseFirst(stack, context);
        }

        BlockPos pPos = new BlockPos(context.getClickedPos().getX() + context.getClickedFace().getStepX(), context.getClickedPos().getY() + context.getClickedFace().getStepY(), context.getClickedPos().getZ() + context.getClickedFace().getStepZ());
        Optional<DungeonPortalShape> optional = DungeonPortalShape.findEmptyPortalShape(context.getLevel(), pPos, Direction.Axis.X);

        if (optional.isEmpty()) {
            return super.onItemUseFirst(stack, context);
        }

        optional.get().createPortalBlocks(tier, depth);

        if (context.getPlayer() == null || !context.getPlayer().isCreative())
        {
            stack.setCount(stack.getCount() - 1);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.set(ItemManager.CATALYST_TIER, 0);
        stack.set(ItemManager.CATALYST_DEPTH, 0);

        return stack;
    }
}
