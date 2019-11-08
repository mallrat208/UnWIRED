package com.mr208.unwired.common.compat;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.content.ModItems;
import com.mr208.unwired.common.inventory.IFluidInventory;
import com.mr208.unwired.common.crafting.RecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;

@JeiPlugin
public class JEICompat implements IModPlugin
{
	static ResourceLocation POLYMERIZER_UID = new ResourceLocation(UnWIRED.MOD_ID,"polymerizer");
	static ResourceLocation RESEQUENCER_UID = new ResourceLocation(UnWIRED.MOD_ID,"resequencer");
	
	@Override
	public ResourceLocation getPluginUid()
	{
		return new ResourceLocation(UnWIRED.MOD_ID, "resequencer");
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		registration.addRecipeCategories(new ResequencerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new PolymerizerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		Collection<IRecipe<IInventory>> recipes = Minecraft.getInstance().world.getRecipeManager().getRecipes(RecipeTypes.RESEQUENCER).values();
		registration.addRecipes(recipes, getPluginUid());
		
		Collection<IRecipe<IFluidInventory>> recipes1 = Minecraft.getInstance().world.getRecipeManager().getRecipes(RecipeTypes.POLYMERIZER).values();
		registration.addRecipes(recipes1, POLYMERIZER_UID);
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		registration.addRecipeCatalyst(new ItemStack(ModItems.resequencer), getPluginUid());
		registration.addRecipeCatalyst(new ItemStack(Items.APPLE), POLYMERIZER_UID);
	}
}
