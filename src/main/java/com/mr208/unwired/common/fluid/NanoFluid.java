package com.mr208.unwired.common.fluid;

import com.mr208.unwired.UnWIRED;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class NanoFluid extends ForgeFlowingFluid
{
	protected NanoFluid(String name)
	{
		super(FluidHelper.nano_fluid_props);
		setRegistryName(UnWIRED.MOD_ID,name);
	}
	
	public static class Source extends NanoFluid
	{
		public Source()
		{
			super("nano_fluid_source");
		}
		
		@Override
		public boolean isSource(IFluidState state)
		{
			return true;
		}
		
		@Override
		public int getLevel(IFluidState p_207192_1_)
		{
			return 8;
		}
	}
	
	public static class Flowing extends NanoFluid
	{
		public Flowing()
		{
			super("nano_fluid_flowing");
			this.setDefaultState(this.getStateContainer().getBaseState().with(LEVEL_1_8, 7));
		}
		
		protected void fillStateContainer(net.minecraft.state.StateContainer.Builder<Fluid, IFluidState> builder)
		{
			super.fillStateContainer(builder);
			builder.add(LEVEL_1_8);
		}
		
		@Override
		public boolean isSource(IFluidState state)
		{
			return false;
		}
		
		@Override
		public int getLevel(IFluidState state)
		{
			return state.get(LEVEL_1_8);
		}
	}
}
