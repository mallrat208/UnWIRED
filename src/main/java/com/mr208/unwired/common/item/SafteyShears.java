package com.mr208.unwired.common.item;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.content.ModGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.tags.BlockTags;

public class SafteyShears extends ShearsItem
{
	public SafteyShears()
	{
		super(new Item.Properties().group(ModGroups.mainGroup).maxStackSize(1).maxDamage(255));
		this.setRegistryName(UnWIRED.MOD_ID, "saftey_shears");
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Block block = state.getBlock();
		if (block != Blocks.COBWEB && !state.isIn(BlockTags.LEAVES)) {
			return block.isIn(BlockTags.WOOL) ? 3.0F : super.getDestroySpeed(stack, state);
		} else {
			return 10.0F;
		}
	}
}
