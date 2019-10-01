package com.mr208.unwired.client;

import com.mr208.unwired.client.render.GreyGooRenderer;
import com.mr208.unwired.client.screen.ResequencerScreen;
import com.mr208.unwired.common.Content.Containers;
import com.mr208.unwired.common.entity.GreyGooEntity;
import com.mr208.unwired.setup.IProxy;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import tabsapi.TabRegistry;

public class ClientProxy implements IProxy
{
	@Override
	public void init()
	{
		
		ScreenManager.registerFactory(Containers.resequencer, ResequencerScreen::new);
		
		RenderingRegistry.registerEntityRenderingHandler(GreyGooEntity.class, GreyGooRenderer::new);
		
		TabRegistry.registerTab(new ExoTab());
	}
}
