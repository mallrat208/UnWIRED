package com.mr208.unwired.common.inventory;

import com.mr208.unwired.common.util.ingredient.FluidIngredient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class MachineInventory implements IMachineInventory
{
	private final LazyOptional<IEnergyStorage> energyHandler;
	private final LazyOptional<IItemHandler> itemHandler;
	private final LazyOptional<IFluidHandler> fluidHandler;
	
	
	public MachineInventory(LazyOptional<IEnergyStorage> energyHandler, LazyOptional<IItemHandler> itemHandler, LazyOptional<IFluidHandler> fluidHandler)
	{
		this.energyHandler = energyHandler;
		this.itemHandler = itemHandler;
		this.fluidHandler = fluidHandler;
		
	}
	
	@Override
	public int getCurrentEnergy()
	{
		return energyHandler.map(IEnergyStorage::getEnergyStored).orElse(0);
	}
	
	@Override
	public void setEnergy(int energy)
	{
	
	}
	
	@Override
	public void decreaseEnergy(int energy)
	{
		energyHandler.ifPresent(energyStorage -> energyStorage.extractEnergy(energy,false));
	}
	
	@Override
	public int getNumberTanks()
	{
		return fluidHandler.map(iFluidHandler -> iFluidHandler.getTanks()).orElse(0);
	}
	
	@Override
	public boolean areTanksEmpty()
	{
		
		return false;
	}
	
	@Override
	public FluidStack getFluidInTank(int tank)
	{
		return fluidHandler.map(iFluidHandler -> iFluidHandler.getFluidInTank(tank)).orElse(FluidStack.EMPTY);
	}
	
	@Override
	public FluidStack decrFluidInTank(int tank, int amount)
	{
		return fluidHandler.map(iFluidHandler -> iFluidHandler.drain(amount, FluidAction.EXECUTE)).orElse(FluidStack.EMPTY);
	}
	
	@Override
	public FluidStack removeFluidFromTank(int tank)
	{
		return fluidHandler.map(iFluidHandler -> iFluidHandler.drain(Integer.MAX_VALUE, FluidAction.EXECUTE)).orElse(FluidStack.EMPTY);
	}
	
	@Override
	public NonNullList<FluidIngredient> getFluidIngredients()
	{
		return null;
	}
	
	@Override
	public void setTankContents(int tank, FluidStack fluidStack)
	{
	
	}
	
	@Override
	public int getSizeInventory()
	{
		return itemHandler.map(itemHandler1 -> itemHandler1.getSlots()).orElse(0);
	}
	
	@Override
	public boolean isEmpty()
	{
		return false;
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return itemHandler.map(itemHandler1 -> itemHandler1.getStackInSlot(i)).orElse(ItemStack.EMPTY);
	}
	
	@Override
	public ItemStack decrStackSize(int i, int i1)
	{
		return itemHandler.map(itemHandler1 -> itemHandler1.extractItem(i, i1, true)).orElse(ItemStack.EMPTY);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int i)
	{
		return itemHandler.map(itemHandler1 -> itemHandler1.extractItem(i, Integer.MAX_VALUE, false)).orElse(ItemStack.EMPTY);
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack)
	{
		//itemHandler.ifPresent(itemHandler1 -> );
	}
	
	@Override
	public void markDirty()
	{
	
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity playerEntity)
	{
		return false;
	}
	
	@Override
	public void clear()
	{
	
	}
}
