package com.mr208.unwired.client;

import com.mr208.unwired.client.render.GreyGooRenderer;
import com.mr208.unwired.common.entity.GreyGooEntity;
import com.mr208.unwired.setup.IProxy;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy
{
	@Override
	public void init()
	{
		
		RenderingRegistry.registerEntityRenderingHandler(GreyGooEntity.class, GreyGooRenderer::new);
	}
}
