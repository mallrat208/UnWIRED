package com.mr208.unwired.common.block.base;

import com.mr208.unwired.UnWIRED;
import net.minecraft.block.Block;

public class UWBlock extends Block
{
	public UWBlock(String name, Properties properties)
	{
		super(properties);
		setRegistryName(UnWIRED.MOD_ID, name);
	}
}
