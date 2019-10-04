package com.mr208.unwired.client;

import com.mr208.unwired.client.render.GreyGooRenderer;
import com.mr208.unwired.client.screen.ResequencerScreen;
import com.mr208.unwired.common.Content.Containers;
import com.mr208.unwired.common.Content.Items;
import com.mr208.unwired.common.entity.GreyGooEntity;
import com.mr208.unwired.common.item.base.IColorableEquipment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import tabsapi.TabRegistry;

import java.util.Random;

public class ClientSetup
{
	private static Random rand;
	
	public static void onSetup()
	{
		rand = new Random();
		
		ScreenManager.registerFactory(Containers.resequencer, ResequencerScreen::new);
		
		RenderingRegistry.registerEntityRenderingHandler(GreyGooEntity.class, GreyGooRenderer::new);
		
		Minecraft.getInstance().getItemColors().register(
				(itemStack, i)-> i == 1 ? ((IColorableEquipment)Items.helmet_visor).getColorInt(itemStack) : -1, Items.helmet_visor);
		
		Minecraft.getInstance().getItemColors().register(
				(itemStack, i)-> i == 0 ? ((IColorableEquipment)Items.boots_flippers).getColorInt(itemStack) : -1, Items.boots_flippers);
		
		//TabRegistry.registerTab(new ExoTab());
	}
}
