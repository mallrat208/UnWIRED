package com.mr208.unwired.common.item.equipment;

import com.mr208.unwired.common.content.ModItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import java.util.function.Supplier;

public enum UnWIREDMaterials implements IArmorMaterial, IStringSerializable
{

	GADGET(13, new int[]{1,2,3,1},0f, 0,SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, () -> {return Ingredient.fromItems(ModItems.active_goo);}),
	POLYMER(15, new int[]{2,5,6,2},0.0f,10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, () -> {return Ingredient.fromItems(ModItems.ingot_polymer);});
	
	private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
	private final int durability;
	private final int[] damageReduction;
	private final int enchantability;
	private final SoundEvent soundEvent;
	private final LazyLoadBase<Ingredient> repairMaterial;
	private final float toughness;

	UnWIREDMaterials(int durability, int[] damageReduction, float toughness, int enchantability, SoundEvent soundEvent, Supplier<Ingredient> repairMaterial)
	{
		this.durability = durability;
		this.damageReduction = damageReduction;
		this.toughness = toughness;
		this.enchantability = enchantability;
		this.soundEvent = soundEvent;
		this.repairMaterial = new LazyLoadBase<>(repairMaterial);
	}
	
	@Override
	public int getDurability(EquipmentSlotType equipmentSlotType)
	{
		return MAX_DAMAGE_ARRAY[equipmentSlotType.getIndex()] * this.durability;
	}
	
	@Override
	public int getDamageReductionAmount(EquipmentSlotType equipmentSlotType)
	{
		
		return this.damageReduction[equipmentSlotType.getIndex()];
	}
	
	@Override
	public int getEnchantability()
	{
		return this.enchantability;
	}
	
	@Override
	public SoundEvent getSoundEvent()
	{
		return this.soundEvent;
	}
	
	@Override
	public Ingredient getRepairMaterial()
	{
		return this.repairMaterial.getValue();
	}
	
	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}
	
	@Override
	public float getToughness()
	{
		return this.toughness;
	}
}

