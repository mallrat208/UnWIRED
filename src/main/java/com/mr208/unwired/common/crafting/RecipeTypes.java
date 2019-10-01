package com.mr208.unwired.common.crafting;

import com.mr208.unwired.UnWIRED;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class RecipeTypes
{
	public static IRecipeType<ResequencerRecipe> RESEQUENCER = registerRecipeType("resequencer");
	
	private static <T extends IRecipe<?>> IRecipeType<T> registerRecipeType(final String key)
	{
		return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(UnWIRED.MOD_ID, key), new IRecipeType<T>()
		{
			@Override
			public String toString()
			{
				return key;
			}
		});
	}
}
