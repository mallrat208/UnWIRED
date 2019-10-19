package com.mr208.unwired.common.item.equipment;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.client.model.RebreatherModel;
import com.mr208.unwired.network.NetworkHandler;
import com.mr208.unwired.network.packet.RebreatherParticlePacket;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import javax.annotation.Nullable;

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
			if(player.isCreative()||!player.isInWater()||player.ticksExisted%20!=0)
				return;
			UnWIRED.getLogger().info(isUsable(stack));
			if(player.getAir()<=1 && isUsable(stack))
			{
				stack.attemptDamageItem(1, world.rand, (ServerPlayerEntity)player);
				player.setAir(player.getMaxAir());
				world.playSound(null, player.getPosition(), SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE, SoundCategory.PLAYERS, 1f, 1.5f);
				NetworkHandler.sendToNearbyPlayers(player, 32, world.getDimension().getType(), new RebreatherParticlePacket(player.getPosition(), player.getEyeHeight(), ((ServerPlayerEntity)player).rotationPitch, ((ServerPlayerEntity)player).rotationYaw));
			}
		}
	}
}
