package com.mr208.unwired.client;

import com.mr208.unwired.common.content.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tabsapi.AbstractTab;

@OnlyIn(Dist.CLIENT)
public class ExoTab extends AbstractTab
{
	public ExoTab()
	{
		super(new ItemStack(ModItems.inert_goo));
		this.setMessage("Exo.Inventory");
	}
	
	@Override
	public void onTabClicked()
	{
	
	}
	
}
