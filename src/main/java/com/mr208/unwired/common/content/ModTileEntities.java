package com.mr208.unwired.common.content;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.block.tile.MetabolicGenTile;
import com.mr208.unwired.common.block.tile.StorageCrateTile;
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
	
	@SubscribeEvent
	public static void onTileEntityRegistryEvent(final RegistryEvent.Register<TileEntityType<?>> event)
	{
		IForgeRegistry registry = event.getRegistry();
		
		registry.registerAll(
				TileEntityType.Builder.<StorageCrateTile>create(StorageCrateTile::new, ModBlocks.crate_polymer)
				.build(null)
				.setRegistryName(UnWIRED.MOD_ID,"storage_crate"),
				TileEntityType.Builder.<MetabolicGenTile>create(MetabolicGenTile::new, ModBlocks.generator_metabolic)
				.build(null)
				.setRegistryName(UnWIRED.MOD_ID,"generator_metabolic")
		);

	}
}
