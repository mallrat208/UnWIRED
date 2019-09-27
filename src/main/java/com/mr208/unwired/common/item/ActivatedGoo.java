package com.mr208.unwired.common.item;

import com.mr208.unwired.common.item.base.UWBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;


public class ActivatedGoo extends UWBase
{
	public ActivatedGoo()
	{
		super("active_goo");
	}
	
	@Override
	public Rarity getRarity(ItemStack stack)
	{
		return Rarity.UNCOMMON;
	}
}
