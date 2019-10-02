package com.mr208.unwired.common.item.base;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.Content;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;

public class UWArmor extends ArmorItem
{
	public UWArmor(String name, IArmorMaterial armorMaterial, EquipmentSlotType slotType)
	{
		super(armorMaterial, slotType, new Item.Properties().group(Content.itemGroup).maxStackSize(1));
		this.setRegistryName(UnWIRED.MOD_ID,name);
	}
}
