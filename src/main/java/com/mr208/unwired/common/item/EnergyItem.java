package com.mr208.unwired.common.item;

import com.mr208.unwired.Config;
import com.mr208.unwired.common.content.ModGroups;
import com.mr208.unwired.common.item.base.UWBase;
import com.mr208.unwired.common.util.EnergyStorageItem;
import com.mr208.unwired.common.util.EnergyStorageItem.IFluxStorageItem;
import com.mr208.unwired.common.util.NBTHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EnergyItem extends UWBase implements IFluxStorageItem
{
	private int ENERGY_CAPACITY;
	
	public EnergyItem(String name, int capacity)
	{
		super(name,new Item.Properties().group(ModGroups.mainGroup).maxStackSize(1));
		this.ENERGY_CAPACITY = capacity;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced)
	{
		tooltip.add(new TranslationTextComponent("tooltip.unwired.energy", getEnergy(stack), getMaxEnergy(stack)).setStyle(new Style().setColor(TextFormatting.GOLD)));
		super.addInformation(stack, world, tooltip, advanced);
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if(group == ModGroups.mainGroup)
		{
			ItemStack empty = new ItemStack(this);
			ItemStack full = empty.copy();
			CompoundNBT tag = new CompoundNBT();
			tag.putInt(NBTHelper.NBT_ENERGY_KEY, this.ENERGY_CAPACITY);
			full.setTag(tag);
			
			items.add(empty);
			items.add(full);
		}
	}
	
	@Override
	public int getMaxEnergy(ItemStack stack)
	{
		return this.ENERGY_CAPACITY;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean showDurabilityBar(ItemStack stack)
	{
		return Config.ENERGY_CELLS_USE_DURABILITY_BAR.get();
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1 - (double)getEnergy(stack)/(double)getMaxEnergy(stack);
	}
	
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
	{
		if(!stack.isEmpty())
		{
			return new ICapabilityProvider()
			{
				final LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(() ->new EnergyStorageItem(stack));
				
				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction)
				{
					return capability==CapabilityEnergy.ENERGY?energyStorage.cast():LazyOptional.empty();
				}
			};
		}
		else
			return super.initCapabilities(stack, nbt);
	}
}
