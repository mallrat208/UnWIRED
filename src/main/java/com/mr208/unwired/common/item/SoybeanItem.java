package com.mr208.unwired.common.item;

import com.mr208.unwired.common.item.base.UWBlockNamed;
import com.mr208.unwired.common.content.ModGroups;
import com.mr208.unwired.common.content.ModBlocks;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import javax.annotation.Nullable;

public class SoybeanItem extends UWBlockNamed
{
	public Food foodstats;
	
	public SoybeanItem()
	{
		super("soybean", ModBlocks.soy_crop,new Item.Properties().group(ModGroups.mainGroup));
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
