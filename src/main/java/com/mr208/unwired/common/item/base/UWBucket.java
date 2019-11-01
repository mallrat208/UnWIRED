package com.mr208.unwired.common.item.base;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.content.ModGroups;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class UWBucket extends BucketItem
{
	public UWBucket(String name,Supplier<? extends Fluid> supplier)
	{
		super(supplier, new Item.Properties().group(ModGroups.mainGroup).maxStackSize(1).containerItem(Items.BUCKET));
		setRegistryName(UnWIRED.MOD_ID, name + "_bucket");
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
	{
		return new FluidBucketWrapper(stack);
		
	}
}
