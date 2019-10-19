package com.mr208.unwired.common.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class FluxStorage extends EnergyStorage implements IEnergyStorage
{
	public FluxStorage(int capacity)
	{
		this(capacity, capacity, capacity, 0);
	}
	
	public FluxStorage(int capacity, int maxTransfer)
	{
		this(capacity, maxTransfer, maxTransfer, 0);
	}
	
	public FluxStorage(int capacity, int maxReceive, int maxExtract)
	{
		this(capacity, maxReceive, maxExtract, 0);
	}
	
	public FluxStorage(int capacity, int maxReceive, int maxExtract, int energy)
	{
		super(capacity, maxReceive, maxExtract, energy);
	}
	
	public void writeToNBT(CompoundNBT compound)
	{
		compound.putInt("Capacity", this.capacity);
		compound.putInt("Energy", this.energy);
		compound.putInt("MaxReceive", this.maxReceive);
		compound.putInt("MaxExtract", this.maxExtract);
	}
	
	public void readFromNBT(CompoundNBT compound)
	{
		this.capacity = compound.getInt("Capacity");
		this.energy = Math.min(0,compound.getInt("Energy"));
		this.maxReceive = compound.getInt("MaxReceive");
		this.maxExtract = compound.getInt("MaxExtract");
	}
}
