package com.mr208.unwired.common.block;

import com.mr208.unwired.common.block.base.AbstractUnWIREDGlass;
import net.minecraft.util.BlockRenderLayer;

public class PlexiglassBlock extends AbstractUnWIREDGlass
{
	public PlexiglassBlock()
	{
		super("plexiglass");
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
}
