package com.mr208.unwired.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;

public interface UWInventory
{
	@Nullable
	NonNullList<ItemStack> getInventory();
	
	boolean isStackValid(int slot, ItemStack stack);
	int getSlotLimit(int slot);
	default NonNullList<ItemStack> getDroppedItems()
	{
		return getInventory();
	}
}
