package com.mr208.unwired.common.item.equipment;

import com.google.common.collect.Multimap;
import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.client.model.VisorModel;
import com.mr208.unwired.common.entity.UnWIREDAttributes;
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
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import javax.annotation.Nullable;

@EventBusSubscriber(bus = Bus.FORGE)
public class VisorHelm extends UWGadget
{
	private Object model;
	
	public VisorHelm()
	{
		super("helmet_visor", EquipmentSlotType.HEAD);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
		
		if(this.slot == slot)
		{
			map.put(UnWIREDAttributes.rangedAttackDamage.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Ranged damage", 0.15, Operation.MULTIPLY_BASE));
		}
		
		return map;
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
		
		if(itemStack.getItem() instanceof IColorableEquipment)
		{
			((VisorModel)model).setColorArray(getColorInt(itemStack));
		}
		
		((VisorModel)model).isChild = _default.isChild;
		((VisorModel)model).isSitting = _default.isSitting;
		((VisorModel)model).isSneak = _default.isSneak;
		
		return (A) model;
	}
	
	@Override
	public boolean isDyable()
	{
		return true;
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.DEDICATED_SERVER)
	public static void onRangedDamage(LivingHurtEvent event)
	{
		if(event.getSource().isProjectile() && event.getSource().getTrueSource() != null && event.getSource().getImmediateSource() != event.getSource().getTrueSource() && event.getSource().getTrueSource() instanceof LivingEntity )
		{
			LivingEntity attackingEntity =(LivingEntity)event.getSource().getTrueSource();
			
			ItemStack headSlot = attackingEntity.getItemStackFromSlot(EquipmentSlotType.HEAD);
			
			if(!headSlot.isEmpty() && headSlot.getItem() instanceof VisorHelm)
			{
				float damage = event.getAmount();
				double multiplier = attackingEntity.getAttribute(UnWIREDAttributes.rangedAttackDamage).getValue();
				
				event.setAmount((float)(damage * ( 1 + multiplier)));
			}
		}
	}
}
