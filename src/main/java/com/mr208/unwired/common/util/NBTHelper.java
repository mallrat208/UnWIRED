package com.mr208.unwired.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.NonNullList;

public class NBTHelper
{
	public static String NBT_ENERGY_KEY = "energy";
	
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
	
	public static int insertEnergyItem(ItemStack stack, int energy, int maxEnergy, boolean simulate)
	{
		int stored = getEnergyStoredItem(stack);
		int received = Math.min(energy, maxEnergy-stored);
		if(!simulate)
		{
			stored+= received;
			setInt(stack, NBT_ENERGY_KEY, stored);
		}
		
		return received;
	}
	
	public static int extractEnergyItem(ItemStack stack, int energy, boolean simulate)
	{
		int stored = getEnergyStoredItem(stack);
		int output = Math.min(energy, stored);
		if(!simulate)
		{
			stored -= output;
			setInt(stack, NBT_ENERGY_KEY, stored);
		}
		
		return output;
	}
	
	public static int getEnergyStoredItem(ItemStack stack)
	{
		return getInt(stack,NBT_ENERGY_KEY);
	}
	
	public static void setInt(ItemStack stack, String name, int value)
	{
		getTag(stack).putInt(name, value);
	}
	
	public static int getInt(ItemStack stack, String name)
	{
		if(!getTag(stack).contains(name))
			getTag(stack).putInt(name, 0);
		
		return getTag(stack).getInt(name);
	}
	
	public static CompoundNBT getTag(ItemStack stack)
	{
		return stack.getOrCreateTag();
	}
	
	
}
