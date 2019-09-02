package com.mr208.unwired.client.render;

import com.mr208.unwired.UnWIRED;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.ResourceLocation;

public class GreyGooRenderer extends SlimeRenderer
{
	private static final ResourceLocation SLIME_TEXTURES = new ResourceLocation(UnWIRED.MOD_ID ,"textures/entity/grey_goo.png");
	
	public GreyGooRenderer(EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(SlimeEntity entity)
	{
		return SLIME_TEXTURES;
	}
}
