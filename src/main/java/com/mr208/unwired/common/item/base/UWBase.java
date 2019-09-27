package com.mr208.unwired.common.item.base;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.Content;
import net.minecraft.item.Item;

public class UWBase extends Item
{
	public UWBase(String name, Properties properties)
	{
		super(properties);
		setRegistryName(UnWIRED.MOD_ID, name);
	}
	
	public UWBase(String name)
	{
		this(name, new Item.Properties().group(Content.itemGroup));
	}
}
