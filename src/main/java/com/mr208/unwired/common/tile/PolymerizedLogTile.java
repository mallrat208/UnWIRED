package com.mr208.unwired.common.tile;

import com.mr208.unwired.common.content.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants.BlockFlags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PolymerizedLogTile extends UWBaseTileEntity
{
	private BlockState cachedState;
	
	public static final ModelProperty<BlockState> cachedModel = new ModelProperty<>();
	
	public PolymerizedLogTile()
	{
		super(ModTileEntities.polymerized_log);
	}
	
	public void setCachedState(BlockState cachedState)
	{
		this.cachedState=cachedState;
	}
	
	public BlockState getCachedState()
	{
		return cachedState;
	}
	
	@Override
	public boolean hasFastRenderer()
	{
		return false;
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(pos,0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		BlockState oldCache = cachedState;
		read(pkt.getNbtCompound());
		if(oldCache!= cachedState)
		{
			ModelDataManager.requestModelDataRefresh(this);
			world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), BlockFlags.BLOCK_UPDATE+BlockFlags.NOTIFY_NEIGHBORS);
		}
	}
	
	@Override
	public void readCustomNBT(CompoundNBT compound, boolean descPacket)
	{
		cachedState = NBTUtil.readBlockState(compound.getCompound("State"));
	}
	
	@Override
	public void writeCustomNBT(CompoundNBT compound, boolean descPacket)
	{
		compound.put("State", NBTUtil.writeBlockState(cachedState));
	}
	
	@Nonnull
	@Override
	public IModelData getModelData()
	{
		return new ModelDataMap.Builder()
				.withInitial(cachedModel, cachedState)
				.build();
	}
}
