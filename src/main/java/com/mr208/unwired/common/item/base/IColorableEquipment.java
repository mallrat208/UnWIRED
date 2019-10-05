package com.mr208.unwired.common.item.base;

import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.awt.*;

public interface IColorableEquipment
{
	String COLORTAG = "UW_COLOR_OVERLAY";
	
	default boolean isDyable()
	{
		return false;
	}
	
	default boolean hasColor(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().contains(COLORTAG);
	}
	
	default int getDefaultColor()
	{
		return 65518;
	}
	
	default int getColorInt(ItemStack stack)
	{
		if(!stack.isEmpty())
		{
			if(stack.hasTag() && stack.getTag().contains(COLORTAG))
			{
				return stack.getTag().getInt(COLORTAG);
			}
		}
		
		return getDefaultColor();
	}
	
	default float[] getColorFloat(ItemStack stack)
	{
		int color = getColorInt(stack);
		
		return new Color(color).getRGBColorComponents(null);
	}
	default void setColor(ItemStack stack, int color)
	{
		CompoundNBT compoundNBT = stack.getOrCreateTag();
		
		compoundNBT.putInt(COLORTAG, color);
		stack.setTag(compoundNBT);
	}
	default void setColor(ItemStack stack, float red, float green, float blue)
	{
		setColor(stack, new Color(red,green,blue).getRGB());
	}
	
	default void setColor(ItemStack stack, DyeColor color)
	{
		setColor(stack, color.getFireworkColor());
	}
	
}
