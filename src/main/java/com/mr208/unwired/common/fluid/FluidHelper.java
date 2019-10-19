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
	public static FluidAttributes.Builder nano_fluid_attributes = FluidAttributes.builder(new ResourceLocation(UnWIRED.MOD_ID,"fluid/nano_fluid_source"),new ResourceLocation(UnWIRED.MOD_ID, "fluid/nano_fluid_flowing"))
			.color(0xFFDFDFDF)
			.density(2000)
			.luminosity(14)
			.overlay(new ResourceLocation("minecraft","block/water_overlay"))
			.sound(SoundEvents.ITEM_BUCKET_FILL_LAVA,SoundEvents.ITEM_BUCKET_EMPTY_LAVA)
			.temperature(300);
	
	public static ForgeFlowingFluid.Properties nano_fluid_props = new ForgeFlowingFluid.Properties(() -> ModFluids.nano_fluid_source, () -> ModFluids.nano_fluid_flowing, nano_fluid_attributes)
			.block(() -> (FlowingFluidBlock) ModBlocks.nano_fluid_block)
			.bucket(() -> ModItems.nano_fluid_bucket);
	
}
