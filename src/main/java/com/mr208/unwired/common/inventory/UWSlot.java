package com.mr208.unwired.common.inventory;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.util.EnergyUtil;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public abstract class UWSlot extends SlotItemHandler
{
	Container container;
	
	public UWSlot(Container container, IItemHandler itemHandler, int index, int xPos, int yPos)
	{
		super(itemHandler, index, xPos, yPos);
		this.container = container;
		
		if(EffectiveSide.get() == LogicalSide.CLIENT)
			this.setBackgroundName("unwired:gui/uw_slot");
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
			if(EffectiveSide.get() == LogicalSide.CLIENT)
				this.setBackgroundName("unwired:gui/food_slot");
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
			if(EffectiveSide.get() == LogicalSide.CLIENT)
				this.setBackgroundName("unwired:gui/charge_slot");
			
		}
		
		@Override
		public boolean isItemValid(ItemStack stack)
		{
			LazyOptional<IEnergyStorage> energyCap = EnergyUtil.getEnergyHandler(stack);
			return energyCap.isPresent();
		}
	}
}
