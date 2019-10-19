package com.mr208.unwired.common.item;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.content.ModGroups;
import com.mr208.unwired.common.block.StorageCrate;
import com.mr208.unwired.common.block.StorageCrate.Crate;
import com.mr208.unwired.common.content.ModItems;
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
import java.util.Collection;
import java.util.HashMap;

public class CrateItem extends UWDirectionalBlockItem
{
	private static HashMap<DyeColor, CrateItem> colorCrateMap = new HashMap<>();
	private DyeColor color;
	private Crate crate;
	
	public CrateItem(Block blockIn, Crate crate, DyeColor colorIn)
	{
		super(blockIn,false);
		this.setRegistryName(UnWIRED.MOD_ID,  blockIn.getRegistryName().getPath()+"_" + colorIn.getTranslationKey());
		this.crate = crate;
		color = colorIn;
		colorCrateMap.put(colorIn,this);
	}
	
	public DyeColor getColor()
	{
		return color;
	}
	
	@Override
	public String getTranslationKey()
	{
		return super.getTranslationKey();
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
		if(p_150895_1_ ==ModGroups.mainGroup&& color == DyeColor.LIGHT_GRAY)
		{
			p_150895_2_.add(new ItemStack(this));
		}
	}
	
	public static ItemStack getCrateStackForColor(DyeColor colorIn)
	{
		return new ItemStack(colorCrateMap.getOrDefault(colorIn, (CrateItem)ModItems.crate_polymer_light_gray));
	}
	
	public static Collection<CrateItem> getCrateItems()
	{
		return colorCrateMap.values();
	}
}