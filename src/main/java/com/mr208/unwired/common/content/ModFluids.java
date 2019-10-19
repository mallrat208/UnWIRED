package com.mr208.unwired.common.content;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.fluid.NanoFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(UnWIRED.MOD_ID)
@EventBusSubscriber(bus = Bus.MOD)
public class ModFluids
{
	public static final Fluid nano_fluid_source = null;
	public static final Fluid nano_fluid_flowing = null;
	
	@SubscribeEvent
	public static void onFluidRegistryEvent(final RegistryEvent.Register<Fluid> event)
	{
		IForgeRegistry<Fluid> registry = event.getRegistry();
		
		registry.registerAll(
				new NanoFluid.Source(),
				new NanoFluid.Flowing()
		);
	}
}
