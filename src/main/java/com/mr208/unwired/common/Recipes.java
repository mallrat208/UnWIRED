package com.mr208.unwired.common;

import com.mr208.unwired.api.crafting.GooConversion;
import com.mr208.unwired.common.Content.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;

public class Recipes
{
	public static void loadComplete()
	{
		BlockTags.getCollection().getOrCreate(new ResourceLocation("minecraft","logs"))
				.getAllElements()
				.forEach(
					(block)->
						GooConversion.addConversion(block,  Items.ingot_polymer)
				);
		
		
	}
}
