package com.mr208.unwired.common.content;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.inventory.ResequencerContainer;
import net.minecraft.inventory.container.ContainerType;
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
	
	@SubscribeEvent
	public static void onContainterRegistryEvent(final RegistryEvent.Register<ContainerType<?>> event)
	{
		IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
		
		registry.registerAll(
				new ContainerType<ResequencerContainer>(ResequencerContainer::new).setRegistryName(UnWIRED.MOD_ID, "resequencer")
		);
	}
}
