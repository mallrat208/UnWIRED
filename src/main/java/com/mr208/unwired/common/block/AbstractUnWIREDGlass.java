package com.mr208.unwired.common.block;

import com.mr208.unwired.common.Content;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public abstract class AbstractUnWIREDGlass extends AbstractGlassBlock
{
	protected AbstractUnWIREDGlass()
	{
		super(Properties.create(Material.GLASS)
				.sound(SoundType.GLASS)
				.hardnessAndResistance(0.4f));
		
		Content.registeredBlocks.add(this);
	}
	
	public void createItemBlock()
	{
		if(this.getRegistryName()!=null)
		{
			Item blockItem = new BlockItem(this, new Item.Properties().group(Content.itemGroup)).setRegistryName(this.getRegistryName());
			Content.registeredItems.add(blockItem);
		}
	}
}
