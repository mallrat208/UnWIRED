package com.mr208.unwired.common.crafting.recipes;

import com.mr208.unwired.common.crafting.RecipeSerializers;
import com.mr208.unwired.common.item.base.IColorableEquipment;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class DyeColorableRecipe extends SpecialRecipe
{
	public DyeColorableRecipe(ResourceLocation p_i48169_1_)
	{
		super(p_i48169_1_);
	}
	
	@Override
	public boolean matches(CraftingInventory craftingInventory, World world)
	{
		boolean dye = false;
		boolean colorableItem = false;
		
		for(int i = 0; i < craftingInventory.getSizeInventory(); i++)
		{
			ItemStack testStack = craftingInventory.getStackInSlot(i);
			
			if(!testStack.isEmpty())
			{
				if(testStack.getItem() instanceof IColorableEquipment&&((IColorableEquipment)testStack.getItem()).isDyable()&&!colorableItem)
				{
					colorableItem=true;
				} else if(DyeColor.getColor(testStack)!=null&&!dye)
				{
					dye=true;
				} else
				{
					return false;
				}
			}
		}
		
		return dye && colorableItem;
	}
	
	@Override
	public ItemStack getCraftingResult(CraftingInventory craftingInventory)
	{
		ItemStack stackToColor = ItemStack.EMPTY;
		ItemStack dyeStack = ItemStack.EMPTY;
		
		for(int i = 0; i < craftingInventory.getSizeInventory(); i++)
		{
			ItemStack testStack = craftingInventory.getStackInSlot(i);
			if(!testStack.isEmpty())
				if(testStack.getItem() instanceof IColorableEquipment && ((IColorableEquipment)testStack.getItem()).isDyable())
					stackToColor = testStack.copy();
				else if(DyeColor.getColor(testStack) != null)
					dyeStack = testStack.copy();
		}

		((IColorableEquipment)stackToColor.getItem()).setColor(stackToColor, DyeColor.getColor(dyeStack));
			
		return stackToColor;
		
	}
	
	@Override
	public boolean canFit(int i, int i1)
	{
		return i * i1 >= 2;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return RecipeSerializers.DYE_COLORABLE;
	}
}
