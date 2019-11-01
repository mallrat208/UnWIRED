package com.mr208.unwired.client;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.client.render.CrateTileEntityRenderer;
import com.mr208.unwired.client.render.DrumTileEntityRenderer;
import com.mr208.unwired.client.render.GreyGooRenderer;
import com.mr208.unwired.client.screen.FluidDrumScreen;
import com.mr208.unwired.client.screen.GooCrecheScreen;
import com.mr208.unwired.client.screen.MetabolicGenScreen;
import com.mr208.unwired.client.screen.ResequencerScreen;
import com.mr208.unwired.common.block.FluidDrum;
import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.content.ModContainers;
import com.mr208.unwired.common.block.StorageCrate;
import com.mr208.unwired.common.item.DrumItem;
import com.mr208.unwired.common.item.FluidCanister;
import com.mr208.unwired.common.tile.FluidDrumTile;
import com.mr208.unwired.common.tile.StorageCrateTile;
import com.mr208.unwired.common.content.ModItems;
import com.mr208.unwired.common.entity.GreyGooEntity;
import com.mr208.unwired.common.item.CrateItem;
import com.mr208.unwired.common.item.LabelMarker;
import com.mr208.unwired.common.item.base.IColorableEquipment;
import com.mr208.unwired.common.util.EnergyUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@EventBusSubscriber(bus = Bus.MOD, value = Dist.CLIENT)
public class ClientSetup
{
	private static Random rand;
	
	private static HashMap<Fluid, Integer> fluidColorMap = new HashMap<>();
	
	public static void onSetup()
	{
		rand = new Random();
		
		ClientRegistry.bindTileEntitySpecialRenderer(StorageCrateTile.class, new CrateTileEntityRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(FluidDrumTile.class, new DrumTileEntityRenderer());
		
		ScreenManager.registerFactory(ModContainers.resequencer, ResequencerScreen::new);
		ScreenManager.registerFactory(ModContainers.metabolic_generator, MetabolicGenScreen::new);
		ScreenManager.registerFactory(ModContainers.goo_creche, GooCrecheScreen::new);
		ScreenManager.registerFactory(ModContainers.fluid_drum, FluidDrumScreen::new);
		
		RenderingRegistry.registerEntityRenderingHandler(GreyGooEntity.class, GreyGooRenderer::new);
		
		Minecraft.getInstance().getItemColors().register(
				(itemStack, i)-> i == 1 ? ((IColorableEquipment)ModItems.helmet_visor).getColorInt(itemStack) : -1, ModItems.helmet_visor);
		
		Minecraft.getInstance().getItemColors().register(
				(itemStack, i)-> i == 0 ? ((IColorableEquipment)ModItems.boots_flippers).getColorInt(itemStack) : -1, ModItems.boots_flippers);
		
		Minecraft.getInstance().getItemColors().register((stack, layer) -> layer == 1 ? getColorForEnergyPercentage(stack) : -1, ModItems.cell_bio);
		
		for(FluidCanister canister:FluidCanister.getFluidCanisterItems())
		{
			Minecraft.getInstance().getItemColors().register(
					((itemStack, layer) -> {
						switch(layer)
						{
							case 0:
								return ((FluidCanister)itemStack.getItem()).getMaterialColor();
							case 1:
								return getOrCreateFluidColor(FluidUtil.getFluidHandler(itemStack).map(iFluidHandlerItem -> iFluidHandlerItem.getFluidInTank(0).getFluid()).orElse(Fluids.EMPTY));
							default:
								return -1;
						}
					}), canister);
		}
		
		for(LabelMarker marker:LabelMarker.registeredMarkers)
		{
			Minecraft.getInstance().getItemColors().register(
					(itemStack, layer) -> layer == 1 ? ((LabelMarker)itemStack.getItem()).getMarkerColor().getColorValue() : -1, marker);
		}
		
		for(CrateItem crate:CrateItem.getCrateItems())
			Minecraft.getInstance().getItemColors().register((itemStack, layer) -> layer == 1 ? ((CrateItem)itemStack.getItem()).getColor().getColorValue():-1, crate);
			
		for(DrumItem drum:DrumItem.getDrumItems())
			Minecraft.getInstance().getItemColors().register((itemStack, layer) -> layer == 1 ? ((DrumItem)itemStack.getItem()).getColor().getColorValue():-1, drum);
		
		Minecraft.getInstance().getBlockColors().register((blockState, iEnviromentBlockReader, blockPos, i) -> i == 1 ? blockState.get(FluidDrum.COLOR).getColorValue():-1, ModBlocks.drum_polymer);
		Minecraft.getInstance().getBlockColors().register((blockState, iEnviromentBlockReader, blockPos, i) -> i == 1 ? blockState.get(StorageCrate.COLOR).getColorValue() : -1, ModBlocks.crate_polymer);
		
		//TabRegistry.registerTab(new ExoTab());
	}
	
	private static int getOrCreateFluidColor(Fluid fluidIn)
	{
		if(fluidColorMap.containsKey(fluidIn))
			return fluidColorMap.get(fluidIn);
		
		if(fluidIn.getAttributes().getColor()!=-1)
		{
			fluidColorMap.put(fluidIn, fluidIn.getAttributes().getColor());
			return fluidIn.getAttributes().getColor();
		}
		
		int color = -1;
		InputStream stream;
		BufferedImage image;
		
		
		ArrayList<Integer> reds = new ArrayList<>();
		ArrayList<Integer> greens = new ArrayList<>();
		ArrayList<Integer> blues =  new ArrayList<>();
		
		int[] temp;
		
		UnWIRED.getLogger().info(fluidIn);
		ResourceLocation stillLoc = fluidIn.getAttributes().getStillTexture();
		
		UnWIRED.getLogger().info(stillLoc);
		
		if(stillLoc == null)
		{
			fluidColorMap.put(fluidIn, -1);
			return -1;
		}
		
		String[] stillLocParts = stillLoc.toString().split(":");
		if(stillLocParts.length!=2)
		{
			fluidColorMap.put(fluidIn, -1);
			return -1;
		}
		
		ResourceLocation textureLoc = new ResourceLocation(stillLocParts[0], "textures/" + stillLocParts[1] + ".png");
		
		try
		{
			stream = Minecraft.getInstance().getResourceManager().getResource(textureLoc).getInputStream();
			image = ImageIO.read(stream);
			
			for(int x = 6; x <=9; x++)
			{
				for(int y=6; y<=9; y++)
				{
					try
					{
						temp = image.getRaster().getPixel(x, y, (int[])null);
					} catch(ArrayIndexOutOfBoundsException exception)
					{
						temp=new int[3];
					}
					
					if(temp.length>=3)
					{
						reds.add(temp[0]);
						greens.add(temp[1]);
						blues.add(temp[2]);
					}
				}
			}
			
			if(reds.isEmpty() || greens.isEmpty() || blues.isEmpty())
			{
				fluidColorMap.put(fluidIn, -1);
				return -1;
			}
			
			int red = reds.stream().mapToInt(o -> o).sum() / reds.size();
			int green = greens.stream().mapToInt(o -> o).sum() / greens.size();
			int blue = blues.stream().mapToInt(o -> o).sum() / blues.size();
			color = new Color(red, green,blue).getRGB();
			fluidColorMap.put(fluidIn, color);
		}
		catch(IOException exception)
		{
			UnWIRED.getLogger().error("Failed to find the Still Texture for: {}", fluidIn);
			fluidColorMap.put(fluidIn, -1);
		}
		
		
		return color;
	}
	
	protected static int getColorForEnergyPercentage(ItemStack stack)
	{
		float percentage = EnergyUtils.getEnergyPercentage(stack);
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
