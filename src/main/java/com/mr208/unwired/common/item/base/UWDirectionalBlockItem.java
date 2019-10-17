package com.mr208.unwired.common.item.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.state.properties.BlockStateProperties;

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
	
	public UWDirectionalBlockItem(Block blockIn, boolean setRegistry)
	{
		super(blockIn, setRegistry);
	}
	
	@Nullable
	@Override
	public BlockItemUseContext getBlockItemUseContext(BlockItemUseContext context)
	{
		return new DirectionalPlaceContext(context.getWorld(), context.getPos(), context.getPlacementHorizontalFacing(), context.getItem(), context.getNearestLookingDirection());
	}
	
	@Nullable
	@Override
	protected BlockState getStateForPlacement(BlockItemUseContext useContext)
	{
		if(this.getBlock().getDefaultState().has(BlockStateProperties.HORIZONTAL_FACING))
		{
			boolean isWaterlogged = useContext.getWorld().hasWater(useContext.getPos());
			return this.getBlock().getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, useContext.getPlacementHorizontalFacing()).with(BlockStateProperties.WATERLOGGED, isWaterlogged);
		}
		return super.getStateForPlacement(useContext);
	}
}
