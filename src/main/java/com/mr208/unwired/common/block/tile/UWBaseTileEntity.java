package com.mr208.unwired.common.block.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public abstract class UWBaseTileEntity extends TileEntity
{
	public UWBaseTileEntity(TileEntityType<? extends TileEntity> type)
	{
		super(type);
	}
	
	private final Set<LazyOptional<?>> caps = new HashSet<>();
	
	public abstract void readCustomNBT(CompoundNBT compound, boolean descPacket);
	public abstract void writeCustomNBT(CompoundNBT compound, boolean descPacket);
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		this.readCustomNBT(compound, false);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		this.writeCustomNBT(compound, false);
		return compound;
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT compound = new CompoundNBT();
		this.writeCustomNBT(compound, true);
		return new SUpdateTileEntityPacket(this.pos, 3, compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.readCustomNBT(pkt.getNbtCompound(), true);
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT compound = super.getUpdateTag();
		writeCustomNBT(compound, true);
		return compound;
	}
	
	protected <T> LazyOptional<T> registerCapability(T val)
	{
		return registerCapability(() -> val);
	}
	
	protected <T> LazyOptional<T> registerCapability(NonNullSupplier<T> cap)
	{
		return registerCapability(LazyOptional.of(cap));
	}
	protected <T> LazyOptional<T> registerCapability(LazyOptional<T> cap)
	{
		caps.add(cap);
		return cap;
	}
	
	@Override
	public void remove()
	{
		super.remove();
		for(LazyOptional<?> cap : caps)
			if(cap.isPresent())
				cap.invalidate();
	}
	
	
}
