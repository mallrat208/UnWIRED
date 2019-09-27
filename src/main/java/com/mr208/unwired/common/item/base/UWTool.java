package com.mr208.unwired.common.item.base;

import net.minecraft.block.Block;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ToolItem;

import java.util.Set;

public class UWTool extends ToolItem
{
	public UWTool(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Properties builder)
	{
		super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builder);
		
	}
}
