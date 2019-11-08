package com.mr208.unwired.common.crafting;

import com.mr208.unwired.common.inventory.IFluidInventory;
import com.mr208.unwired.common.inventory.IMachineInventory;
import com.mr208.unwired.common.util.ingredient.FluidIngredient;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class RecipeInterfaces
{
	public interface IFluidRecipe<C extends IFluidInventory> extends IRecipe<C>
	{
		default NonNullList<FluidIngredient> getFluidIngredients() {return NonNullList.create();}
		default FluidStack getFluidResult() {return FluidStack.EMPTY;}
	}
	
	public interface IMachineRecipe<C extends IMachineInventory> extends IFluidRecipe<C>
	{
		int getEnergyCost();
		int getEnergyPerTick();
	}
}
