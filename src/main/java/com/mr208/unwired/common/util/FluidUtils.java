package com.mr208.unwired.common.util;

import com.mr208.unwired.UnWIRED;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidUtils
{
	private FluidUtils() {}
	
	public static boolean isFluidContainerFull(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		
		return FluidUtil.getFluidHandler(stack).map(iFluidHandlerItem -> {
			int tanks = iFluidHandlerItem.getTanks();
			for(int i = 0; i < tanks; i++)
			{
				if(iFluidHandlerItem.getFluidInTank(i).isEmpty() || iFluidHandlerItem.getFluidInTank(i).getAmount() < iFluidHandlerItem.getTankCapacity(i))
					return false;
			}
			
			return true;
			
		}).orElse(false);
	}
	
	public static ItemStack fillFluidContainer(IFluidHandler handler, ItemStack containerIn, ItemStack containerOut, @Nullable PlayerEntity player)
	{
		if(containerIn==null || containerIn.isEmpty())
			return ItemStack.EMPTY;
		
		if(containerIn.hasTag() && containerIn.getTag().isEmpty())
			containerIn.setTag(null);
		
		FluidActionResult result = FluidUtil.tryFillContainer(containerIn, handler, Integer.MAX_VALUE, player, false);
		
		if(result.isSuccess())
		{
			final ItemStack fullStack = result.getResult();
			
			if((containerOut.isEmpty() || containerOut.isItemEqual(fullStack)))
			{
				if(!containerOut.isEmpty() && containerOut.getCount()+fullStack.getCount() > containerOut.getMaxStackSize())
					return ItemStack.EMPTY;
				
				result = FluidUtil.tryFillContainer(containerIn, handler, Integer.MAX_VALUE, player, true);
				if(result.isSuccess())
				{
					return result.getResult();
				}
			}
			
		}
		
		return ItemStack.EMPTY;
	}
	
	public static ItemStack drainFluidContainer(IFluidHandler handler, ItemStack containerIn, ItemStack containerOut, @Nullable PlayerEntity player)
	{
		if(containerIn==null||containerIn.isEmpty())
			return ItemStack.EMPTY;
		
		if(containerIn.hasTag()&&containerIn.getTag().isEmpty())
			containerIn.setTag(null);
		
		FluidActionResult result = tryEmptyContainer(containerIn, handler, Integer.MAX_VALUE, player, false);
		
		if(result.isSuccess())
		{
			ItemStack empty = result.getResult().getContainerItem();
			if(containerOut.isEmpty() || ItemStack.areItemsEqual(containerOut,empty))
			{
				if(!containerOut.isEmpty()&&containerOut.getCount()+empty.getCount() > containerOut.getMaxStackSize())
					return ItemStack.EMPTY;
				result = FluidUtil.tryEmptyContainer(containerIn, handler, Integer.MAX_VALUE, player, true);
				if(result.isSuccess())
				{
					return result.getResult();
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Nonnull
	public static FluidActionResult tryEmptyContainer(@Nonnull ItemStack container, IFluidHandler fluidDestination, int maxAmount, @Nullable PlayerEntity player, boolean doDrain) {
		ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1);
		return FluidUtil.getFluidHandler(containerCopy).map((containerFluidHandler) -> {
			FluidStack transfer = FluidUtil.tryFluidTransfer(fluidDestination, containerFluidHandler, maxAmount, false);
			if (transfer.isEmpty()) {
				return FluidActionResult.FAILURE;
			} else {
				if (doDrain && player != null) {
					SoundEvent soundevent = transfer.getFluid().getAttributes().getEmptySound(transfer);
					player.world.playSound(null, player.posX, player.posY + 0.5D, player.posZ, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				
				ItemStack resultContainer = containerFluidHandler.getContainer();
				return new FluidActionResult(resultContainer);
			}
		}).orElse(FluidActionResult.FAILURE);
	}
}
