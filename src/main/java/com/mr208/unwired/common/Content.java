package com.mr208.unwired.common;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.block.PlexiglassBlock;
import com.mr208.unwired.common.block.SmartglassBlock;
import com.mr208.unwired.common.block.SoyCrop;
import com.mr208.unwired.common.entity.GreyGooEntity;
import com.mr208.unwired.common.item.SoybeanItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.ArrayList;

public class Content
{
	public static ArrayList<Block> registeredBlocks = new ArrayList<>();
	public static ArrayList<Item> registeredItems = new ArrayList<>();
	
	public static ItemGroup itemGroup = new ItemGroup("unwired") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Content.plexiglass);
		}
	};
	
	public static PlexiglassBlock plexiglass = new PlexiglassBlock();
	public static SmartglassBlock smartglass = new SmartglassBlock();
	
	public static SoyCrop soyCrop = new SoyCrop();
	public static SoybeanItem soybeanItem = new SoybeanItem();
	
	public static EntityType<GreyGooEntity> GREYGOOD = EntityType.Builder.<GreyGooEntity>create(GreyGooEntity::new, EntityClassification.MONSTER)
			.setTrackingRange(80)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(true)
			.size(1f,1f)
			.build(UnWIRED.MOD_ID +":grey_goo");
	
	public static SpawnEggItem greygooEgg = new SpawnEggItem(GREYGOOD, 0x616161, 0x343434, new Item.Properties().group(itemGroup));
	
	static
	{
		registeredItems.add(greygooEgg);
		greygooEgg.setRegistryName(UnWIRED.MOD_ID,"grey_goo_spawn_egg");
	}
	
	@EventBusSubscriber(bus = Bus.MOD)
	public static class RegistryEvents
	{
		@SubscribeEvent
		public static void onBlockRegistryEvent(final RegistryEvent.Register<Block> event)
		{
			event.getRegistry().registerAll(registeredBlocks.toArray(new Block[registeredBlocks.size()]));
			
			registeredBlocks.clear();
		}
		
		@SubscribeEvent
		public static void onItemRegistryEvent(final RegistryEvent.Register<Item> event)
		{
			event.getRegistry().registerAll(registeredItems.toArray(new Item[registeredItems.size()]));
			
			registeredItems.clear();
		}
		
		@SubscribeEvent
		public static void onEntityRegistryEvent(final RegistryEvent.Register<EntityType<?>> event)
		{
			GREYGOOD.setRegistryName(UnWIRED.MOD_ID, "grey_goo");
			event.getRegistry().register(GREYGOOD);
		}
	
	}
	
}
