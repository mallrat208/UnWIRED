package com.mr208.unwired.common;

import com.mr208.unwired.api.crafting.GooConversion;
import com.mr208.unwired.common.Content.Items;
import net.minecraft.block.ComposterBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

public class Recipes
{
	private static Method registerCompostable;
	
	public static void loadComplete()
	{
		registerCompostable = ObfuscationReflectionHelper.findMethod(ComposterBlock.class, "func_220290_a", float.class, IItemProvider.class);
		
		try
		{
			registerCompostable.invoke(null, 0.5f, Items.soybean);
		}
		catch(Exception e)
		{
		
		}
	}
	
	public static void serverStarting()
	{
		BlockTags.getCollection().getOrCreate(new ResourceLocation("minecraft","logs"))
				.getAllElements()
				.forEach(
					(block)->
						GooConversion.addConversion(block,  Items.ingot_polymer)
				);
		
	}
}
