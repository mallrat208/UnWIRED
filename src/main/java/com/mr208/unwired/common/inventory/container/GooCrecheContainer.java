package com.mr208.unwired.common.inventory.container;

import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.content.ModContainers;
import com.mr208.unwired.common.inventory.UWSlot.Charge;
import com.mr208.unwired.common.inventory.UWSlot.Fluid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;

public class GooCrecheContainer extends AbstractContainerBase
{
	public GooCrecheContainer(int id, PlayerInventory playerInventory, BlockPos pos)
	{
		super(ModContainers.goo_creche,id, playerInventory, pos);
		
		tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
			addSlot(new Charge(this,itemHandler, 0, 10, 61));
			addSlot(new Fluid(this, itemHandler, 3, 150, 61));
		});
		
		addPlayerSlots(playerInv, 8, 84);
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerEntity)
	{
		return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, ModBlocks.goo_creche);
	}
}
