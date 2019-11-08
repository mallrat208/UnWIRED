package com.mr208.unwired.common.content;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.entity.GreyGooEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(UnWIRED.MOD_ID)
@EventBusSubscriber(bus = Bus.MOD)
public class ModEntities
{
	public static final EntityType<GreyGooEntity> grey_goo = EntityType.Builder.<GreyGooEntity>create(GreyGooEntity::new, EntityClassification.MONSTER)
			.setTrackingRange(80)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(true)
			.size(2f, 2f)
			.build(UnWIRED.MOD_ID+":grey_goo");
	
	@SubscribeEvent
	public static void onEntityRegistryEvent(final RegistryEvent.Register<EntityType<?>> event)
	{
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();
		
		registry.register(grey_goo.setRegistryName(UnWIRED.MOD_ID, "grey_goo"));
	}
}
