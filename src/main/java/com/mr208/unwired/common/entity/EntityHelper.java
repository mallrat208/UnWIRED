package com.mr208.unwired.common.entity;

import com.mr208.unwired.Config;
import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.content.ModEntities;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class EntityHelper
{
	public static void loadComplete()
	{
		if(ModEntities.grey_goo!=null)
		{
			UnWIRED.getLogger().info("Registering Grey Goo spawn entries");
			EntitySpawnPlacementRegistry.register(ModEntities.grey_goo, PlacementType.ON_GROUND, Type.MOTION_BLOCKING_NO_LEAVES, EntityHelper::canGreyGooSpawn);
			
			for(Biome biome : ForgeRegistries.BIOMES.getValues())
			{
				if(biome == Biomes.THE_VOID)
					continue;
				if(biome == Biomes.MUSHROOM_FIELD_SHORE)
					continue;
				if(biome == Biomes.MUSHROOM_FIELDS)
					continue;
				
				biome.getSpawns(EntityClassification.MONSTER).add(new SpawnListEntry(ModEntities.grey_goo, 20, 1, 1));
			}
		}
	}
	
	public static boolean canGreyGooSpawn(EntityType<? extends LivingEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		return Config.GREY_GOO_DIMENSION_WHITELIST.get().contains(world.getDimension().getType().getId()) && canEntitySpawn(entityType, world, reason, pos, random);
	}
	
	public static boolean canEntitySpawn(EntityType<? extends LivingEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
		BlockPos blockpos = pos.down();
		
		return world.getDifficulty() != Difficulty.PEACEFUL && world.getBlockState(blockpos).canEntitySpawn(world, blockpos, entityType) && world.getLight(blockpos) < 8;
		//return (reason == SpawnReason.SPAWNER || world.getBlockState(blockpos).canEntitySpawn(world, blockpos, entityType)) && world.getLight(pos.down()) < 8;
	}
}
