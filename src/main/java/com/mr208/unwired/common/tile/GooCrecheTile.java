package com.mr208.unwired.common.tile;

import com.mr208.unwired.common.content.ModItems;
import com.mr208.unwired.common.content.ModTileEntities;
import com.mr208.unwired.common.inventory.GooCrecheContainer;
import com.mr208.unwired.common.util.energy.EnergyUtils;
import com.mr208.unwired.common.util.energy.UWEnergyStorage;
import com.mr208.unwired.common.util.UWInventory;
import com.mr208.unwired.common.util.UWInventoryHandler;
import com.mr208.unwired.libs.TagLib;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GooCrecheTile extends UWEnergyTile implements UWInventory, INamedContainerProvider
{
	
	private static final int CRECHE_INVENTORY_SLOTS = 4;
	
	public static final int SLOT_BATTERY = 0;
	public static final int SLOT_NUGGETS = 1;
	public static final int SLOT_GOO = 2;
	public static final int SLOT_FLUID = 3;
	
	private boolean PROCESSING;
	private int PROCESSING_TIME;
	private int PROCESSING_MAX;
	
	private int GOO_USES;
	
	FluidTank TANK = new FluidTank(16000,fluidStack -> fluidStack.getFluid().getTags().contains(TagLib.FLUID_GOO));
	
	NonNullList<ItemStack> INVENTORY = NonNullList.withSize(CRECHE_INVENTORY_SLOTS, ItemStack.EMPTY);
	LazyOptional<IItemHandler> INV_HANDLER = registerCapability(new UWInventoryHandler(INVENTORY.size(), this));
	LazyOptional<IFluidHandler> FLUID_HANDLER = registerCapability(TANK);
	
	public GooCrecheTile()
	{
		super(ModTileEntities.goo_creche);
	}
	
	@Override
	public UWEnergyStorage createEnergyStorage()
	{
		return new UWEnergyStorage(16000,80);
	}
	
	@Override
	public void tick()
	{
		if(!hasWorld() ||world.isRemote)
			return;
		
		if(!INVENTORY.get(SLOT_BATTERY).isEmpty())
		{
			int accepted = Math.min(EnergyUtils.extractEnergy(INVENTORY.get(SLOT_BATTERY), 80, true), EnergyUtils.insertEnergy(this, 80, true));
			
			if((accepted=EnergyUtils.extractEnergy(INVENTORY.get(SLOT_BATTERY), accepted, hasFastRenderer()))>0)
				EnergyUtils.insertEnergy(this, accepted, false);
			
			this.markDirty();
		}
	}
	
	@Nullable
	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return INVENTORY;
	}
	
	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		switch(slot)
		{
			case SLOT_BATTERY:
				return EnergyUtils.isEnergyProvider(stack);
			case SLOT_GOO:
				return stack.getItem() == ModItems.inert_goo;
			case SLOT_NUGGETS:
				return stack.getItem().getTags().contains(TagLib.ITEM_NUGGETS);
			case SLOT_FLUID:
				return stack.getItem() == Items.BUCKET || FluidUtil.getFluidHandler(stack).isPresent();
		}
		return false;
	}
	
	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent("container.unwired.goo_creche");
	}
	
	@Nullable
	@Override
	public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity)
	{
		return new GooCrecheContainer(i, playerInventory, this.pos);
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap  ==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return INV_HANDLER.cast();
		else if(cap ==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return FLUID_HANDLER.cast();
		else
			return super.getCapability(cap, side);
	}
	
	@Override
	public void readCustomNBT(CompoundNBT compound, boolean descPacket)
	{
		super.readCustomNBT(compound, descPacket);
		ItemStackHelper.loadAllItems(compound, INVENTORY);
		TANK.readFromNBT(compound);
		PROCESSING = compound.getBoolean("Processing");
		PROCESSING_TIME = compound.getInt("ProcessingTime");
		PROCESSING_MAX = compound.getInt("ProcessingMax");
	}
	
	@Override
	public void writeCustomNBT(CompoundNBT compound, boolean descPacket)
	{
		super.writeCustomNBT(compound, descPacket);
		ItemStackHelper.saveAllItems(compound, INVENTORY);
		TANK.writeToNBT(compound);
		compound.putBoolean("Processing", this.PROCESSING);
		compound.putInt("ProcessingTime", PROCESSING_TIME);
		compound.putInt("ProcessingMax", PROCESSING_MAX);
	}
}
