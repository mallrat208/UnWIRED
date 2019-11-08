package com.mr208.unwired.common.compat;

import com.google.common.collect.Lists;
import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.crafting.recipes.PolymerizerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;

public class PolymerizerRecipeCategory implements IRecipeCategory<PolymerizerRecipe>
{
	private static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation("jei", "textures/gui/gui_vanilla.png");
	private static final ResourceLocation UID = new ResourceLocation(UnWIRED.MOD_ID,"polymerizer");
	private static final int WIDTH = 116, HEIGHT = 18;
	private final IDrawable background, icon;
	private final String localizedName;
	
	PolymerizerRecipeCategory(IGuiHelper guiHelper)
	{
		this.background = guiHelper.drawableBuilder(RECIPE_GUI_VANILLA, 49, 168,WIDTH,HEIGHT).addPadding(0,0,40,0).build();
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(Items.APPLE)); //TODO: ICON
		this.localizedName =I18n.format("container.unwired.polymerizer");
	}
	
	@Override
	public ResourceLocation getUid()
	{
		return UID;
	}
	
	@Override
	public Class<PolymerizerRecipe> getRecipeClass()
	{
		return PolymerizerRecipe.class;
	}
	
	@Override
	public String getTitle()
	{
		return this.localizedName;
	}
	
	@Override
	public IDrawable getBackground()
	{
		return this.background;
	}
	
	@Override
	public IDrawable getIcon()
	{
		return this.icon;
	}
	
	@Override
	public void setIngredients(PolymerizerRecipe polymerizerRecipe, IIngredients iIngredients)
	{
		List<FluidStack> modifiedFluidList = Lists.newArrayList();
		List<Ingredient> modifiedList = Lists.newArrayList();
		
		polymerizerRecipe.getFluidIngredients().forEach(fluidIngredient ->modifiedFluidList.addAll(Arrays.asList(fluidIngredient.getMatchingFluids())));
		
		polymerizerRecipe.getIngredients().forEach(ingredient -> {
			for(ItemStack stack:ingredient.getMatchingStacks())
			{
				modifiedList.add(Ingredient.fromStacks(stack));
			}
		});
		
		iIngredients.setInputIngredients(modifiedList);
		iIngredients.setInputs(VanillaTypes.FLUID, modifiedFluidList);
		iIngredients.setOutput(VanillaTypes.ITEM, polymerizerRecipe.getRecipeOutput());
	}
	
	@Override
	public void setRecipe(IRecipeLayout iRecipeLayout, PolymerizerRecipe polymerizerRecipe, IIngredients iIngredients)
	{
		IGuiItemStackGroup guiItemStackGroup = iRecipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStackGroup = iRecipeLayout.getFluidStacks();
		
		guiFluidStackGroup.init(0, true, 20, 0);
		guiItemStackGroup.init(0, true, 40, 0);
		guiItemStackGroup.init(1,false, 98, 0);
		guiFluidStackGroup.set(0, iIngredients.getInputs(VanillaTypes.FLUID).get(0));
		guiFluidStackGroup.addTooltipCallback((i, b, fluidStack, list)->list.add(fluidStack.getAmount() + " mB"));
		guiItemStackGroup.set(0, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
		guiItemStackGroup.set(1, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
	}
}
