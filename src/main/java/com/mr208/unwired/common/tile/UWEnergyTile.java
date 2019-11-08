package com.mr208.unwired.common.tile;

import com.mr208.unwired.common.util.energy.UWEnergyStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class UWEnergyTile extends UWBaseTileEntity implements ITickableTileEntity
{
	UWEnergyStorage energyStorage;
	LazyOptional<IEnergyStorage> energyHandler;
	
	public UWEnergyTile(TileEntityType<? extends TileEntity> type)
	{
		super(type);
		this.energyStorage = createEnergyStorage();
		this.energyHandler = registerCapability(energyStorage);
	}
	
	public abstract UWEnergyStorage createEnergyStorage();
	
	public abstract void tick();
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == CapabilityEnergy.ENERGY)
		{
			return energyHandler.cast();
		}
		
		return super.getCapability(cap, side);
	}
	
	@Override
	public void readCustomNBT(CompoundNBT compound, boolean descPacket)
	{
		energyStorage.readFromNBT(compound);
	}
	
	@Override
	public void writeCustomNBT(CompoundNBT compound, boolean descPacket)
	{
		energyStorage.writeToNBT(compound);
	}
	
	public int receiveEnergy(int energy, boolean simulate)
	{
		return energyStorage.receiveEnergy(energy, simulate);
	}
	
	public int extractEnergy(int energy, boolean simulate)
	{
		return energyStorage.extractEnergy(energy, simulate);
	}
	
	public int getEnergyStored()
	{
		return energyStorage.getEnergyStored();
	}
	
	public int getMaxEnergyStored()
	{
		return energyStorage.getMaxEnergyStored();
	}
}
