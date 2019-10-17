package com.mr208.unwired.client;

import com.mr208.unwired.client.render.CrateTileEntityRenderer;
import com.mr208.unwired.client.render.GreyGooRenderer;
import com.mr208.unwired.client.screen.ResequencerScreen;
import com.mr208.unwired.common.Content.Blocks;
import com.mr208.unwired.common.Content.Containers;
import com.mr208.unwired.common.Content.Items;
import com.mr208.unwired.common.block.StorageCrate;
import com.mr208.unwired.common.block.tile.StorageCrateTile;
import com.mr208.unwired.common.entity.GreyGooEntity;
import com.mr208.unwired.common.item.LabelMarker;
import com.mr208.unwired.common.item.base.IColorableEquipment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.DyeColor;
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
		ScreenManager.registerFactory(Containers.resequencer, ResequencerScreen::new);
		RenderingRegistry.registerEntityRenderingHandler(GreyGooEntity.class, GreyGooRenderer::new);
		
		Minecraft.getInstance().getItemColors().register(
				(itemStack, i)-> i == 1 ? ((IColorableEquipment)Items.helmet_visor).getColorInt(itemStack) : -1, Items.helmet_visor);
		
		Minecraft.getInstance().getItemColors().register(
				(itemStack, i)-> i == 0 ? ((IColorableEquipment)Items.boots_flippers).getColorInt(itemStack) : -1, Items.boots_flippers);
		
		for(LabelMarker marker:LabelMarker.registeredMarkers)
		{
			Minecraft.getInstance().getItemColors().register(
					(itemStack, layer) -> layer == 1 ? ((LabelMarker)itemStack.getItem()).getMarkerColor().getColorValue() : -1, marker);
		}
		
		Minecraft.getInstance().getItemColors().register(
				(itemStack, i) -> i == 1 ? DyeColor.LIGHT_GRAY.getColorValue() : -1, Items.crate_polymer);		
		
		Minecraft.getInstance().getBlockColors().register((blockState, iEnviromentBlockReader, blockPos, i) -> i == 1 ? blockState.get(StorageCrate.COLOR).getColorValue() : -1, Blocks.crate_polymer);
		
		//TabRegistry.registerTab(new ExoTab());
	}
}
