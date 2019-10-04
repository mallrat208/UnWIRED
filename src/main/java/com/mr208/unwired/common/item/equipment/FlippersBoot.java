package com.mr208.unwired.common.item.equipment;

import com.google.common.collect.Multimap;
import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.item.base.IColorableEquipment;
import com.mr208.unwired.common.item.base.UWGadget;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class FlippersBoot extends UWGadget implements IColorableEquipment
{
	public FlippersBoot()
	{
		super("boots_flippers", EquipmentSlotType.FEET);
	}
	
	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
	{
		return UnWIRED.MOD_ID+":textures/model/gadget/flippers.png";
	}
	
	@Nullable
	@Override
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default)
	{
		return null;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
		
		
		if(this.slot == slot)
		{
			map.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor speed",  -0.15f, Operation.MULTIPLY_BASE));
			map.put(LivingEntity.SWIM_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor swim speed", 0.25f, Operation.MULTIPLY_BASE));
		}
		
		return map;
	}
	
	@Override
	public boolean isDyable(ItemStack stack)
	{
		return stack.getItem() == this;
	}
	
	@Override
	public int getDefaultColor()
	{
		
		return 139;
	}
}
