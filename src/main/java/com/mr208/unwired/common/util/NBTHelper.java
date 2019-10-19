package com.mr208.unwired.common.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.NonNullList;

import java.util.function.Consumer;

public class NBTHelper
{
	public static NonNullList<ItemStack> readInventory(CompoundNBT compound, int slots)
	{
		
		ListNBT invList = compound.getList("Inventory",10);
		
		
		NonNullList<ItemStack> inv = NonNullList.create();
		
		for(int i = 0; i < slots; i++)
		{
			inv.add(i, ItemStack.read((CompoundNBT)invList.get(i)));
		}
		
		return inv;
	}
	
	public static void writeInventory(CompoundNBT compound, NonNullList<ItemStack> inventory)
	{
		
		
		ListNBT list = new ListNBT();
		
		for(int i = 0; i < inventory.size(); i++)
		{
			list.add(i,inventory.get(i).write(new CompoundNBT()));
		}
		
		compound.put("Inventory",list);
	}
	
	public static void addUnlocalizedLoreTag(ItemStack stack, String... lore)
	{
		CompoundNBT stackTag = stack.getOrCreateTag();
		CompoundNBT displayTag = stackTag.getCompound("display");
		ListNBT listTag = displayTag.contains("Lore") ? displayTag.getList("Lore",10) : new ListNBT();
		
		for(String line:lore)
		{
			listTag.add(new StringNBT("{\"translate\":\"" + line + "\"}"));
		}
		
		displayTag.put("Lore", listTag);
		stackTag.put("display", displayTag);
		
		stack.setTag(stackTag);
	}
	
	public static void setUnlocalizedNameTag(ItemStack stack, String  name)
	{
		CompoundNBT stackTag = stack.getOrCreateTag();
		CompoundNBT displayTag = stackTag.getCompound("display");
		displayTag.putString("Name", "{\"translate\":\"" + name + "\"}");
		stackTag.put("display", displayTag);
		stack.setTag(stackTag);
	}
}
