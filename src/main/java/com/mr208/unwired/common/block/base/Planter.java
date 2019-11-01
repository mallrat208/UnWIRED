package com.mr208.unwired.common.block.base;

import com.mr208.unwired.common.content.ModBlocks.Materials;
import net.minecraft.block.Block;

public class Planter extends UWBlock
{
	public Planter()
	{
		super("planter", Block.Properties.create(Materials.MACHINE));
	}
}
