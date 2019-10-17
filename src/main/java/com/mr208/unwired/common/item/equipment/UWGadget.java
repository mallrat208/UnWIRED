package com.mr208.unwired.common.item.equipment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mr208.unwired.common.item.base.IColorableEquipment;
import com.mr208.unwired.common.item.base.UWArmor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class UWGadget extends UWArmor implements IColorableEquipment
{
	public UWGadget(String name, EquipmentSlotType slot)
	{
		super(name, UnWIREDMaterials.GADGET, slot);
		this.addPropertyOverride(new ResourceLocation("broken"),
				(itemStack, world, livingEntity) -> isUsable(itemStack) ? 0.0F : 1.0F);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(isDyable())
		{
			tooltip.add(new TranslationTextComponent("tooltip.unwired.colorable").setStyle(new Style().setColor(TextFormatting.DARK_GRAY)));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
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
