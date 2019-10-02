package com.mr208.unwired.common.item.equipment;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.client.model.VisorModel;
import com.mr208.unwired.common.item.base.UWGadget;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class VisorHelm extends UWGadget
{
	private Object model;
	
	public VisorHelm()
	{
		super("helmet_visor", EquipmentSlotType.HEAD);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
	{
		return UnWIRED.MOD_ID + ":textures/model/gadget/visor.png";
	}
	
	@OnlyIn(Dist.CLIENT)
	@Nullable
	@Override
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default)
	{
		
		if(!(model instanceof VisorModel))
		{
			model = new VisorModel();
		}
		
		model = new VisorModel();
		
		((VisorModel)model).isChild = _default.isChild;
		((VisorModel)model).isSitting = _default.isSitting;
		((VisorModel)model).isSitting = _default.isSitting;
		
		return (A) model;
	}
}
