package com.mr208.unwired.client.render;

import com.mr208.unwired.client.ClientSetup;
import com.mr208.unwired.common.tile.PolymerizedLogTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraftforge.client.model.animation.TileEntityRendererFast;

public class PolymerizedLogTESR extends TileEntityRendererFast<PolymerizedLogTile>
{
	@Override
	public void renderTileEntityFast(PolymerizedLogTile te, double x, double y, double z, float partialTicks, int damageStage, BufferBuilder buffer)
	{
		if(te.getCachedState()!=null)
		{
			BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
			
			BlockState cachedState = te.getCachedState();
			
			IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(te.getCachedState());
			BlockPos pos = te.getPos();
			
			buffer.setTranslation(x,y,z);
			buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
			int skyLight = getWorld().getLightFor(LightType.SKY, pos);
			int blockLight = getWorld().getLightFor(LightType.BLOCK, pos);
			buffer.lightmap(skyLight,blockLight);
			dispatcher.getBlockModelRenderer().renderModelFlat(getWorld(), model, cachedState, te.getPos(), buffer, true, ClientSetup.rand,ClientSetup.rand.nextLong(),te.getModelData());
			buffer.setTranslation(0,0,0);
		}
	}
}
