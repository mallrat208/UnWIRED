package com.mr208.unwired.common.item;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.block.FluidDrum;
import com.mr208.unwired.common.block.FluidDrum.Drum;
import com.mr208.unwired.common.content.ModGroups;
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

public class DrumItem extends UWDirectionalBlockItem
{
	private static HashMap<DyeColor, DrumItem> colorDrumMap = new HashMap<>();
	private DyeColor color;
	private Drum drum;
	
	public DrumItem(Block blockIn, Drum type, DyeColor colorIn)
	{
		super(blockIn, false);
		this.setRegistryName(UnWIRED.MOD_ID, blockIn.getRegistryName().getPath()+"_"+colorIn.getTranslationKey());
		this.drum = type;
		this.color = colorIn;
		colorDrumMap.put(colorIn, this);
	}
	
	public DyeColor getColor()
	{
		return color;
	}
	
	@Override
	public String getTranslationKey(ItemStack p_77667_1_)
	{
		return "item.unwired.drum_" + drum.getName();
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
			return this.getBlock().getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, useContext.getPlacementHorizontalFacing()).with(BlockStateProperties.WATERLOGGED, isWaterlogged).with(FluidDrum.COLOR, this.color);
		}
		
		return super.getStateForPlacement(useContext);
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> itemStacks)
	{
		if(group == ModGroups.mainGroup && color == DyeColor.LIGHT_GRAY)
		{
			itemStacks.add(new ItemStack(this));
		}
	}
	
	public static ItemStack getDrumStackForColor(DyeColor colorIn)
	{
		return new ItemStack(colorDrumMap.getOrDefault(colorIn, (DrumItem)ModItems.drum_polymer_light_gray));
	}
	
	public static Collection<DrumItem> getDrumItems()
	{
		return colorDrumMap.values();
	}
}
