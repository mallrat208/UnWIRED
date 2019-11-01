package com.mr208.unwired.common.inventory;

import com.mr208.unwired.common.util.EnergyUtils;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class UWSlot extends SlotItemHandler
{
	Container container;
	
	public UWSlot(Container container, IItemHandler itemHandler, int index, int xPos, int yPos)
	{
		super(itemHandler, index, xPos, yPos);
		this.container = container;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return true;
	}
	
	public static class Output extends UWSlot
	{
		public Output(Container container, IItemHandler itemHandler, int index, int xPos, int yPos)
		{
			super(container, itemHandler, index, xPos, yPos);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return false;
		}
	}
	
	public static class Food extends UWSlot
	{
		public Food(Container container, IItemHandler itemHandler, int index, int xPos, int yPos)
		{
			super(container, itemHandler, index, xPos, yPos);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return stack.isFood();
		}
	}
	
	public static class Charge extends UWSlot
	{
		public Charge(Container container, IItemHandler itemHandler, int index, int xPos, int yPos)
		{
			super(container, itemHandler, index, xPos, yPos);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack)
		{
			LazyOptional<IEnergyStorage> energyCap = EnergyUtils.getEnergyHandler(stack);
			return energyCap.isPresent();
		}
	}
	
	public static class Fluid extends UWSlot
	{
		public Fluid(Container container, IItemHandler itemHandler, int index, int xPos, int yPos)
		{
			super(container, itemHandler, index, xPos, yPos);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack)
		{
			LazyOptional<IFluidHandlerItem> fluidCap =FluidUtil.getFluidHandler(stack);
			return fluidCap.isPresent();
		}
	}
}
