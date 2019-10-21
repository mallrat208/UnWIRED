package com.mr208.unwired.common.tile;

import com.mr208.unwired.common.content.ModTileEntities;
import com.mr208.unwired.common.inventory.MetabolicGenContainer;
import com.mr208.unwired.common.util.EnergyUtil;
import com.mr208.unwired.common.util.UWEnergyStorage;
import com.mr208.unwired.common.util.UWInventory;
import com.mr208.unwired.common.util.UWInventoryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MetabolicGenTile extends UWEnergyTile implements UWInventory, INamedContainerProvider
{
	
	private static final int METABOLIC_INVENTORY_SLOTS = 3;
	
	
	public static final int SLOT_BATTERY = 0;
	public static final int SLOT_FOOD = 1;
	public static final int SLOT_CONTAINER = 2;
	
	private final int METABOLIC_ENERGY_PER_TIC = 40;
	
	
	private boolean processing;
	private float processingTime = 0;
	
	NonNullList<ItemStack> inventory = NonNullList.withSize(METABOLIC_INVENTORY_SLOTS, ItemStack.EMPTY);
	LazyOptional<IItemHandler> itemHandler = registerCapability(new UWInventoryHandler(inventory.size(), this));
	
	public MetabolicGenTile()
	{
		super(ModTileEntities.generator_metabolic);
	}
	
	@Override
	public UWEnergyStorage createEnergyStorage()
	{
		return new UWEnergyStorage(40000, 80);
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
			if(getEnergyStored() != getMaxEnergyStored() && !inventory.get(SLOT_FOOD).isEmpty())
			{
				Food stackFood = inventory.get(SLOT_FOOD).getItem().getFood();
				
				float foodModifier = (stackFood.getHealing() * stackFood.getSaturation()) * 100;
				
				inventory.get(SLOT_FOOD).shrink(1);
				
				processing = true;
				processingTime = foodModifier;
				markDirty();
			}
		}
		
		if(!inventory.get(0).isEmpty())
		{
			if(!world.isRemote)
			{
				int stored = EnergyUtil.getEnergyStored(inventory.get(0));
				int max = EnergyUtil.getMaxEnergyStored(inventory.get(0));
				
				int space = max-stored;
				
				if(space> 0)
				{
					int energyPre = (10 * stored)/max;
					
					int insert = Math.min(80, space);
					int accepted = Math.min(EnergyUtil.extractEnergy(this,insert, true), EnergyUtil.insertEnergy(inventory.get(0), insert, true));
					
					if((accepted = EnergyUtil.extractEnergy(this, accepted, false)) > 0)
						stored += EnergyUtil.insertEnergy(inventory.get(0), accepted, false);
					
					int energyPost = (10*stored)/max;
					
					if(energyPost!=energyPre)
						this.markDirty();
				}
			}
		}
	}
	
	@Nullable
	@Override
	public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity)
	{
		return new MetabolicGenContainer(i, playerInventory, this.pos);
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent("container.unwired.metabolic_generator");
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
		return true;
	}
	
	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
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
