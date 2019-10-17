package com.mr208.unwired.common.item.base;

import com.mr208.unwired.UnWIRED;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;

public class UWSpawnItem extends SpawnEggItem
{
	public UWSpawnItem(EntityType<?> typeIn, int primaryColorIn, int secondaryColorIn, String name)
	{
		super(typeIn, primaryColorIn, secondaryColorIn,new Item.Properties().group(ItemGroup.MISC));
		
		setRegistryName(UnWIRED.MOD_ID, name + "_spawn_egg");
	}
}
