package com.mr208.unwired.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

import java.util.function.Consumer;

public class NBTHelper
{
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
