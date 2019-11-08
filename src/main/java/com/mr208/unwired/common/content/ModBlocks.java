package com.mr208.unwired.common.content;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.block.*;
import com.mr208.unwired.common.block.FluidDrum.Drum;
import com.mr208.unwired.common.block.StorageCrate.Crate;
import com.mr208.unwired.common.block.base.UWBlock;
import com.mr208.unwired.common.block.base.UWFluidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.DyeColor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(UnWIRED.MOD_ID)
@EventBusSubscriber(bus = Bus.MOD)
public class ModBlocks
{
	public static final Block frame_polymer = null;
	public static final Block frame_carbon = null;
	public static final Block frame_plasteel = null;
	public static final Block plexiglass = null;
	public static final Block smartglass = null;
	public static final Block resequencer = null;
	public static final Block block_polymer = null;
	public static final Block crate_polymer = null;
	public static final Block drum_polymer = null;
	public static final Block soy_crop = null;
	public static final Block goo_slurry_block= null;
	public static final Block generator_metabolic = null;
	public static final Block goo_creche = null;
	
	public static final Block polymerized_log = null;
	public static final Block brittle_log = null;
	
	public static class Materials
	{
		public static final Material MACHINE = new Material.Builder(MaterialColor.IRON).build();
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
				new UWFluidBlock(() -> (FlowingFluid) ModFluids.goo_slurry, Block.Properties.create(Material.WATER).doesNotBlockMovement().noDrops(), "goo_slurry"),
				new Resequencer(),
				new StorageCrate(Crate.POLYMER),
				new MetabolicGenerator(),
				new GooCreche(),
				new FluidDrum(Drum.POLYMER),
				new PolymerizedLog(),
				new BrittleLog()
		);
	}
	

}
