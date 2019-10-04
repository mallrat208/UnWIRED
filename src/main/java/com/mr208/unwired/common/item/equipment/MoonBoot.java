package com.mr208.unwired.common.item.equipment;

import com.google.common.collect.Multimap;
import com.mr208.unwired.common.item.base.UWGadget;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(bus = Bus.FORGE)
public class MoonBoot extends UWGadget
{
	public MoonBoot()
	{
		super("boots_moon", EquipmentSlotType.FEET);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
		
		if(this.slot == slot)
		{
			map.put(LivingEntity.ENTITY_GRAVITY.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Entity gravity", -0.85D, Operation.MULTIPLY_BASE));
		}
		
		return map;
	}
	
	@SubscribeEvent
	public static void onLivingFall(LivingFallEvent event)
	{
		if(event.getEntity() instanceof PlayerEntity && ((PlayerEntity)event.getEntity()).isServerWorld())
		{
			if(((PlayerEntity)event.getEntity()).getItemStackFromSlot(EquipmentSlotType.FEET).getItem() instanceof MoonBoot)
			{
				event.setCanceled(true);
			}
		}
	}
}
