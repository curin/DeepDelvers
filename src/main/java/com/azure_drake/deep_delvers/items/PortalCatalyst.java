package com.azure_drake.deep_delvers.items;

import com.azure_drake.deep_delvers.blocks.DeepBlockTags;
import com.azure_drake.deep_delvers.datagen.DataDriven;
import com.azure_drake.deep_delvers.dungeon.DungeonDepth;
import com.azure_drake.deep_delvers.dungeon.DungeonRegistries;
import com.azure_drake.deep_delvers.portal.DungeonPortalShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PortalCatalyst extends DeepDelversItem
{
    public static final DataDriven<Block> DEFAULT_FRAME_BLOCKS = new DataDriven<>(Optional.of(DeepBlockTags.DEEP_DUNGEON_PORTAL), Optional.empty());
    public PortalCatalyst(Properties properties) {
        super(properties
                .component(ItemManager.CATALYST_TIER, 0)
                .component(ItemManager.CATALYST_DEPTH, DungeonDepth.DEFAULT)
                .component(ItemManager.CATALYST_FRAME_BLOCKS, DEFAULT_FRAME_BLOCKS));
    }

    @Override
    @NotNull
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getLevel().isClientSide)
            return InteractionResult.SUCCESS;
        Integer tier_int = stack.get(ItemManager.CATALYST_TIER);
        ResourceLocation depth_in = stack.get(ItemManager.CATALYST_DEPTH);
        DataDriven<Block> valid_frame = stack.get(ItemManager.CATALYST_FRAME_BLOCKS);
        valid_frame = valid_frame == null || (valid_frame.Key().isEmpty() && valid_frame.Tag().isEmpty()) ? DEFAULT_FRAME_BLOCKS : valid_frame;
        int tier = tier_int == null ? 0 : tier_int;
        ResourceLocation depth_loc = depth_in == null ? DungeonDepth.DEFAULT : depth_in;

        Registry<DungeonDepth> depthRegistry = DungeonRegistries.DUNGEON_DEPTHS((ServerLevel) context.getLevel());
        if (!depthRegistry.containsKey(depth_loc))
        {
            depth_loc = DungeonDepth.DEFAULT;
        }

        Optional<Holder.Reference<DungeonDepth>> depth = depthRegistry.get(depth_loc);

        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if ((valid_frame.Tag().isEmpty() || !state.is(valid_frame.Tag().get())) || (valid_frame.Key().isEmpty() || !state.is(valid_frame.Key().get())))
        {
            return super.onItemUseFirst(stack, context);
        }

        BlockPos pPos = new BlockPos(context.getClickedPos().getX() + context.getClickedFace().getStepX(), context.getClickedPos().getY() + context.getClickedFace().getStepY(), context.getClickedPos().getZ() + context.getClickedFace().getStepZ());
        Optional<DungeonPortalShape> optional = DungeonPortalShape.findEmptyPortalShape(context.getLevel(), pPos, Direction.Axis.X, valid_frame);

        if (optional.isEmpty() || optional.get().createPortalBlocks(tier, depth.get()).Id == -1) {
            return super.onItemUseFirst(stack, context);
        }

        if (context.getPlayer() == null || !context.getPlayer().isCreative())
        {
            stack.setCount(stack.getCount() - 1);
        }
        return InteractionResult.SUCCESS;
    }
}
