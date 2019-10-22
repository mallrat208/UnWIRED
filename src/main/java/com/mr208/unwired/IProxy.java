package com.mr208.unwired;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public interface IProxy
{
	default PlayerEntity getClientPlayer() { return null;}
	
	
	default void openMarkerScreen(BlockPos blockPos) {}
	
	default void setWritableSignColor(BlockPos blockPos, String colorKey) {}
	
	default void spawnRebreatherParticle(BlockPos pos, float eyeHeight, float pitch, float yaw) {}
	
	default void spawnConversionParticles(BlockPos pos) {}
	
	default void syncEnergy(BlockPos pos, int energy) {}
}
