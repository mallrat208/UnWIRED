package com.mr208.unwired.common.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class UWEnergyStorage extends EnergyStorage implements IEnergyStorage
{
	public UWEnergyStorage(int capacity)
	{
		this(capacity, capacity, capacity, 0);
	}
	
	public UWEnergyStorage(int capacity, int maxTransfer)
	{
		this(capacity, maxTransfer, maxTransfer, 0);
	}
	
	public UWEnergyStorage(int capacity, int maxReceive, int maxExtract)
	{
		this(capacity, maxReceive, maxExtract, 0);
	}
	
	public UWEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
	{
		super(capacity, maxReceive, maxExtract, energy);
	}
	
	public void setEnergy(int amount)
	{
		this.energy = Math.max(0,Math.min(getMaxEnergyStored(), amount));
	}
	
	public void addEnergy(int amount)
	{
		this.energy = Math.min(getMaxEnergyStored(),this.energy + amount);
	}
	
	public void consumeEnergy(int amount)
	{
		this.energy = Math.max(0, this.energy - amount);
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
		this.energy = Math.max(0,compound.getInt("Energy"));
		this.maxReceive = compound.getInt("MaxReceive");
		this.maxExtract = compound.getInt("MaxExtract");
	}
}
