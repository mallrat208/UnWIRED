package com.mr208.unwired.api.crafting;

import com.mr208.unwired.api.UnWIREDAPI;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class GooConversion
{
	private static HashMap<ResourceLocation,Object> conversionMap = new HashMap<>();
	
	public static void addConversion(Block block, Object output)
	{
		if(!(output instanceof ItemStack) && !(output instanceof Item) && !( output instanceof Block))
		{
			UnWIREDAPI.LOGGER.warn("Unable to addConversion Goo Conversion Recipe for {}. Output, {},  is not an Block, Item, or ItemStack", block.getRegistryName(), output);
			return;
		}
		
		if(conversionMap.containsKey(block.getRegistryName()))
		{
			UnWIREDAPI.LOGGER.warn("Unable to addConversion Goo Conversion Recipe for {}. There is a preexisting conversion for this block",block.getRegistryName());
		}
		
		conversionMap.put(block.getRegistryName(), output);
	}
	
	public static Object getConversionOutput(Block block)
	{
		return getConversionOutput(block.getRegistryName());
	}
	
	public static Object getConversionOutput(ResourceLocation blockName)
	{
		if(hasConversion(blockName))
		{
			return conversionMap.get(blockName);
		}
		
		UnWIREDAPI.LOGGER.error("Failed to convert block: {}, there is no recipe present", blockName);
		return null;
	}
	
	public static boolean hasConversion(Block block)
	{
		return hasConversion(block.getRegistryName());
	}
	
	public static boolean hasConversion(ResourceLocation blockName)
	{
		return conversionMap.containsKey(blockName);
	}
	
	public static void removeConversion(Block block)
	{
		removeConversion(block.getRegistryName());
	}
	
	public static void removeConversion(ResourceLocation blockName)
	{
		conversionMap.remove(blockName);
		UnWIREDAPI.LOGGER.info("Removed Goo Conversion recipe for {}", blockName);
	}
	
	
}
