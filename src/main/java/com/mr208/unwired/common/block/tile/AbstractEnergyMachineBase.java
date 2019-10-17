package com.mr208.unwired.common.block.tile;

import com.mr208.unwired.common.util.FluxStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public abstract class AbstractEnergyMachineBase extends AbstractFluidMachineBase implements IEnergyStorage
{
	protected FluxStorage energyStorage;
	private LazyOptional<IEnergyStorage> energyStorageHandler;
	
	protected AbstractEnergyMachineBase(TileEntityType<?> typeIn)
	{
		super(typeIn);
		
		this.energyStorage = createEnergyStorage();
		this.energyStorageHandler = LazyOptional.of(() -> this);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		this.energyStorage.setEnergy(compound.getInt("Energy"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putInt("Energy", getEnergyStored());
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT compound = super.getUpdateTag();
		compound.putInt("Energy", getEnergyStored());
		return compound;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		super.onDataPacket(net, pkt);
		this.energyStorage.setEnergy(pkt.getNbtCompound().getInt("Energy"));
	}
	
	@Override
	public int receiveEnergy(int i, boolean b)
	{
		return this.energyStorage.receiveEnergy(i, b);
	}
	
	@Override
	public int extractEnergy(int i, boolean b)
	{
		return this.extractEnergy(i, b);
	}
	
	@Override
	public int getEnergyStored()
	{
		return this.energyStorage.getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return this.energyStorage.getMaxEnergyStored();
		
	}
	
	@Override
	public boolean canExtract()
	{
		return this.energyStorage.canExtract();
	}
	
	@Override
	public boolean canReceive()
	{
		return this.energyStorage.canReceive();
	}
	
	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
	{
		if(cap ==CapabilityEnergy.ENERGY)
			return energyStorageHandler.cast();
		
		return super.getCapability(cap, side);
	}
	
	@Override
	public void remove()
	{
		super.remove();
		energyStorageHandler.invalidate();
	}
	
	protected abstract FluxStorage createEnergyStorage();
}
