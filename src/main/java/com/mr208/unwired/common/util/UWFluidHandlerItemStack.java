package com.mr208.unwired.common.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class UWFluidHandlerItemStack extends FluidHandlerItemStack
{
	public UWFluidHandlerItemStack(@Nonnull ItemStack container, int capacity)
	{
		super(container, capacity);
	}
	
	public int getCapacity()
	{
		if(container.getItem() instanceof ICustomizableFluidItem)
			return ((ICustomizableFluidItem)container.getItem()).getCapacity(container,capacity);
		
		return capacity;
	}
	
	@Override
	public boolean canFillFluidType(FluidStack fluid)
	{
		if(container.getItem() instanceof ICustomizableFluidItem)
			return ((ICustomizableFluidItem)container.getItem()).allowFluid(container,fluid);
		
		return true;
	}
	
	@Override
	public int fill(FluidStack resource, FluidAction doFill)
	{
		if(container.getCount()!=1||resource.isEmpty()||!canFillFluidType(resource))
			return 0;
		
		FluidStack current = getFluid();
		
		if(current.isEmpty())
		{
			int toFill = Math.min(getCapacity(), resource.getAmount());
			if(doFill.execute())
			{
				FluidStack modified = resource.copy();
				modified.setAmount(toFill);
				setFluid(modified);
			}
			
			return toFill;
		}
		else
		{
			if(current.isFluidEqual(resource))
			{
				int toFill = Math.min(getCapacity() - current.getAmount(), resource.getAmount());
				if(doFill.execute() && toFill > 0)
				{
					current.grow(toFill);
					setFluid(current);
				}
				
				return toFill;
			}
		}
		
		return 0;
	}
	
	public interface ICustomizableFluidItem
	{
		int getCapacity(ItemStack stack, int defaultCapacity);
		
		default boolean allowFluid(ItemStack fluidContainer, FluidStack resource)
		{
			return true;
		}
		
		default FluidStack getFluid(ItemStack fluidContainer)
		{
			return FluidUtil.getFluidContained(fluidContainer).orElse(FluidStack.EMPTY);
		}
	}
}
