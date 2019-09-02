package com.mr208.unwired.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GreyGooEntity extends SlimeEntity
{
	private static final DataParameter<Boolean> KILLED_BY_GOO =EntityDataManager.createKey(GreyGooEntity.class, DataSerializers.BOOLEAN);
	
	public GreyGooEntity(EntityType<? extends SlimeEntity> type, World worldIn)
	{
		super(type, worldIn);
		
		if(worldIn != null && !worldIn.isRemote)
			this.augmentEntityAI();
	}
	
	@Override
	protected void registerData()
	{
		super.registerData();
		this.dataManager.register(KILLED_BY_GOO, false);
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		if(cause.getTrueSource() instanceof GreyGooEntity || cause.getImmediateSource() instanceof GreyGooEntity)
		{
			this.dataManager.set(KILLED_BY_GOO, true);
		}
		
		super.onDeath(cause);
	}
	
	@Override
	public void remove()
	{
		if(this.dataManager.get(KILLED_BY_GOO))
		{
			this.removed = true;
			this.invalidateCaps();
		}
		else
		{
			super.remove();
		}
	}
	
	@Override
	public void remove(boolean keepData)
	{
		if(this.dataManager.get(KILLED_BY_GOO))
		{
			this.removed = true;
			this.invalidateCaps();
		}
		else
		{
			super.remove(keepData);
		}
	}
	
	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, AttributeModifier.Operation.MULTIPLY_BASE));
		if (this.rand.nextFloat() < 0.05F) {
			this.setLeftHanded(true);
		} else {
			this.setLeftHanded(false);
		}
		
		this.setSlimeSize(1,true);
		
		return spawnDataIn;
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
		
		if(entityLivingIn instanceof GreyGooEntity && this.getSlimeSize() <= ((GreyGooEntity)entityLivingIn).getSlimeSize())
		{
			this.setSlimeSize(this.getSlimeSize()+1, true);
			this.recalculateSize();
		}
	}
	
	protected void augmentEntityAI ()
	{
		this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, GreyGooEntity.class, true));
	}
}
