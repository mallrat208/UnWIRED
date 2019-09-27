package com.mr208.unwired.common.item.base;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.Content;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;

import java.util.function.Supplier;

public class UWBucket extends BucketItem
{
	public UWBucket(String name,Supplier<? extends Fluid> supplier)
	{
		super(supplier, new Item.Properties().group(Content.itemGroup).maxStackSize(1).containerItem(net.minecraft.item.Items.BUCKET));
		
		setRegistryName(UnWIRED.MOD_ID, name + "_bucket");
	}
}
