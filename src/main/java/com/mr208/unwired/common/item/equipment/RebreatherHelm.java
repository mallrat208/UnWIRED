package com.mr208.unwired.common.item.equipment;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.client.model.RebreatherModel;
import com.mr208.unwired.common.content.ModGroups;
import com.mr208.unwired.common.content.ModItems;
import com.mr208.unwired.network.NetworkHandler;
import com.mr208.unwired.network.packet.RebreatherParticlePacket;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerEvent.Visibility;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import javax.annotation.Nullable;
import java.util.HashMap;

@EventBusSubscriber(bus = Bus.FORGE)
public class RebreatherHelm extends UWGadget
{
	private Object model;
	
	public RebreatherHelm()
	{
		super("helmet_rebreather", EquipmentSlotType.HEAD);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
	{
		return UnWIRED.MOD_ID + ":textures/model/gadget/rebreather.png";
	}
	
	@OnlyIn(Dist.CLIENT)
	@Nullable
	@Override
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default)
	{
		
		if(!(model instanceof RebreatherModel))
		{
			model = new RebreatherModel();
		}
		
		model = new RebreatherModel();
		
		((RebreatherModel)model).isChild = _default.isChild;
		((RebreatherModel)model).isSneak = _default.isSneak;
		((RebreatherModel)model).isSitting = _default.isSitting;
		
		return (A) model;

	}
	
	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
	{
		if(!world.isRemote())
		{
			if(player.isCreative()||player.ticksExisted%20!=0)
				return;
			
			if(player.isInWater())
			{
				if(player.getAir()<=1&&isUsable(stack))
				{
					stack.attemptDamageItem(1, world.rand, (ServerPlayerEntity)player);
					player.setAir(player.getMaxAir());
					world.playSound(null, player.getPosition(), SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE, SoundCategory.PLAYERS, 1f, 1.5f);
					NetworkHandler.sendToNearbyPlayers(player, 32, world.getDimension().getType(), new RebreatherParticlePacket(player.getPosition(), player.getEyeHeight(), ((ServerPlayerEntity)player).rotationPitch, ((ServerPlayerEntity)player).rotationYaw));
				}
			}
			else
			{
				if(stack.isDamaged())
				{
					stack.setDamage(Math.min(stack.getDamage() -2, stack.getMaxDamage()));
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onBreakSpeed(BreakSpeed event)
	{
		Entity entity = event.getEntity();
		
		if(entity instanceof PlayerEntity && entity.isInWater() && ((PlayerEntity)entity).getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == ModItems.helmet_rebreather)
		{
			PlayerEntity player = event.getEntityPlayer();
			float speed = event.getOriginalSpeed();
			speed = player.isInWater()? speed * 3 : speed;
			speed = player.onGround ? speed : speed * 3;
			event.setNewSpeed(speed);
		}
	}
}
