package com.mr208.unwired.common.block;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.content.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SoyCrop extends CropsBlock
{
	public SoyCrop()
	{
		super(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.CROP));
		setRegistryName(UnWIRED.MOD_ID, "soy_crop");
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	protected IItemProvider getSeedsItem()
	{
		return ModItems.soybean;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state)
	{
		return new ItemStack(this.getSeedsItem());
	}
}
