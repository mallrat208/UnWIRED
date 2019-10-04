package com.mr208.unwired.common.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(bus = Bus.FORGE)
public class UnWIREDAttributes
{
	public static final RangedAttribute resistFire = new RangedAttribute(null, "unwired.resist_fire", 0, 0, 100);
	public static final RangedAttribute resistExplosion = new RangedAttribute(null, "unwired.resist_explosion", 0,0,100);
	public static final RangedAttribute resistProjectile = new RangedAttribute(null, "unwired.resist_projectile", 0,0,100);
	public static final RangedAttribute resistMagic = new RangedAttribute( null, "unwired.resist_magic", 0,0, 100);
	public static final RangedAttribute resistFall = new RangedAttribute(null, "unwired.resist_fall",0,0,100);
	public static final RangedAttribute bonusWalkspeed = new RangedAttribute(null,"unwired.walkspeed",0,0, 100);
	public static final RangedAttribute bonusWaterspeed = new RangedAttribute(null, "unwired.waterspeed", 0, 0, 100);
	public static final RangedAttribute bonusJumpheight = new RangedAttribute(null, "unwired.jumpheight", 0,0, 100);
	public static final RangedAttribute bonusStability = new RangedAttribute(null, "unwired.stability", 0,0,100);
	public static final RangedAttribute rangedAttackDamage = new RangedAttribute(null, "unwired.damage_ranged",1, 0, 100);
	
		@SubscribeEvent
	public static void onEntityConstruction(EntityConstructing event)
	{
		if(event.getEntity() instanceof PlayerEntity)
		{
			LivingEntity e = (LivingEntity) event.getEntity();
			
			e.getAttributes().registerAttribute(resistFire);
			e.getAttributes().registerAttribute(resistExplosion);
			e.getAttributes().registerAttribute(resistProjectile);
			e.getAttributes().registerAttribute(resistMagic);
			e.getAttributes().registerAttribute(resistFall);
			e.getAttributes().registerAttribute(bonusWalkspeed);
			e.getAttributes().registerAttribute(bonusJumpheight);
			e.getAttributes().registerAttribute(bonusWaterspeed);
			e.getAttributes().registerAttribute(bonusStability);
			e.getAttributes().registerAttribute(rangedAttackDamage);
		}
	}}
