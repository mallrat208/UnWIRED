package com.mr208.unwired.common.entity;

import com.mr208.unwired.common.content.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;


public class GreyGooEntity extends SlimeEntity
{
	public GreyGooEntity(World world)
	{
		super((EntityType<GreyGooEntity>)ModEntities.grey_goo, world);
	}
	
	public GreyGooEntity(EntityType<? extends SlimeEntity> type, World worldIn)
	{
		super(type, worldIn);
		
		if(worldIn != null && !worldIn.isRemote)
			this.augmentEntityAI();
	}

	
	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, AttributeModifier.Operation.MULTIPLY_BASE));
		this.setSlimeSize(rand.nextInt(3)+1,true);
		this.recalculateSize();
		return spawnDataIn;
	}
	
	public static boolean canSpawnHere(EntityType<GreyGooEntity> entity, IWorld world, SpawnReason reason, BlockPos pos, Random rand)
	{
		return world.getDifficulty() != Difficulty.PEACEFUL;
	}
	
	@Override
	public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn)
	{
		return worldIn.getDifficulty()!= Difficulty.PEACEFUL && worldIn.getLight(this.getPosition()) < 8;
	}
	
	@Override
	public void applyEntityCollision(Entity entityIn)
	{
		super.applyEntityCollision(entityIn);
		
		if(entityIn instanceof GreyGooEntity)
			this.dealDamage((LivingEntity)entityIn);
	}
	
	@Override
	protected IParticleData getSquishParticle()
	{
		return ParticleTypes.SMOKE;
	}
	
	@Override
	public void onKillEntity(LivingEntity entityLivingIn)
	{
		super.onKillEntity(entityLivingIn);
	}
	
	protected void augmentEntityAI ()
	{
		//this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, GreyGooEntity.class, true));
	}
}
