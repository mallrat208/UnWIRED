package com.mr208.unwired.common;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.block.PlexiglassBlock;
import com.mr208.unwired.common.block.Resequencer;
import com.mr208.unwired.common.block.SmartglassBlock;
import com.mr208.unwired.common.block.SoyCrop;
import com.mr208.unwired.common.block.base.UWBlock;
import com.mr208.unwired.common.block.base.UWFluidBlock;
import com.mr208.unwired.common.entity.GreyGooEntity;
import com.mr208.unwired.common.fluid.NanoFluid;
import com.mr208.unwired.common.inventory.ResequencerContainer;
import com.mr208.unwired.common.item.ActivatedGoo;
import com.mr208.unwired.common.item.SoybeanItem;
import com.mr208.unwired.common.item.base.UWBase;
import com.mr208.unwired.common.item.base.UWBlockItem;
import com.mr208.unwired.common.item.base.UWBucket;
import com.mr208.unwired.common.item.base.UWDirectionalBlockItem;
import com.mr208.unwired.common.item.base.UWSpawnItem;
import com.mr208.unwired.common.item.equipment.FlippersBoot;
import com.mr208.unwired.common.item.equipment.RebreatherHelm;
import com.mr208.unwired.common.item.equipment.VisorHelm;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@EventBusSubscriber(bus = Bus.MOD)
public class Content
{
	@ObjectHolder(UnWIRED.MOD_ID)
	public static class Items
	{
		public static final Item soybean = null;
		public static final Item dust_polymer = null;
		public static final Item ingot_polymer = null;
		public static final Item block_polymer = null;
		public static final Item plate_polymer = null;
		public static final Item inert_goo = null;
		public static final Item active_goo = null;
		public static final Item nano_fluid_bucket = null;
		public static final Item grey_goo_spawn_egg = null;
		public static final Item resequencer = null;
		public static final Item frame_plastic = null;
		public static final Item frame_carbon = null;
		public static final Item frame_plasteel = null;
		public static final Item helmet_rebreather = null;
		public static final Item helmet_visor = null;
		public static final Item boots_flippers = null;
	}
	
	@ObjectHolder(UnWIRED.MOD_ID)
	public static class Blocks
	{
		public static final Block frame_polymer = null;
		public static final Block frame_carbon = null;
		public static final Block frame_plasteel = null;
		
		public static final Block plexiglass = null;
		public static final Block smartglass = null;
		public static final Block resequencer = null;
		
		public static final Block block_polymer = null;
		
		public static final Block soy_crop = null;
		
		public static final Block nano_fluid_block = null;
	}
	
	@ObjectHolder(UnWIRED.MOD_ID)
	public static class Fluids
	{
		public static final Fluid nano_fluid_source = null;
		public static final Fluid nano_fluid_flowing = null;
	}
	
	@ObjectHolder(UnWIRED.MOD_ID)
	public static class EntityTypes
	{
		public static final EntityType<GreyGooEntity> grey_goo = null;
	}
	
	@ObjectHolder(UnWIRED.MOD_ID)
	public static class Containers
	{
		public static final ContainerType<ResequencerContainer> resequencer = null;
	}
	
	@SubscribeEvent
	public static void onContainterRegistryEvent(final RegistryEvent.Register<ContainerType<?>> event)
	{
		IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
		
		registry.registerAll(
				new ContainerType<ResequencerContainer>(ResequencerContainer::new).setRegistryName(UnWIRED.MOD_ID, "resequencer")
		);
	}
	
	@SubscribeEvent
	public static void onBlockRegistryEvent(final RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		
		registry.registerAll(
				new UWBlock("frame_polymer", Block.Properties.create(Material.IRON, DyeColor.WHITE).hardnessAndResistance(5f).sound(SoundType.STONE)),
				new PlexiglassBlock(),
				new SmartglassBlock(),
				new UWBlock("block_polymer", Block.Properties.create(Material.IRON, DyeColor.WHITE).hardnessAndResistance(5f).sound(SoundType.STONE)),
				new SoyCrop(),
				new UWFluidBlock(() -> (FlowingFluid) Fluids.nano_fluid_source, Block.Properties.create(Material.WATER).doesNotBlockMovement().noDrops(), "nano_fluid"),
				new Resequencer()
		);
	}
	
	@SubscribeEvent
	public static void onItemRegistryEvent(final RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();
		
		registry.registerAll(
				new UWBase("ingot_polymer"),
				new UWBase("plate_polymer"),
				new UWBase("dust_polymer"),
				new UWBase("inert_goo"),
				new ActivatedGoo(),
				new UWBucket("nano_fluid", () -> Fluids.nano_fluid_source),
				new UWSpawnItem(EntityTypes.grey_goo, 0x616161, 0x343434,"grey_goo"),
				new UWBlockItem(Blocks.plexiglass),
				new UWBlockItem(Blocks.smartglass),
				new SoybeanItem(),
				new UWBlockItem(Blocks.frame_polymer),
				new UWBlockItem(Blocks.block_polymer),
				new UWDirectionalBlockItem(Blocks.resequencer),
				new RebreatherHelm(),
				new VisorHelm(),
				new FlippersBoot()
		);
	}
	
	@SubscribeEvent
	public static void onFluidRegistryEvent(final RegistryEvent.Register<Fluid> event)
	{
		IForgeRegistry<Fluid> registry = event.getRegistry();
		
		registry.registerAll(
				new NanoFluid.Source(),
				new NanoFluid.Flowing()
		);
	}
	
	@SubscribeEvent
	public static void onEntityRegistryEvent(final RegistryEvent.Register<EntityType<?>> event)
	{
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();
		
		registry.registerAll(
				EntityType.Builder.<GreyGooEntity>create(GreyGooEntity::new, EntityClassification.MONSTER)
						.setTrackingRange(80)
						.setUpdateInterval(3)
						.setShouldReceiveVelocityUpdates(true)
						.size(1f, 1f)
						.build(UnWIRED.MOD_ID+":grey_goo")
						.setRegistryName(UnWIRED.MOD_ID, "grey_goo")
		);
	}
	
	public static ItemGroup itemGroup = new ItemGroup(UnWIRED.MOD_ID) {
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(Items.inert_goo);
		}
	};
}
