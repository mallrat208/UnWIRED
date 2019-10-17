package com.mr208.unwired.common.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class UWInventoryHandler implements IItemHandlerModifiable
{
	int slots;
	UWInventory inventory;
	int slotOffset;
	boolean[] canInsert,canExtract;
	
	
	public UWInventoryHandler(int slots, UWInventory inventory, int slotOffset, boolean[] canInsert, boolean[] canExtract)
	{
		this.slots = slots;
		this.inventory = inventory;
		this.slotOffset = slotOffset;
		this.canInsert = canInsert;
		this.canExtract = canExtract;
	}
	
	public UWInventoryHandler(int slots, UWInventory inventory, int slotOffset, boolean canInsert, boolean canExtract)
	{
		this(slots, inventory, slotOffset, new boolean[slots], new boolean[slots]);
		for(int i = 0; i < slots; i++)
		{
			this.canInsert[i] = canInsert;
			this.canExtract[i] = canExtract;
		}
	}
	
	public UWInventoryHandler(int slots, UWInventory inventory)
	{
		this(slots, inventory, 0, new boolean[slots], new boolean[slots]);
		for(int i = 0; i < slots; i++)
			this.canExtract[i] = this.canInsert[i] = true;
	}
	
	@Override
	public void setStackInSlot(int i, @Nonnull ItemStack itemStack)
	{
		inventory.getInventory().set(this.slotOffset + i, itemStack);
	}
	
	@Override
	public int getSlots()
	{
		return slots;
	}
	
	@Nonnull
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return this.inventory.getInventory().get(this.slotOffset + i);
	}
	
	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack itemStack, boolean simulate)
	{
		if(!canInsert[slot]||itemStack.isEmpty())
			return itemStack;
		itemStack = itemStack.copy();
		
		if(!inventory.isStackValid(this.slotOffset + slot, itemStack))
			return itemStack;
		
		ItemStack existingStack = inventory.getInventory().get(this.slotOffset + slot);
		
		if(existingStack.isEmpty())
		{
			int quantityAccepted = Math.min(itemStack.getMaxStackSize(), inventory.getSlotLimit(this.slotOffset + slot));
			if(quantityAccepted < itemStack.getCount())
			{
				if(!simulate)
				{
					inventory.getInventory().set(this.slotOffset + slot, itemStack.split(quantityAccepted));
					return itemStack;
				}
				else
				{
					itemStack.shrink(quantityAccepted);
					return itemStack;
				}
			}
			else
			{
				if(!simulate)
				{
					inventory.getInventory().set(this.slotOffset + slot, itemStack);
					
				}
				
				return ItemStack.EMPTY;
			}
		}
		else
		{
			if(!ItemHandlerHelper.canItemStacksStack(itemStack, existingStack))
				return itemStack;
			
			int quantityAccepted = Math.min(itemStack.getMaxStackSize(), inventory.getSlotLimit(this.slotOffset + slot)) - existingStack.getCount();
			if(quantityAccepted < itemStack.getCount())
			{
				if(!simulate)
				{
					ItemStack newStack = itemStack.split(quantityAccepted);
					newStack.grow(existingStack.getCount());
					inventory.getInventory().set(this.slotOffset + slot, newStack);
					return itemStack;
				}
				else
				{
					itemStack.shrink(quantityAccepted);
					return itemStack;
				}
			}
			else
			{
				if(!simulate)
				{
					ItemStack newStack = itemStack.copy();
					newStack.grow(existingStack.getCount());
					inventory.getInventory().set(this.slotOffset + slot, newStack);
				}
				
				return ItemStack.EMPTY;
			}
		}
	}
	
	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if(!canExtract[slot]||amount==0)
			return ItemStack.EMPTY;
		
		ItemStack existingStack = inventory.getInventory().get(this.slotOffset + slot);
		
		if(existingStack.isEmpty())
			return ItemStack.EMPTY;
		
		int extracted = Math.min(existingStack.getCount(), amount);
		
		ItemStack copy = existingStack.copy();
		copy.setCount(extracted);
		
		if(!simulate)
		{
			if(extracted < existingStack.getCount())
				existingStack.shrink(extracted);
			else
				existingStack = ItemStack.EMPTY;
			inventory.getInventory().set(this.slotOffset + slot, existingStack);
		}
		
		return copy;
	}
	
	@Override
	public int getSlotLimit(int i)
	{
		return 64;
	}
	
	@Override
	public boolean isItemValid(int i, @Nonnull ItemStack itemStack)
	{
		return canInsert[i]&&inventory.isStackValid(i, itemStack);
	}
}
