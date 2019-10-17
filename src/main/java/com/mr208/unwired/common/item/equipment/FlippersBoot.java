package com.mr208.unwired.common.item.equipment;

import com.google.common.collect.Multimap;
import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.client.model.FlippersModel;
import com.mr208.unwired.common.item.base.IColorableEquipment;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class FlippersBoot extends UWGadget
{
	private Object model;
	
	public FlippersBoot()
	{
		super("boots_flippers", EquipmentSlotType.FEET);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
	{
		return UnWIRED.MOD_ID+":textures/model/gadget/flippers.png";
	}
	
	@OnlyIn(Dist.CLIENT)
	@Nullable
	@Override
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default)
	{
		if(!(model instanceof FlippersModel))
		{
			model = new FlippersModel();
		}
		
		if(itemStack.getItem() instanceof IColorableEquipment)
		{
			((FlippersModel)model).setColorArray(getColorInt(itemStack));
		}
		
		((FlippersModel)model).isChild = _default.isChild;
		((FlippersModel)model).isSitting = _default.isSitting;
		((FlippersModel)model).isSneak = _default.isSneak;
		
		return (A) model;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
		
		
		if(this.slot == slot)
		{
			map.put(LivingEntity.SWIM_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor swim speed", 0.25f, Operation.MULTIPLY_BASE));
		}
		
		return map;
	}
	
	@Override
	public boolean isDyable()
	{
		return true;
	}
	
	@Override
	public int getDefaultColor()
	{
		
		return 139;
	}
}
