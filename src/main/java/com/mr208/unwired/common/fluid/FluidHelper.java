package com.mr208.unwired.common.fluid;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.content.ModFluids;
import com.mr208.unwired.common.content.ModItems;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class FluidHelper
{
	public static FluidAttributes.Builder goo_slurry_attributes= FluidAttributes.builder(new ResourceLocation(UnWIRED.MOD_ID,"fluid/goo_slurry"),new ResourceLocation(UnWIRED.MOD_ID, "fluid/goo_slurry_flowing"))
			.density(2000)
			.luminosity(14)
			.sound(SoundEvents.ITEM_BUCKET_FILL_LAVA,SoundEvents.ITEM_BUCKET_EMPTY_LAVA)
			.temperature(300);
	
	public static ForgeFlowingFluid.Properties goo_slurry_props= new ForgeFlowingFluid.Properties(() -> ModFluids.goo_slurry, () -> ModFluids.goo_slurry_flowing, goo_slurry_attributes)
			.block(() -> (FlowingFluidBlock) ModBlocks.goo_slurry_block)
			.bucket(() -> ModItems.goo_slurry_bucket);
	
}
