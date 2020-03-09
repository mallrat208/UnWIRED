package com.mr208.unwired.common.content;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.inventory.container.FluidDrumContainer;
import com.mr208.unwired.common.inventory.container.GooCrecheContainer;
import com.mr208.unwired.common.inventory.container.MetabolicGenContainer;
import com.mr208.unwired.common.inventory.container.ResequencerContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(UnWIRED.MOD_ID)
@EventBusSubscriber(bus = Bus.MOD)
public class ModContainers
{
	public static final ContainerType<ResequencerContainer> resequencer = null;
	public static final ContainerType<MetabolicGenContainer> metabolic_generator = null;
	public static final ContainerType<GooCrecheContainer> goo_creche = null;
	public static final ContainerType<FluidDrumContainer> fluid_drum = null;
	
	@SubscribeEvent
	public static void onContainterRegistryEvent(final RegistryEvent.Register<ContainerType<?>> event)
	{
		IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
		
		registry.registerAll(
				new ContainerType<>(ResequencerContainer::new).setRegistryName(UnWIRED.MOD_ID, "resequencer"),
				IForgeContainerType.create((windowID, inv, data) -> {
					BlockPos pos = data.readBlockPos();
					return new MetabolicGenContainer(windowID, inv, pos);
				}).setRegistryName(UnWIRED.MOD_ID, "metabolic_generator"),
				IForgeContainerType.create((windowID, inv, data) -> {
					BlockPos pos = data.readBlockPos();
					return new GooCrecheContainer(windowID, inv, pos);
				}).setRegistryName(UnWIRED.MOD_ID,"goo_creche"),
				IForgeContainerType.create((windowID, inv, data) -> {
					BlockPos pos = data.readBlockPos();
					return new FluidDrumContainer(windowID, inv, pos);
				}).setRegistryName(UnWIRED.MOD_ID, "fluid_drum")
		);
	}
}
