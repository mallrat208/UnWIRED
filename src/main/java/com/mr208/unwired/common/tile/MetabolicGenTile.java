package com.mr208.unwired.common.tile;

import com.mr208.unwired.Config;
import com.mr208.unwired.common.content.ModTileEntities;
import com.mr208.unwired.common.inventory.container.MetabolicGenContainer;
import com.mr208.unwired.common.util.energy.EnergyUtils;
import com.mr208.unwired.common.util.energy.UWEnergyStorage;
import com.mr208.unwired.common.util.UWInventory;
import com.mr208.unwired.common.util.UWInventoryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SoupItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
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
import java.util.ArrayList;
import java.util.List;

public class MetabolicGenTile extends UWEnergyTile implements UWInventory, INamedContainerProvider
{
	
	private static final int METABOLIC_INVENTORY_SLOTS = 3;
	
	
	public static final int SLOT_BATTERY = 0;
	public static final int SLOT_FOOD = 1;
	public static final int SLOT_CONTAINER = 2;
	
	private final int METABOLIC_ENERGY_PER_TIC = 40;
	
	
	private boolean processing;
	
	private List<Direction> directionCache = new ArrayList<>();
	private int cacheCounter = 0;
	
	NonNullList<ItemStack> inventory = NonNullList.withSize(METABOLIC_INVENTORY_SLOTS, ItemStack.EMPTY);
	LazyOptional<IItemHandler> itemHandler = registerCapability(new UWInventoryHandler(inventory.size(), this));
	
	public MetabolicGenTile()
	{
		super(ModTileEntities.generator_metabolic);
	}
	
	@Override
	public UWEnergyStorage createEnergyStorage()
	{
		return new UWEnergyStorage(Config.METABOLIC_GENERATOR_CAPACITY.get(), 80);
	}
	
	@Override
	public void tick()
	{
		if(!world.isRemote)
		{
			if(processing)
			{
				PROGRESS--;
				if(PROGRESS<=0)
					processing=false;
				
				if(!world.isRemote())
				{
					receiveEnergy(METABOLIC_ENERGY_PER_TIC, false);
				}
				
			} else
			{
				if(getEnergyStored()!=getMaxEnergyStored()&&!inventory.get(SLOT_FOOD).isEmpty()&&(!inventory.get(SLOT_FOOD).hasContainerItem()||inventory.get(SLOT_CONTAINER).isEmpty()||(inventory.get(SLOT_FOOD).getContainerItem().isEmpty())&&((inventory.get(SLOT_CONTAINER).getItem()==inventory.get(SLOT_FOOD).getContainerItem().getItem())||(inventory.get(SLOT_FOOD).getItem() instanceof SoupItem&&inventory.get(SLOT_CONTAINER).getItem()==Items.BOWL)&&inventory.get(SLOT_CONTAINER).getCount()<inventory.get(SLOT_CONTAINER).getMaxStackSize())))
				{
					Food stackFood=inventory.get(SLOT_FOOD).getItem().getFood();
					
					float foodModifier=(stackFood.getHealing()*stackFood.getSaturation())*100;
					
					if(!world.isRemote())
					{
						if(inventory.get(SLOT_FOOD).hasContainerItem()||inventory.get(SLOT_FOOD).getItem() instanceof SoupItem)
						{
							if(inventory.get(SLOT_CONTAINER).isEmpty())
							{
								if(inventory.get(SLOT_FOOD).getItem() instanceof SoupItem)
									inventory.set(SLOT_CONTAINER, new ItemStack(Items.BOWL));
								else
									inventory.set(SLOT_CONTAINER, inventory.get(SLOT_FOOD).getContainerItem().copy());
							} else
							{
								ItemStack temp=inventory.get(SLOT_CONTAINER).copy();
								temp.grow(1);
								inventory.set(SLOT_CONTAINER, temp);
							}
						}
						inventory.get(SLOT_FOOD).shrink(1);
						
						markDirty();
					}
					
					processing = true;
					setProgress((int)foodModifier);
					setProgessMax((int)foodModifier);
				}
			}
	
			if(!inventory.get(0).isEmpty())
			{
				if(!world.isRemote)
				{
					int stored=EnergyUtils.getEnergyStored(inventory.get(0));
					int max=EnergyUtils.getMaxEnergyStored(inventory.get(0));
					
					int space=max-stored;
					
					if(space>0)
					{
						int insert=Math.min(80, space);
						int accepted=Math.min(EnergyUtils.extractEnergy(this, insert, true), EnergyUtils.insertEnergy(inventory.get(0), insert, true));
						
						if((accepted=EnergyUtils.extractEnergy(this, accepted, false))>0)
							stored+=EnergyUtils.insertEnergy(inventory.get(0), accepted, false);
						
						this.markDirty();
					}
				}
				
			}
			
			if(directionCache.isEmpty()||cacheCounter>30)
			{
				directionCache.clear();
				
				for(Direction direction : Direction.values())
				{
					TileEntity tile=world.getTileEntity(pos.add(direction.getXOffset(), direction.getYOffset(), direction.getZOffset()));
					if(tile!=null&&!tile.isRemoved())
						if(EnergyUtils.isEnergyReceiver(tile))
							directionCache.add(direction);
				}
				
				cacheCounter=0;
			} else
			{
				cacheCounter++;
			}
			
			
			for(Direction direction : directionCache)
			{
				TileEntity tile=world.getTileEntity(this.pos.add(direction.getXOffset(), direction.getYOffset(), direction.getZOffset()));
				if(tile!=null&&!tile.isRemoved())
				{
					int insert=Math.min(80, getEnergyStored());
					int accepted=Math.min(EnergyUtils.extractEnergy(this, direction, insert, true), EnergyUtils.insertEnergy(tile, direction.getOpposite(), insert, true));
					
					if(accepted!=0)
					{
						EnergyUtils.extractEnergy(this, direction, accepted, false);
						EnergyUtils.insertEnergy(tile, direction.getOpposite(), accepted, false);
					}
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
		return getProgress() > 0;
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
				return EnergyUtils.isEnergyReceiver(stack);
			case SLOT_FOOD:
				return stack.isFood();
		}
		
		return false;
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
		ItemStackHelper.loadAllItems(compound, inventory);
		this.processing = compound.getBoolean("Processing");
	}
	
	@Override
	public void writeCustomNBT(CompoundNBT compound, boolean descPacket)
	{
		super.writeCustomNBT(compound, descPacket);
		ItemStackHelper.saveAllItems(compound, inventory);
		compound.putBoolean("Processing", this.processing);
	}
}
