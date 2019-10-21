package com.mr208.unwired.common.content;

import com.mr208.unwired.UnWIRED;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModGroups
{
	public static ItemGroup mainGroup = new ItemGroup(UnWIRED.MOD_ID) {
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ModItems.inert_goo);
		}
	};
}
