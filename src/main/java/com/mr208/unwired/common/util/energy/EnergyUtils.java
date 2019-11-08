package com.mr208.unwired.common.util.energy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyUtils
{
	public static boolean isEnergyProvider(ICapabilityProvider provider)
	{
		return isEnergyProvider(provider, null);
	}
	
	public static boolean isEnergyProvider(ICapabilityProvider provider, @Nullable Direction side)
	{
		if(provider == null)
			return false;
		
		return provider.getCapability(CapabilityEnergy.ENERGY)
				.map(IEnergyStorage::canExtract)
				.orElse(false);
	}
	
	public static boolean isEnergyReceiver(ICapabilityProvider provider)
	{
		return isEnergyReceiver(provider, null);
	}
	
	public static boolean isEnergyReceiver(ICapabilityProvider provider, @Nullable Direction facing)
	{
		if(provider==null)
			return false;
		
		return provider.getCapability(CapabilityEnergy.ENERGY, facing).map(IEnergyStorage::canReceive).orElse(false);
	}
	
	public static int insertEnergy(ICapabilityProvider provider, int energy, boolean simulate)
	{
		return insertEnergy(provider, null, energy, simulate);
	}
	
	public static int insertEnergy(ICapabilityProvider provider, @Nullable Direction side, int energy, boolean simulate)
	{
		if(provider==null)
			return 0;
		
		return provider.getCapability(CapabilityEnergy.ENERGY,side)
				.map(iEnergyStorage -> iEnergyStorage.receiveEnergy(energy,simulate))
				.orElse(0);
	}
	
	public static int extractEnergy(ICapabilityProvider provider, int energy, boolean simulate)
	{
		return extractEnergy(provider, null, energy, simulate);
	}
	
	public static int extractEnergy(ICapabilityProvider provider, @Nullable Direction side, int energy, boolean simulate)
	{
		if(provider == null)
			return 0;
		
		return provider.getCapability(CapabilityEnergy.ENERGY, side)
				.map(iEnergyStorage -> iEnergyStorage.extractEnergy(energy, simulate))
				.orElse(0);
	}
	
	public static int getEnergyStored(ICapabilityProvider provider)
	{
		return getEnergyStored(provider, null);
	}
	
	public static int getEnergyStored(ICapabilityProvider provider, @Nullable Direction side)
	{
		if(provider == null)
			return 0;
		
		return provider.getCapability(CapabilityEnergy.ENERGY, side)
				.map(IEnergyStorage::getEnergyStored)
				.orElse(0);
	}
	
	public static int getMaxEnergyStored(ICapabilityProvider provider)
	{
		return getMaxEnergyStored(provider, null);
	}
	
	public static int getMaxEnergyStored(ICapabilityProvider provider, @Nullable Direction side)
	{
		if(provider == null)
			return 0;
			
		return provider.getCapability(CapabilityEnergy.ENERGY, side)
				.map(IEnergyStorage::getMaxEnergyStored)
				.orElse(0);
	}
	
	public static float getEnergyPercentage(ICapabilityProvider provider)
	{
		return getEnergyPercentage(provider, null);
	}
	
	public static float getEnergyPercentage(ICapabilityProvider provider, @Nullable Direction side)
	{
		if(provider == null)
			return 0;
		
		int max = getMaxEnergyStored(provider, side);
		
		if(max == 0)
			return 0;
		
		return (float)getEnergyStored(provider, side) / (float)getMaxEnergyStored(provider, side);
	}
	
	public static LazyOptional<IEnergyStorage> getEnergyHandler(@Nonnull ItemStack stack)
	{
		return stack.getCapability(CapabilityEnergy.ENERGY);
	}
	
	public static LazyOptional<IEnergyStorage> getEnergyHandler(World world, BlockPos pos, @Nullable Direction side)
	{
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if(block.hasTileEntity(state));
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile!= null)
				return tile.getCapability(CapabilityEnergy.ENERGY, side);
		}
		
		return LazyOptional.empty();
	}
}
