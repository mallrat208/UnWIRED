package com.mr208.unwired.common.tile;

import com.mr208.unwired.common.util.UWEnergyStorage;
import com.mr208.unwired.network.NetworkHandler;
import com.mr208.unwired.network.packet.SyncEnergyPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class UWEnergyTile extends UWBaseTileEntity implements IEnergyStorage, ITickableTileEntity
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
	
	public void syncEnergy()
	{
		NetworkHandler.sendToTrackingPlayers(this, new SyncEnergyPacket(this.pos, getEnergyStored()));
	}
	
	public abstract boolean canExtract();
	
	
	public abstract boolean canReceive();
	
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
	
	@Override
	public int receiveEnergy(int energy, boolean simulate)
	{
		return energyStorage.receiveEnergy(energy, simulate);
	}
	
	@Override
	public int extractEnergy(int energy, boolean simulate)
	{
		return energyStorage.receiveEnergy(energy, simulate);
	}
	
	@Override
	public int getEnergyStored()
	{
		return energyStorage.getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return energyStorage.getMaxEnergyStored();
	}
	
	public void setEnergyStored(int energy)
	{
		energyStorage.setEnergy(energy);
	}
}
