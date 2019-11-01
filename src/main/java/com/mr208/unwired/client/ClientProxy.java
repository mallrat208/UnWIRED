package com.mr208.unwired.client;

import com.mr208.unwired.client.screen.EditWritableScreen;
import com.mr208.unwired.common.tile.IWritable;
import com.mr208.unwired.IProxy;
import com.mr208.unwired.common.tile.UWBaseTileEntity;
import com.mr208.unwired.common.tile.UWEnergyTile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements IProxy
{
	public static Random rand = new Random();
	
	@Override
	public void setWritableSignColor(BlockPos blockPos, String colorKey)
	{
		World world = getClientPlayer().world;
		
		if(world.isBlockLoaded(blockPos))
		{
			TileEntity tile = world.getTileEntity(blockPos);
			if(tile !=null && tile instanceof IWritable)
			{
				((IWritable)tile).setTextColor(DyeColor.byTranslationKey(colorKey,DyeColor.BLACK));
			}
			else if(tile != null && tile instanceof SignTileEntity)
			{
				((SignTileEntity)tile).setTextColor(DyeColor.byTranslationKey(colorKey, DyeColor.BLACK));
			}
		}
	}
	
	@Override
	public void openMarkerScreen(BlockPos blockPos)
	{
		World world = getClientPlayer().world;
		if(world.isBlockLoaded(blockPos))
			if(world.getTileEntity(blockPos) instanceof IWritable)
				Minecraft.getInstance().displayGuiScreen(new EditWritableScreen((IWritable)world.getTileEntity(blockPos)));
	}
	
	@Override
	public PlayerEntity getClientPlayer()
	{
		return Minecraft.getInstance().player;
	}
	
	public World getClientWorld()
	{
		return Minecraft.getInstance().world;
	}
	
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
	
	@Override
	public void syncEnergy(BlockPos pos, int energy)
	{
		World world = getClientWorld();
		
		if(world.isBlockLoaded(pos))
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			
			if(tileEntity instanceof UWEnergyTile)
			{
				((UWEnergyTile)tileEntity).setEnergyStored(energy);
			}
		}
	}
	
	@Override
	public void syncFluid(BlockPos pos, CompoundNBT tankTag)
	{
		World world = getClientWorld();
		if(world.isBlockLoaded(pos))
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			
			if(tileEntity instanceof UWBaseTileEntity)
			{
				((UWBaseTileEntity)tileEntity).setStoredFluid(tankTag);
			}
		}
	}
}
