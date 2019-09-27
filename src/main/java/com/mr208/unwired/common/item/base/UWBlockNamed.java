package com.mr208.unwired.common.item.base;

import com.mr208.unwired.UnWIRED;
import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;

public class UWBlockNamed extends BlockNamedItem
{
	public UWBlockNamed(String name, Block blockIn, Properties properties)
	{
		super(blockIn, properties);
		setRegistryName(UnWIRED.MOD_ID, name);
	}
}
