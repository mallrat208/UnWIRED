package com.mr208.unwired.setup;

import net.minecraft.util.math.BlockPos;

public interface IProxy
{
	void init();
	
	
	default void setup()
	{
	
	}
	
	default void spawnRebreatherParticle(BlockPos pos, float eyeHeight, float pitch, float yaw) {}
}
