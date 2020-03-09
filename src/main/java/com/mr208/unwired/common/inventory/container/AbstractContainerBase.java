package com.mr208.unwired.common.inventory.container;

import com.mr208.unwired.common.tile.UWBaseTileEntity;
import com.mr208.unwired.libs.FieldsLib;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractContainerBase<T extends UWBaseTileEntity> extends Container
{
	public UWBaseTileEntity tile;
	
	protected PlayerEntity player;
	protected IItemHandler playerInv;

	protected final IIntArray fields;
	
	protected AbstractContainerBase(ContainerType<?> type, int id, PlayerInventory playerInventory, BlockPos pos)
	{
		super(type, id);
		
		playerInv = new InvWrapper(playerInventory);
		player = playerInventory.player;
		tile = getBaseTile(pos);
		
		fields = tile.getFields();
		
		trackIntArray(fields);
	}
	
	
	
	public int getEnergyStored()
	{
		int upper = fields.get(FieldsLib.ENERGY_UPPER) & 0xFFFF;
		int lower = fields.get(FieldsLib.ENERGY_LOWER) & 0xFFFF;
		return (upper << 16) + lower;
	}
	
	public int getMaxEnergyStored()
	{
		int upper = fields.get(FieldsLib.ENERGY_MAX_UPPER) & 0xFFFF;
		int lower = fields.get(FieldsLib.ENERGY_MAX_LOWER) & 0xFFFF;
		
		return (upper << 16) + lower;
	}
	
	public int getProgress()
	{
		return fields.get(FieldsLib.PROGRESS);
	}
	
	public int getMaxProgress()
	{
		return fields.get(FieldsLib.PROGRESS_MAX);
	}
	
	public FluidStack getFluidTankStack(int tank)
	{
		return getTileCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).map(iFluidHandler -> iFluidHandler.getFluidInTank(tank)).orElse(FluidStack.EMPTY);
	}
	
	public int getFluidTankCapacity(int tank)
	{
		return getTileCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).map(iFluidHandler -> iFluidHandler.getTankCapacity(tank)).orElse(0);
	}
	
	public float getEnergyPercentage()
	{
		return (float) getEnergyStored() / (float) getMaxEnergyStored();
	}
	
	public float getProgressPercentage()
	{
		return (float)getProgress() / (float)getMaxProgress();
	}
	
	public <C> LazyOptional<C> getTileCapability(@Nonnull Capability<C> cap, @Nullable Direction side)
	{
		return tile.getCapability(cap,side);
	}
	
	public <C> LazyOptional<C> getTileCapability(@Nonnull Capability<C> cap)
	{
		return getTileCapability(cap, null);
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
	
	protected UWBaseTileEntity getBaseTile(BlockPos pos)
	{
		return (UWBaseTileEntity)player.getEntityWorld().getTileEntity(pos);
	}
}
