package com.mr208.unwired.common.block.base;

import com.mr208.unwired.UnWIRED;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public abstract class AbstractUnWIREDGlass extends AbstractGlassBlock
{
	protected AbstractUnWIREDGlass(String name)
	{
		super(Properties.create(Material.GLASS)
				.sound(SoundType.GLASS)
				.hardnessAndResistance(0.4f));
		setRegistryName(UnWIRED.MOD_ID, name);
	}
}
