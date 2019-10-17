package com.mr208.unwired.common.item.base;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.Content;
import com.mr208.unwired.common.Content.Items;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class UWBase extends Item
{
	public UWBase(String name, Properties properties)
	{
		super(properties);
		setRegistryName(UnWIRED.MOD_ID, name);
	}
	
	public UWBase(String name)
	{
		this(name, new Item.Properties().group(Content.itemGroup));
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced)
	{
		if(stack.getItem() == Items.ingot_polymer)
			tooltip.add(new TranslationTextComponent("tooltip.unwired.polymer_ingot").setStyle(new Style().setColor(TextFormatting.DARK_GRAY)));
		else if(stack.getItem() == Items.inert_goo)
			tooltip.add(new TranslationTextComponent("tooltip.unwired.goo").setStyle(new Style().setColor(TextFormatting.DARK_GRAY)));
			
		super.addInformation(stack, world, tooltip, advanced);
	}
}
