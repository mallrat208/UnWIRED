package com.mr208.unwired.client;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.client.render.CrateTileEntityRenderer;
import com.mr208.unwired.client.render.GreyGooRenderer;
import com.mr208.unwired.client.screen.MetabolicGenScreen;
import com.mr208.unwired.client.screen.ResequencerScreen;
import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.content.ModContainers;
import com.mr208.unwired.common.block.StorageCrate;
import com.mr208.unwired.common.tile.StorageCrateTile;
import com.mr208.unwired.common.content.ModItems;
import com.mr208.unwired.common.entity.GreyGooEntity;
import com.mr208.unwired.common.item.CrateItem;
import com.mr208.unwired.common.item.LabelMarker;
import com.mr208.unwired.common.item.base.IColorableEquipment;
import com.mr208.unwired.common.util.EnergyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.awt.*;
import java.util.Random;

@EventBusSubscriber(bus = Bus.MOD, value = Dist.CLIENT)
public class ClientSetup
{
	private static Random rand;
	
	public static void onSetup()
	{
		rand = new Random();
		
		ClientRegistry.bindTileEntitySpecialRenderer(StorageCrateTile.class, new CrateTileEntityRenderer());
		ScreenManager.registerFactory(ModContainers.resequencer, ResequencerScreen::new);
		ScreenManager.registerFactory(ModContainers.metabolic_generator, MetabolicGenScreen::new);
		RenderingRegistry.registerEntityRenderingHandler(GreyGooEntity.class, GreyGooRenderer::new);
		
		Minecraft.getInstance().getItemColors().register(
				(itemStack, i)-> i == 1 ? ((IColorableEquipment)ModItems.helmet_visor).getColorInt(itemStack) : -1, ModItems.helmet_visor);
		
		Minecraft.getInstance().getItemColors().register(
				(itemStack, i)-> i == 0 ? ((IColorableEquipment)ModItems.boots_flippers).getColorInt(itemStack) : -1, ModItems.boots_flippers);
		
		Minecraft.getInstance().getItemColors().register((stack, layer) -> layer == 1 ? getColorForEnergyPercentage(stack) : -1, ModItems.cell_bio);
		
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
	
	protected static int getColorForEnergyPercentage(ItemStack stack)
	{
		float percentage = EnergyUtil.getEnergyPercentage(stack);
		return new Color(Math.min(2f * (1 - percentage),1f),Math.min(2f * percentage, 1f), 0f).getRGB();
	}
	
	@SubscribeEvent
	public static void onTextureAtlasStich(TextureStitchEvent.Pre event)
	{
		if(!event.getMap().getBasePath().equalsIgnoreCase("textures"))
			return;
		
		event.addSprite(new ResourceLocation(UnWIRED.MOD_ID,"gui/uw_slot"));
		event.addSprite(new ResourceLocation(UnWIRED.MOD_ID,"gui/food_slot"));
		event.addSprite(new ResourceLocation(UnWIRED.MOD_ID,"gui/charge_slot"));
	}
}
