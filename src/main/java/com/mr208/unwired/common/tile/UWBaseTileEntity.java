package com.mr208.unwired.common.tile;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.libs.FieldsLib;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public abstract class UWBaseTileEntity extends TileEntity
{
	public UWBaseTileEntity(TileEntityType<? extends TileEntity> type)
	{
		super(type);
	}
	
	private final Set<LazyOptional<?>> caps = new HashSet<>();
	
	public abstract void readCustomNBT(CompoundNBT compound, boolean descPacket);
	public abstract void writeCustomNBT(CompoundNBT compound, boolean descPacket);
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		this.readCustomNBT(compound, false);
		this.setProgress(compound.getInt("Progress"));
		this.setProgessMax(compound.getInt("ProgressMax"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		this.writeCustomNBT(compound, false);
		compound.putInt("Progress", getProgress());
		compound.putInt("ProgressMax", getProgressMax());
		return compound;
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 9, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		read(pkt.getNbtCompound());
	}
	
	public void syncFluid()
	{
	
	}
	
	public void setStoredFluid(CompoundNBT tankTag)
	{
	
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}
	
	protected <T> LazyOptional<T> registerCapability(T val)
	{
		return registerCapability(() -> val);
	}
	
	protected <T> LazyOptional<T> registerCapability(NonNullSupplier<T> cap)
	{
		return registerCapability(LazyOptional.of(cap));
	}
	protected <T> LazyOptional<T> registerCapability(LazyOptional<T> cap)
	{
		caps.add(cap);
		return cap;
	}
	
	@Override
	public void remove()
	{
		super.remove();
		for(LazyOptional<?> cap : caps)
			if(cap.isPresent())
				cap.invalidate();
	}
	
	
	private int energyU;
	private int energyL;
	private int energyMaxU;
	private int energyMaxL;
	
	protected final IIntArray FIELDS = new IIntArray()
	{
		@Override
		public int get(int index)
		{
			switch(index)
			{
				case FieldsLib.ENERGY_LOWER:
				{
					return (world!=null && world.isRemote) ? energyL : UWBaseTileEntity.this.getCapability(CapabilityEnergy.ENERGY).map(handler->handler.getEnergyStored()&0xFFFF).orElse(0);
				}
				case FieldsLib.ENERGY_UPPER:
				{
					return (world!=null && world.isRemote) ? energyU : UWBaseTileEntity.this.getCapability(CapabilityEnergy.ENERGY).map(handler->(handler.getEnergyStored() >> 16)&0xFFFF).orElse(0);
				}
				case FieldsLib.ENERGY_MAX_LOWER:
				{
					return (world!=null && world.isRemote) ? energyMaxL : UWBaseTileEntity.this.getCapability(CapabilityEnergy.ENERGY).map(handler->handler.getMaxEnergyStored()&0xFFFF).orElse(0);
				}
				case FieldsLib.ENERGY_MAX_UPPER:
				{
					return (world!=null && world.isRemote) ? energyMaxU : UWBaseTileEntity.this.getCapability(CapabilityEnergy.ENERGY).map(handler->(handler.getMaxEnergyStored() >> 16)&0xFFFF).orElse(0);
				}
				case FieldsLib.PROGRESS:
					return UWBaseTileEntity.this.getProgress();
				case FieldsLib.PROGRESS_MAX:
					return UWBaseTileEntity.this.getProgressMax();
			}
			
			return 0;
		}
		
		@Override
		public void set(int index, int value)
		{
			switch(index)
			{
				case FieldsLib.ENERGY_LOWER:
				{
					UWBaseTileEntity.this.energyL = value;
					break;
				}
				case FieldsLib.ENERGY_UPPER:
				{
					UWBaseTileEntity.this.energyU = value;
					break;
				}
				case FieldsLib.ENERGY_MAX_LOWER:
				{
					UWBaseTileEntity.this.energyMaxL = value;
					break;
				}
				case FieldsLib.ENERGY_MAX_UPPER:
				{
					UWBaseTileEntity.this.energyMaxU = value;
					break;
				}
				case FieldsLib.PROGRESS:
					UWBaseTileEntity.this.setProgress(value); break;
				case FieldsLib.PROGRESS_MAX:
					UWBaseTileEntity.this.setProgessMax(value);
			}
		}
		
		@Override
		public int size()
		{
			return FieldsLib.FIELD_COUNT;
		}
	};
	
	public IIntArray getFields()
	{
		return FIELDS;
	}
	
	protected int PROGRESS = 0;
	protected int PROGRESS_MAX = 0;
	
	public int getProgress()
	{
		return PROGRESS;
	}
	
	public void setProgress(int in)
	{
		PROGRESS = in;
	}
	
	public int getProgressMax()
	{
		return PROGRESS_MAX;
	}
	
	public int getProgressPercentage()
	{
		return (getProgressMax() - getProgress()/getProgressMax());
	}
	
	public void setProgessMax(int in)
	{
		PROGRESS_MAX = in;
	}
}
