package com.mr208.unwired.common.inventory;

import com.mr208.unwired.common.inventory.UWSlot.Charge;
import com.mr208.unwired.common.inventory.UWSlot.Food;
import com.mr208.unwired.common.inventory.UWSlot.Output;
import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.content.ModContainers;
import com.mr208.unwired.common.util.EnergyUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class MetabolicGenContainer extends AbstractContainerBase
{
	private PlayerEntity player;
	private IItemHandler playerInv;
	
	public MetabolicGenContainer(int id, PlayerInventory playerInventory)
	{
		super(ModContainers.metabolic_generator,id);
	}
	
	public MetabolicGenContainer(int id, PlayerInventory playerInventory, BlockPos pos)
	{
		super(ModContainers.metabolic_generator,id);
		
		this.tile = playerInventory.player.world.getTileEntity(pos);
		this.player = playerInventory.player;
		this.playerInv = new InvWrapper(playerInventory);
		
		tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
			addSlot(new Charge(this,handler,0, 10,61));
			addSlot(new Food(this,handler,1, 82,36));
			addSlot(new Output(this,handler,2, 82,58));
		});
		
		addPlayerSlots(playerInv, 8, 84);
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
	{
		ItemStack stack = ItemStack.EMPTY;
		
		Slot slot = this.inventorySlots.get(index);
		if(slot !=null && slot.getHasStack())
		{
			ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();
			if(index<=2)
			{
				if(!this.mergeItemStack(slotStack, 3, 39, true))
					return ItemStack.EMPTY;
					
				slot.onSlotChange(slotStack, stack);
			}
			else
			{
				if(slotStack.isFood())
				{
					if(!this.mergeItemStack(slotStack, 1,2, false))
						return ItemStack.EMPTY;
				}
				else if(EnergyUtil.isEnergyReceiver(slotStack))
				{
					if(!this.mergeItemStack(slotStack,0,1, false))
						return ItemStack.EMPTY;
				}
				else if(index >= 3 && index <30)
				{
					if(!this.mergeItemStack(slotStack, 30, 39, false))
						return ItemStack.EMPTY;
				}
				else if(index >= 30 && index <39 && !this.mergeItemStack(slotStack, 3, 30, false))
					return ItemStack.EMPTY;
			}
			
			if(slotStack.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			} else
			{
				slot.onSlotChanged();
			}
			
			if(slotStack.getCount() == stack.getCount())
			{
				return ItemStack.EMPTY;
			}
			
			slot.onTake(playerIn, slotStack);
		}
		
		
		return stack;
	}
	
	
	@Override
	public boolean canInteractWith(PlayerEntity playerEntity)
	{
		return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()),playerEntity, ModBlocks.generator_metabolic);
	}
}
