package com.mr208.unwired.common.item.base;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mr208.unwired.common.item.equipment.UnWIREDMaterials;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class UWGadget extends UWArmor
{
	public UWGadget(String name, EquipmentSlotType slot)
	{
		super(name, UnWIREDMaterials.GADGET, slot);
		this.addPropertyOverride(new ResourceLocation("broken"),
				(itemStack, world, livingEntity) -> isUsable(itemStack) ? 0.0F : 1.0F);
	}
	
	public static boolean isUsable(ItemStack stack)
	{
		return stack.getDamage() < (stack.getMaxDamage() - 1);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
		HashMultimap<String, AttributeModifier> multimap = HashMultimap.create();
		if(this.slot == slot)
		{
			multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", isUsable(stack) ? (double)this.damageReduceAmount:0, Operation.ADDITION));
			multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", isUsable(stack) ? (double)this.toughness:0, Operation.ADDITION));
		}
		
		return multimap;
	}
}
