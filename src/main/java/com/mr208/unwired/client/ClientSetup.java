package com.mr208.unwired.client;

import com.mr208.unwired.client.render.CrateTileEntityRenderer;
import com.mr208.unwired.client.render.GreyGooRenderer;
import com.mr208.unwired.client.screen.ResequencerScreen;
import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.content.ModContainers;
import com.mr208.unwired.common.block.StorageCrate;
import com.mr208.unwired.common.block.tile.StorageCrateTile;
import com.mr208.unwired.common.content.ModItems;
import com.mr208.unwired.common.entity.GreyGooEntity;
import com.mr208.unwired.common.item.CrateItem;
import com.mr208.unwired.common.item.LabelMarker;
import com.mr208.unwired.common.item.base.IColorableEquipment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.Random;

public class ClientSetup
{
	private static Random rand;
	
	public static void onSetup()
	{
		rand = new Random();
		
		
		ClientRegistry.bindTileEntitySpecialRenderer(StorageCrateTile.class, new CrateTileEntityRenderer());
		ScreenManager.registerFactory(ModContainers.resequencer, ResequencerScreen::new);
		RenderingRegistry.registerEntityRenderingHandler(GreyGooEntity.class, GreyGooRenderer::new);
		
		Minecraft.getInstance().getItemColors().register(
				(itemStack, i)-> i == 1 ? ((IColorableEquipment)ModItems.helmet_visor).getColorInt(itemStack) : -1, ModItems.helmet_visor);
		
		Minecraft.getInstance().getItemColors().register(
				(itemStack, i)-> i == 0 ? ((IColorableEquipment)ModItems.boots_flippers).getColorInt(itemStack) : -1, ModItems.boots_flippers);
		
		for(LabelMarker marker:LabelMarker.registeredMarkers)
		{
			Minecraft.getInstance().getItemColors().register(
					(itemStack, layer) -> layer == 1 ? ((LabelMarker)itemStack.getItem()).getMarkerColor().getColorValue() : -1, marker);
		}
		
		for(CrateItem crate:CrateItem.getCrateItems())
		{
			Minecraft.getInstance().getItemColors().register((itemStack, layer) -> layer == 1 ? ((CrateItem)itemStack.getItem()).getColor().getColorValue():-1, crate);
		}
		
		Minecraft.getInstance().getBlockColors().register((blockState, iEnviromentBlockReader, blockPos, i) -> i == 1 ? blockState.get(StorageCrate.COLOR).getColorValue() : -1, ModBlocks.crate_polymer);
		
		//TabRegistry.registerTab(new ExoTab());
	}
}
