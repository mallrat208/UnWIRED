package com.mr208.unwired.common.content;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.tile.FluidDrumTile;
import com.mr208.unwired.common.tile.GooCrecheTile;
import com.mr208.unwired.common.tile.MetabolicGenTile;
import com.mr208.unwired.common.tile.PolymerizedLogTile;
import com.mr208.unwired.common.tile.StorageCrateTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(UnWIRED.MOD_ID)
@EventBusSubscriber(bus = Bus.MOD)
public class ModTileEntities
{
	public static final TileEntityType<StorageCrateTile> storage_crate = null;
	public static final TileEntityType<MetabolicGenTile> generator_metabolic = null;
	public static final TileEntityType<GooCrecheTile> goo_creche = null;
	public static final TileEntityType<FluidDrumTile> fluid_drum = null;
	
	public static final TileEntityType<PolymerizedLogTile> polymerized_log = null;
	
	@SubscribeEvent
	public static void onTileEntityRegistryEvent(final RegistryEvent.Register<TileEntityType<?>> event)
	{
		IForgeRegistry registry = event.getRegistry();
		
		registry.registerAll(
				TileEntityType.Builder.create(StorageCrateTile::new, ModBlocks.crate_polymer)
					.build(null)
					.setRegistryName(UnWIRED.MOD_ID,"storage_crate"),
				TileEntityType.Builder.create(MetabolicGenTile::new, ModBlocks.generator_metabolic)
					.build(null)
					.setRegistryName(UnWIRED.MOD_ID,"generator_metabolic"),
				TileEntityType.Builder.create(GooCrecheTile::new, ModBlocks.goo_creche)
					.build(null)
					.setRegistryName(UnWIRED.MOD_ID, "goo_creche"),
				TileEntityType.Builder.create(FluidDrumTile::new, ModBlocks.drum_polymer)
					.build(null)
					.setRegistryName(UnWIRED.MOD_ID, "fluid_drum"),
				TileEntityType.Builder.create(PolymerizedLogTile::new, ModBlocks.polymerized_log)
					.build(null)
					.setRegistryName(UnWIRED.MOD_ID, "polymerized_log")
		);

	}
}
