package com.mr208.unwired.common.item;

import com.mr208.unwired.common.block.StorageCrate;
import com.mr208.unwired.common.block.StorageCrate.Crate;
import com.mr208.unwired.common.item.base.UWDirectionalBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;

public class CrateItem extends UWDirectionalBlockItem
{
	private DyeColor color = DyeColor.LIGHT_GRAY;
	private Crate crate;
	
	public CrateItem(Block blockIn, Crate crate)
	{
		super(blockIn);
		this.crate=crate;
	}
	
	@Override
	public String getTranslationKey(ItemStack p_77667_1_)
	{
		return "item.unwired.crate_" + crate.getName().toLowerCase();
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
			return this.getBlock().getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, useContext.getPlacementHorizontalFacing()).with(BlockStateProperties.WATERLOGGED, isWaterlogged).with(StorageCrate.COLOR, this.color);
		}
		return super.getStateForPlacement(useContext);
	}
	
	@Override
	public void fillItemGroup(ItemGroup p_150895_1_, NonNullList<ItemStack> p_150895_2_)
	{
		if(this.color == DyeColor.LIGHT_GRAY)
			super.fillItemGroup(p_150895_1_, p_150895_2_);
	}
}