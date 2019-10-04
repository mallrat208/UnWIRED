package com.mr208.unwired.setup;

import net.minecraft.util.math.BlockPos;

public interface IProxy
{
	default void spawnRebreatherParticle(BlockPos pos, float eyeHeight, float pitch, float yaw) {}
	
	default void spawnConversionParticles(BlockPos pos) {}
}
