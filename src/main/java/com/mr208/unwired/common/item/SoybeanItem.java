package com.mr208.unwired.common.item;

import com.mr208.unwired.common.item.base.UWBlockNamed;
import com.mr208.unwired.common.Content;
import com.mr208.unwired.common.Content.Blocks;
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
		super("soybean", Blocks.soy_crop,new Item.Properties().group(Content.itemGroup));
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
