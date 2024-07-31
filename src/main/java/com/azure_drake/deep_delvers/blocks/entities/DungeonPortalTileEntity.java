package com.azure_drake.deep_delvers.blocks.entities;

import com.azure_drake.deep_delvers.blocks.BlockManager;
import com.azure_drake.deep_delvers.portal.PortalID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DungeonPortalTileEntity extends BlockEntity
{
    private PortalID PortalId = new PortalID(0, 0);
    public static final String ID_TAG = "portal_id";

    public DungeonPortalTileEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockManager.DUNGEON_PORTAL_ENTITY.get(), pPos, pBlockState);
    }

    public PortalID getPortalId()
    {
        return PortalId;
    }

    public void setPortalId(PortalID portalId)
    {
        PortalId = portalId;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        saveClientData(pTag, pRegistries);
    }

    private void saveClientData(CompoundTag pTag, HolderLookup.Provider pRegistries)
    {
        pTag.put(ID_TAG, PortalId.serializeNBT());
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        loadClientData(pTag, pRegistries);
    }

    private void loadClientData(CompoundTag pTag, HolderLookup.Provider pRegistries)
    {
        if (pTag.contains(ID_TAG))
        {
            PortalId.deserializeNBT(pTag.getCompound(ID_TAG));
        }
    }
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);
        saveClientData(tag, pRegistries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        if (tag != null)
        {
            loadClientData(tag, lookupProvider);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        // This is called client side
        CompoundTag tag = pkt.getTag();
        // This will call loadClientData()
        if (tag != null) {
            handleUpdateTag(tag, lookupProvider);
        }
    }
}
