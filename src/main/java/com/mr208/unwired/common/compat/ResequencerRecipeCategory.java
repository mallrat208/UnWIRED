package com.mr208.unwired.common.compat;

import com.google.common.collect.Lists;
import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.Content;
import com.mr208.unwired.common.Content.Blocks;
import com.mr208.unwired.common.Content.Items;
import com.mr208.unwired.common.crafting.ResequencerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ResequencerRecipeCategory implements IRecipeCategory<ResequencerRecipe>
{
	private static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation("jei", "textures/gui/gui_vanilla.png");
	private static final ResourceLocation UID = new ResourceLocation(UnWIRED.MOD_ID,"resequencer");
	private static final int WIDTH = 116, HEIGHT = 18;
	private final IDrawable background, icon;
	private final String localizedName;
	
	ResequencerRecipeCategory(IGuiHelper guiHelper)
	{
		this.background = guiHelper.drawableBuilder(RECIPE_GUI_VANILLA, 49, 168, WIDTH, HEIGHT).addPadding(0,0,40,0).build();
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(Items.resequencer));
		this.localizedName = I18n.format("container.unwired.resequencer");
	}
	
	@Override
	public ResourceLocation getUid()
	{
		return UID;
	}
	
	@Override
	public Class<ResequencerRecipe> getRecipeClass()
	{
		return ResequencerRecipe.class;
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
	public void setIngredients(ResequencerRecipe resequencerRecipe, IIngredients iIngredients)
	{
		List<Ingredient> modifiedList = Lists.newArrayList();
		resequencerRecipe.getIngredients().forEach(ingredient -> {
			for(ItemStack stack:ingredient.getMatchingStacks())
			{
				modifiedList.add(Ingredient.fromStacks(new ItemStack(stack.getItem(),resequencerRecipe.getIngredientCount())));
			}
		});
		iIngredients.setInputIngredients(modifiedList);
		iIngredients.setOutput(VanillaTypes.ITEM, resequencerRecipe.getRecipeOutput());
	}
	
	@Override
	public void setRecipe(IRecipeLayout iRecipeLayout, ResequencerRecipe resequencerRecipe, IIngredients iIngredients)
	{
		IGuiItemStackGroup guiItemStackGroup = iRecipeLayout.getItemStacks();
		guiItemStackGroup.init(0, true, 40, 0);
		guiItemStackGroup.init(1, false, 98, 0);
		guiItemStackGroup.set(0, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
		guiItemStackGroup.set(1, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
	}
}
