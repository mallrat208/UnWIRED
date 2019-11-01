package com.mr208.unwired.common.inventory;

import com.mr208.unwired.common.tile.UWEnergyTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractContainerBase<T extends TileEntity> extends Container
{
	public T tile;
	protected PlayerEntity player;
	protected IItemHandler playerInv;
	
	protected AbstractContainerBase(@Nullable ContainerType<?> type, int id)
	{
		super(type, id);
		if(tile instanceof UWEnergyTile && !tile.getWorld().isRemote)
		{
			((UWEnergyTile)tile).syncEnergy();
		}
	}
	
	public FluidStack getFluidTankStack(int tank)
	{
		return getTileCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).map(iFluidHandler -> iFluidHandler.getFluidInTank(tank)).orElse(FluidStack.EMPTY);
	}
	
	public int getFluidTankCapacity(int tank)
	{
		return getTileCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).map(iFluidHandler -> iFluidHandler.getTankCapacity(tank)).orElse(0);
	}
	
	public int getEnergyStored()
	{
		return getTileCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
	}
	
	public int getMaxEnergy()
	{
		return getTileCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0);
	}
	
	public float getEnergyPercentage()
	{
		return getTileCapability(CapabilityEnergy.ENERGY).map(energy ->((float)energy.getEnergyStored() / (float)energy.getMaxEnergyStored())).orElse(0f);
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
}
