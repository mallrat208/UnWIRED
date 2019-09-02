package com.mr208.unwired.common.block;

import com.mr208.unwired.UnWIRED;
import net.minecraft.util.BlockRenderLayer;

public class PlexiglassBlock extends AbstractUnWIREDGlass
{
	public PlexiglassBlock()
	{
		setRegistryName(UnWIRED.MOD_ID,"plexiglass");
		createItemBlock();
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
}
