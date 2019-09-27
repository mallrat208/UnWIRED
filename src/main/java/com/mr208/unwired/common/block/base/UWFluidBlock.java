package com.mr208.unwired.common.block.base;

import com.mr208.unwired.UnWIRED;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;

import java.util.function.Supplier;

public class UWFluidBlock extends FlowingFluidBlock
{
	public UWFluidBlock(Supplier<? extends FlowingFluid> supplier,Properties props, String name)
	{
		super(supplier, props);
		
		setRegistryName(UnWIRED.MOD_ID, name+"_block");
	}
}
