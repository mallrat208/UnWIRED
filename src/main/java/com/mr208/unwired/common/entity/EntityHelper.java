package com.mr208.unwired.common.entity;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.Content.EntityTypes;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class EntityHelper
{
	public static void loadComplete()
	{
		if(EntityTypes.grey_goo!=null)
		{
			UnWIRED.getLogger().info("Registering Grey Goo spawn entries");
			EntitySpawnPlacementRegistry.register(EntityTypes.grey_goo, PlacementType.ON_GROUND, Type.MOTION_BLOCKING_NO_LEAVES, EntityHelper::canEntitySpawn);
			
			for(Biome biome : ForgeRegistries.BIOMES.getValues())
			{
				biome.getSpawns(EntityClassification.MONSTER).add(new SpawnListEntry(EntityTypes.grey_goo, 20, 1, 1));
			}
		}
	}
	
	public static boolean canEntitySpawn(EntityType<? extends LivingEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
		BlockPos blockpos = pos.down();
		return reason == SpawnReason.SPAWNER || world.getBlockState(blockpos).canEntitySpawn(world, blockpos, entityType);
	}
}
