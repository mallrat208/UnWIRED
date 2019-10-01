package com.mr208.unwired.common.item.base;

import com.mr208.unwired.common.block.Resequencer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DirectionalPlaceContext;

import javax.annotation.Nullable;

public class UWDirectionalBlockItem extends UWBlockItem
{
	public UWDirectionalBlockItem(Block blockIn)
	{
		super(blockIn);
	}
	
	public UWDirectionalBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Nullable
	@Override
	public BlockItemUseContext getBlockItemUseContext(BlockItemUseContext context)
	{
		return new DirectionalPlaceContext(context.getWorld(), context.getPos(), context.getPlacementHorizontalFacing(), context.getItem(), context.getNearestLookingDirection());
	}
	
	@Nullable
	@Override
	protected BlockState getStateForPlacement(BlockItemUseContext p_195945_1_)
	{
		if(this.getBlock().getDefaultState().has(Resequencer.DIRECTION))
		{
			return this.getBlock().getDefaultState().with(Resequencer.DIRECTION, p_195945_1_.getPlacementHorizontalFacing());
		}
		return super.getStateForPlacement(p_195945_1_);
	}
}
