package com.mr208.unwired.common.util;

import net.minecraftforge.energy.IEnergyStorage;

public class FluxStorage implements IEnergyStorage
{
	protected int energy;
	protected int capacity;
	protected int maxInput;
	protected int maxOutput;
	
	public FluxStorage(int capacity)
	{
		this(capacity, capacity, capacity, 0);
	}
	
	public FluxStorage(int capacity, int maxTransfer)
	{
		this(capacity, maxTransfer, maxTransfer, 0);
	}
	
	public FluxStorage(int capacity, int maxInput, int maxOutput)
	{
		this(capacity, maxInput, maxOutput, 0);
	}
	
	public FluxStorage(int capacity, int maxInput, int maxOutput, int startingEnergy)
	{
		this.capacity = capacity;
		this.maxInput = maxInput;
		this.maxOutput = maxOutput;
		this.energy = Math.max(0, Math.min(capacity, startingEnergy));
	}
	
	public void setEnergy(int energy)
	{
		this.energy = Math.max(0, Math.min(capacity, energy));
	}
	
	@Override
	public int receiveEnergy(int i, boolean b)
	{
		if(!this.canReceive())
			return 0;
		
		int energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxInput, i));
		if(!b)
			this.energy += energyReceived;
		
		return energyReceived;
	}
	
	@Override
	public int extractEnergy(int i, boolean b)
	{
		if(!canExtract())
			return 0;
		
		int energyExtracted = Math.min(this.energy, Math.min(this.maxOutput, i));
		if(!b)
			this.energy -= energyExtracted;
		
		return energyExtracted;
	}
	
	@Override
	public int getEnergyStored()
	{
		return this.energy;
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return this.capacity;
	}
	
	@Override
	public boolean canExtract()
	{
		return this.maxOutput > 0;
	}
	
	@Override
	public boolean canReceive()
	{
		return this.maxInput > 0;
	}
	
	
}
