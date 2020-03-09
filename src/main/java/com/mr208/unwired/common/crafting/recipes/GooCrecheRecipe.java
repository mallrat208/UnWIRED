package com.mr208.unwired.common.crafting.recipes;

import com.mr208.unwired.common.crafting.RecipeInterfaces.IMachineRecipe;
import com.mr208.unwired.common.crafting.RecipeSerializers;
import com.mr208.unwired.common.crafting.RecipeTypes;
import com.mr208.unwired.common.inventory.IFluidInventory;
import com.mr208.unwired.common.inventory.IMachineInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class GooCrecheRecipe implements IMachineRecipe<IMachineInventory>
{
	protected final int energyCost;
	protected final int energyPerTick;
	protected final Ingredient ingredient;
	protected final FluidStack result;
	private final IRecipeType<?> type;
	private final IRecipeSerializer<?> serializer;
	protected final ResourceLocation id;
	protected final String group;
	
	public GooCrecheRecipe(ResourceLocation id, String group, Ingredient ingredient, FluidStack result, int energyCost, int energyPerTick)
	{
		this.type = RecipeTypes.GOO_CRECHE;
		this.serializer = RecipeSerializers.GOO_CRECHE;
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.result = result;
		this.energyCost = energyCost;
		this.energyPerTick = energyPerTick;
	}
	
	@Override
	public int getEnergyCost()
	{
		return energyCost;
	}
	
	@Override
	public int getEnergyPerTick()
	{
		return energyPerTick;
	}
	
	@Override
	public boolean matches(IMachineInventory iMachineInventory, World world)
	{
		return false;
	}
	
	@Override
	public ItemStack getCraftingResult(IMachineInventory iMachineInventory)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canFit(int i, int i1)
	{
		return false;
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}
	
	@Override
	public ResourceLocation getId()
	{
		return null;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return null;
	}
	
	@Override
	public IRecipeType<?> getType()
	{
		return null;
	}
}
