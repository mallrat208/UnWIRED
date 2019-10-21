package com.mr208.unwired.common.inventory;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public abstract class AbstractContainerBase extends Container
{
	protected AbstractContainerBase(@Nullable ContainerType<?> type, int id)
	{
		super(type, id);
	}
	
	protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx)
	{
		for(int i = 0; i < amount; i++)
		{
			addSlot(new SlotItemHandler(handler, index,x,y));
			x += dx;
			index++;
		}
		return index;
	}
	
	protected int addSlotBox(IItemHandler handler, int index, int x, int y, int horizontalAmount, int dX, int verticalAmount, int dY)
	{
		for(int j = 0; j < verticalAmount; j++)
		{
			index = addSlotRange(handler, index, x, y, horizontalAmount, dX);
			y += dY;
		}
		
		return index;
	}
	
	protected void addPlayerSlots(IItemHandler playerInv, int left, int top)
	{
		addSlotBox(playerInv, 9, left, top, 9, 18, 3, 18);
		
		top += 58;
		addSlotRange(playerInv, 0, left, top, 9, 18);
	}
}
