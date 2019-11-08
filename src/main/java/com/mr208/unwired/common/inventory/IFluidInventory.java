package com.mr208.unwired.common.inventory;

import com.mr208.unwired.common.util.ingredient.FluidIngredient;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidInventory extends IInventory
{
	int getNumberTanks();
	
	boolean areTanksEmpty();
	
	FluidStack getFluidInTank(int tank);
	
	FluidStack decrFluidInTank(int tank, int amount);
	
	FluidStack removeFluidFromTank(int tank);
	
	NonNullList<FluidIngredient> getFluidIngredients();
	
	void setTankContents(int tank, FluidStack fluidStack);
	
	default boolean isFluidValidForTank(int tank, FluidStack fluidStack)
	{
		return true;
	}
}
