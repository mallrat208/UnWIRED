package com.mr208.unwired.common.block.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class AbstractRedstoneMachineBase extends LockableSidedInvTile implements ITickableTileEntity
{
	protected RedstoneMode redstoneMode = RedstoneMode.OFF_REQUIRED;
	
	protected AbstractRedstoneMachineBase(TileEntityType<?> typeIn)
	{
		super(typeIn);
	}
	
	@Override
	public void tick()
	{
	
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		this.redstoneMode = RedstoneMode.values()[compound.getShort("RedstoneMode")];
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putShort("RedstoneMode", (short)this.redstoneMode.ordinal());
		return super.write(compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		super.onDataPacket(net, pkt);
		this.redstoneMode = RedstoneMode.values()[pkt.getNbtCompound().getShort("RedstoneMode")];
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT compound = super.getUpdateTag();
		compound.putShort("RedstoneMode", (short)this.redstoneMode.ordinal());
		return compound;
	}
	
	public RedstoneMode getRedstoneMode()
	{
		return redstoneMode;
	}
	
	public void setRedstoneMode(RedstoneMode redstoneMode)
	{
		this.redstoneMode = redstoneMode;
	}
	
	public enum RedstoneMode
	{
		IGNORED,
		OFF_REQUIRED
	}
}
