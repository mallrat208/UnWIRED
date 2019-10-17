package com.mr208.unwired.common.item.base;

import com.mr208.unwired.common.Content;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class UWBlockItem extends BlockItem
{
	public UWBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
		this.setRegistryName(blockIn.getRegistryName());
	}
	
	public UWBlockItem(Block blockIn, boolean setRegistry)
	{
		super(blockIn, new Item.Properties().group(Content.itemGroup));
	}
	
	public UWBlockItem(Block blockIn)
	{
		this(blockIn, new Item.Properties().group(Content.itemGroup));
	}
}
