package com.mr208.unwired.client;

import com.mr208.unwired.common.Content.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tabsapi.AbstractTab;

@OnlyIn(Dist.CLIENT)
public class ExoTab extends AbstractTab
{
	public ExoTab()
	{
		super(new ItemStack(Items.inert_goo));
		this.setMessage("Exo.Inventory");
	}
	
	@Override
	public void onTabClicked()
	{
	
	}
	
}
