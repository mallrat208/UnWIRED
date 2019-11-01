package com.mr208.unwired.common.inventory;

import com.mr208.unwired.client.screen.ContainerScreenBase;
import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.content.ModContainers;
import com.mr208.unwired.common.inventory.UWSlot.Charge;
import com.mr208.unwired.common.inventory.UWSlot.Fluid;
import com.mr208.unwired.common.inventory.UWSlot.Food;
import com.mr208.unwired.common.inventory.UWSlot.Output;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class FluidDrumContainer extends AbstractContainerBase
{
	public FluidDrumContainer(int id, PlayerInventory playerInventory)
	{
		super(ModContainers.fluid_drum, id);
	}
	
	public FluidDrumContainer(int id, PlayerInventory playerInventory, BlockPos pos)
	{
		this(id, playerInventory);
		
		this.tile = playerInventory.player.world.getTileEntity(pos);
		this.player = playerInventory.player;
		this.playerInv = new InvWrapper(playerInventory);
		
		tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
			addSlot(new Fluid(this,handler,0, 44,30));
			addSlot(new Output(this,handler,1, 44,60));
			addSlot(new Fluid(this,handler,2, 116,30));
			addSlot(new Output(this, handler, 3, 116, 60));
		});
		
		addPlayerSlots(playerInv, 8, 84);
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerEntity, int slotIndex)
	{
		ItemStack stack = ItemStack.EMPTY;
		
		Slot slot = this.inventorySlots.get(slotIndex);
		
		if(slot!=null && slot.getHasStack())
		{
			ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();
			
			if(slotIndex<=3)
			{
				if(!this.mergeItemStack(slotStack, 3, 39, true))
					return ItemStack.EMPTY;
				
				slot.onSlotChange(slotStack, stack);
			}
			else
			{
				if((FluidUtil.getFluidHandler(slotStack).isPresent() && FluidUtil.getFluidContained(slotStack).orElse(FluidStack.EMPTY) == FluidStack.EMPTY))
				{
					if(!this.mergeItemStack(slotStack, 2,3, false))
						return ItemStack.EMPTY;
				}
				else if((FluidUtil.getFluidHandler(slotStack).isPresent() && FluidUtil.getFluidContained(slotStack).orElse(FluidStack.EMPTY) != FluidStack.EMPTY))
				{
					if(!this.mergeItemStack(slotStack,0,1, false))
						return ItemStack.EMPTY;
				}
			}
			
			if(slotStack.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if(slotStack.getCount() == stack.getCount())
			{
				return ItemStack.EMPTY;
			}
			
			slot.onTake(playerEntity, slotStack);
		}
		
		
		return stack;
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerEntity)
	{
		return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()),playerEntity, ModBlocks.drum_polymer);
	}
}
