package com.mr208.unwired.common.item;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.Content;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import javax.annotation.Nullable;

public class SoybeanItem extends BlockNamedItem
{
	public Food foodstats;
	
	public SoybeanItem()
	{
		super(Content.soyCrop,new Item.Properties().group(Content.itemGroup));
		setRegistryName(UnWIRED.MOD_ID, "soybean");
		Content.registeredItems.add(this);
		
		foodstats = new Food.Builder().hunger(1).saturation(1).effect(new EffectInstance(Effects.NAUSEA, 30), 0.2f).build();
	}
	
	@Override
	public boolean isFood()
	{
		return true;
	}
	
	@Nullable
	@Override
	public Food getFood()
	{
		return foodstats;
	}
}
