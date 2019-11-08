package com.mr208.unwired.common.util.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class UWEnergyStorage extends EnergyStorage
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
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		return super.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return super.extractEnergy(maxExtract, simulate);
	}
	
	public void setEnergy(int amount)
	{
		this.energy = Math.max(0,Math.min(getMaxEnergyStored(), amount));
	}
	
	public void setMaxEnergy(int amount)
	{
		this.capacity = amount;
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
		compound.putInt("Energy", energy);
	}
	
	public void readFromNBT(CompoundNBT compound)
	{
		energy = compound.getInt("Energy");
	}
}
