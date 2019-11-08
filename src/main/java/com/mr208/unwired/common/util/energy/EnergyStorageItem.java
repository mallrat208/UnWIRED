package com.mr208.unwired.common.util.energy;

import com.mr208.unwired.common.util.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorageItem implements IEnergyStorage
{
	ItemStack stack;
	IFluxStorageItem fluxStorageItem;
	
	public EnergyStorageItem(ItemStack stack)
	{
		assert (stack.getItem() instanceof IFluxStorageItem);
		this.stack = stack;
		this.fluxStorageItem =(IFluxStorageItem)stack.getItem();
	}
	
	@Override
	public int receiveEnergy(int i, boolean b)
	{
		return this.fluxStorageItem.receive(stack,i,b);
	}
	
	@Override
	public int extractEnergy(int i, boolean b)
	{
		return this.fluxStorageItem.extract(stack,i,b);
	}
	
	@Override
	public int getEnergyStored()
	{
		return this.fluxStorageItem.getEnergy(stack);
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return this.fluxStorageItem.getMaxEnergy(stack);
	}
	
	@Override
	public boolean canReceive()
	{
		return true;
	}
	
	@Override
	public boolean canExtract()
	{
		return true;
	}
	
	public static class DisposableStorageItem extends EnergyStorageItem
	{
		public DisposableStorageItem(ItemStack stack)
		{
			super(stack);
		}
		
		@Override
		public boolean canReceive()
		{
			return false;
		}
	}
	
	public interface IFluxStorageItem
	{
		default int receive(ItemStack stack, int energy, boolean simulate)
		{
			return NBTHelper.insertEnergyItem(stack, energy, getMaxEnergy(stack),simulate);
		}
		
		default int extract(ItemStack stack, int energy, boolean simulate)
		{
			return NBTHelper.extractEnergyItem(stack, energy, simulate);
		}
		
		default int getEnergy(ItemStack stack)
		{
			return NBTHelper.getEnergyStoredItem(stack);
		}
		
		int getMaxEnergy(ItemStack stack);
	}
}
