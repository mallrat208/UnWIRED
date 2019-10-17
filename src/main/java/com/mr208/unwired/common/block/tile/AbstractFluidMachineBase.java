package com.mr208.unwired.common.block.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.IntStream;

public abstract class AbstractFluidMachineBase extends AbstractRedstoneMachineBase implements IFluidHandler
{
	protected final FluidTank[] fluidTanks;
	private final LazyOptional<IFluidHandler> lazyFluidHandler;
	
	protected AbstractFluidMachineBase(TileEntityType<?> typeIn)
	{
		super(typeIn);
		
		this.fluidTanks = createTanks();
		this.lazyFluidHandler = LazyOptional.of(() -> this);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		ListNBT tankList = compound.getList("Tanks", 10);
		for(int i = 0; i < fluidTanks.length && i < tankList.size(); i++)
		{
			INBT inbt = tankList.get(i);
			fluidTanks[i].setFluid(FluidStack.loadFluidStackFromNBT((CompoundNBT)inbt));
		}
		super.read(compound);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		ListNBT tankList = new ListNBT();
		for(FluidTank tank : fluidTanks)
			tankList.add(tank.writeToNBT(new CompoundNBT()));
		
		compound.put("Tanks", tankList);
		return super.write(compound);
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT compound = super.getUpdateTag();
		ListNBT tankList = new ListNBT();
		for(FluidTank tank : fluidTanks)
			tankList.add(tank.writeToNBT(new CompoundNBT()));
		
		compound.put("Tanks", tankList);
		return compound;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		super.onDataPacket(net, pkt);
		
		ListNBT tankList = pkt.getNbtCompound().getList("Tanks", 10);
		for(int i = 0; i < fluidTanks.length && i < tankList.size(); i++)
		{
			INBT inbt = tankList.get(i);
			fluidTanks[i].setFluid(FluidStack.loadFluidStackFromNBT((CompoundNBT)inbt));
		}
	}
	
	@Override
	public int getTanks()
	{
		return fluidTanks.length;
	}
	
	@Nonnull
	@Override
	public FluidStack getFluidInTank(int i)
	{
		if(i < 0 || i >= fluidTanks.length)
			return FluidStack.EMPTY;
		
		return fluidTanks[i].getFluid();
	}
	
	@Override
	public int getTankCapacity(int i)
	{
		if(i < 0 || i >= fluidTanks.length)
			return 0;
		
		return fluidTanks[i].getCapacity();
	}
	
	@Override
	public boolean isFluidValid(int i, @Nonnull FluidStack fluidStack)
	{
		if (i < 0 || i >= fluidTanks.length)
			return false;
		
		return fluidTanks[i].isFluidValid(fluidStack);
	}
	
	@Override
	public int fill(FluidStack fluidStack, FluidAction fluidAction)
	{
		for(int i = 0; i < fluidTanks.length; i++)
		{
			FluidStack existingFluid = fluidTanks[i].getFluid();
			
			if(isFluidValid(i, fluidStack) && (existingFluid.isEmpty() || fluidStack.isFluidEqual(existingFluid)));
				return fluidTanks[i].fill(fluidStack, fluidAction);
		}
		
		return 0;
	}
	
	@Nonnull
	@Override
	public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction)
	{
		if(!fluidStack.isEmpty())
			for(FluidTank tank : fluidTanks)
				if(fluidStack.isFluidEqual(tank.getFluid()))
					return tank.drain(fluidStack, fluidAction);
		
		return FluidStack.EMPTY;
	}
	
	@Nonnull
	@Override
	public FluidStack drain(int i, FluidAction fluidAction)
	{
		for (FluidTank tank : fluidTanks)
			if(tank.getFluidAmount() > 0)
				return tank.drain(i, fluidAction);
		
		return FluidStack.EMPTY;
	}
	
	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
	{
		if(cap ==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return lazyFluidHandler.cast();
		
		return super.getCapability(cap, side);
	}
	
	@Override
	public void remove()
	{
		super.remove();
		lazyFluidHandler.invalidate();
	}
	
	abstract int getTankCount();
	abstract int getDefaultTankCapacity();
	
	private FluidTank[] createTanks()
	{
		return IntStream.range(0, getTankCount()).mapToObj(k -> new FluidTank(getDefaultTankCapacity())).toArray(FluidTank[]::new);
	}
}
