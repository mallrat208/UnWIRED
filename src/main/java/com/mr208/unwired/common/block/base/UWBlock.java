package com.mr208.unwired.common.block.base;

import com.mr208.unwired.UnWIRED;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class UWBlock extends Block
{
	public UWBlock(String name, Properties properties)
	{
		super(properties);
		setRegistryName(UnWIRED.MOD_ID, name);
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			TileEntity tile = worldIn.getTileEntity(pos);
			if(tile!=null && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent())
			{
				tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
					NonNullList<ItemStack> listDrops = NonNullList.create();
					for(int i = 0; i < itemHandler.getSlots(); i++)
						if(!itemHandler.getStackInSlot(0).isEmpty())
							listDrops.add(itemHandler.getStackInSlot(i));
					
					InventoryHelper.dropItems(worldIn, pos, listDrops);
				});
				worldIn.updateComparatorOutputLevel(pos,this);
			}
			
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
}
