package com.mr208.unwired.client;

import com.mr208.unwired.client.render.GreyGooRenderer;
import com.mr208.unwired.client.screen.ResequencerScreen;
import com.mr208.unwired.common.Content.Containers;
import com.mr208.unwired.common.entity.GreyGooEntity;
import com.mr208.unwired.setup.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import tabsapi.TabRegistry;

import java.util.Random;

public class ClientProxy implements IProxy
{
	private static Random rand = new Random();
	
	@Override
	public void spawnRebreatherParticle(BlockPos pos, float eyeHeight, float pitch, float yaw)
	{
		Vec3d modifiedPos = new Vec3d(
				pos.getX() + 0.5 + (eyeHeight <0.5 ? Math.cos(pitch):0),
				pos.getY() + eyeHeight,
				pos.getZ() + 0.5 - (eyeHeight <0.5 ?Math.cos(pitch): 0)
		);
		
		for(int i = 0; i < 20; i++)
		{
			Minecraft.getInstance().worldRenderer.addParticle(ParticleTypes.BUBBLE,
					true,
					modifiedPos.getX(),
					modifiedPos.getY(),
					modifiedPos.getZ(),
					0f, 1f, 0f);
		}
	}
	
	@Override
	public void spawnConversionParticles(BlockPos pos)
	{
		for(int i = 0; i < 20; i++)
		{
			Minecraft.getInstance().worldRenderer.addParticle(
					ParticleTypes.EXPLOSION,
					true,
					pos.getX() + .5 + rand.nextGaussian()/2,
					pos.getY() + .5 + rand.nextGaussian()/2,
					pos.getZ() + .5 + rand.nextGaussian()/2,
					0f,
					0.8f,
					00f
			);
		}
	}
}
