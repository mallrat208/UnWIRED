package com.mr208.unwired.common.block.tile;

import com.mr208.unwired.common.content.ModTileEntities;
import com.mr208.unwired.common.util.FluxStorage;
import com.mr208.unwired.common.util.NBTHelper;
import com.mr208.unwired.common.util.UWInventory;
import com.mr208.unwired.common.util.UWInventoryHandler;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MetabolicGenTile extends UWEnergyTile implements UWInventory
{
	
	private static final int METABOLIC_INVENTORY_SLOTS = 3;
	
	
	public static final int SLOT_BATTERY = 0;
	public static final int SLOT_FOOD = 1;
	public static final int SLOT_CONTAINER = 2;
	
	private final int METABOLIC_ENERGY_PER_TIC = 40;
	
	
	private boolean processing;
	private float processingTime = 0;
	
	private NonNullList<ItemStack> inventory = NonNullList.withSize(METABOLIC_INVENTORY_SLOTS, ItemStack.EMPTY);
	private LazyOptional<IItemHandler> itemHandler = registerCapability(new UWInventoryHandler(inventory.size(), this, 0, new boolean[]{false,true}, new boolean[]{true,false}));
	
	public MetabolicGenTile()
	{
		super(ModTileEntities.generator_metabolic);
	}
	
	@Override
	public FluxStorage createEnergyStorage()
	{
		return new FluxStorage(40000, 80);
	}
	
	@Override
	public void tick()
	{
		if(processing)
		{
			processingTime--;
			if(processingTime <= 0)
				processing = false;
			
			receiveEnergy(METABOLIC_ENERGY_PER_TIC,false);
		}
		else
		{
			if(!inventory.get(SLOT_FOOD).isEmpty())
			{
				Food stackFood = inventory.get(SLOT_FOOD).getItem().getFood();
				
				float foodModifier = (stackFood.getHealing() * stackFood.getSaturation()) * 100;
				
				inventory.get(SLOT_FOOD).shrink(1);
				
				processing = true;
				processingTime = foodModifier;
				markDirty();
			}
			else if(getEnergyStored() == 0)
			{
				inventory.set(SLOT_FOOD, new ItemStack(Items.GOLDEN_CARROT, 1));
			}
		}
	}
	
	public boolean isProcessing()
	{
		return this.processingTime > 0;
	}
	
	@Override
	public boolean canExtract()
	{
		return true;
	}
	
	@Override
	public boolean canReceive()
	{
		return false;
	}
	
	@Nullable
	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}
	
	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		switch(slot)
		{
			case SLOT_BATTERY:
				return stack.getCapability(CapabilityEnergy.ENERGY) != null;
			case SLOT_FOOD:
				return stack.getItem().isFood();
			default:
				return false;
		}
	}
	
	@Override
	public int getSlotLimit(int slot)
	{
		return slot == 0 ? 1 : 64;
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return itemHandler.cast();
		}
		
		return super.getCapability(cap, side);
	}
	
	@Override
	public void readCustomNBT(CompoundNBT compound, boolean descPacket)
	{
		super.readCustomNBT(compound, descPacket);
		ItemStackHelper.saveAllItems(compound, inventory);
		this.processing = compound.getBoolean("Processing");
		this.processingTime = compound.getFloat("ProcessingTime");
	}
	
	@Override
	public void writeCustomNBT(CompoundNBT compound, boolean descPacket)
	{
		super.writeCustomNBT(compound, descPacket);
		ItemStackHelper.loadAllItems(compound, inventory);
		compound.putBoolean("Processing", this.processing);
		compound.putFloat("ProcessingTime", this.processingTime);
	}
}
