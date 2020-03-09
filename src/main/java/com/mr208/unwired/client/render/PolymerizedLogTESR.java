package com.mr208.unwired.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mr208.unwired.common.tile.PolymerizedLogTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

public class PolymerizedLogTESR extends TileEntityRenderer<PolymerizedLogTile>
{
	public PolymerizedLogTESR(TileEntityRendererDispatcher p_i226006_1_)
	{
		super(p_i226006_1_);
	}
	
	@Override
	public void render(PolymerizedLogTile polymerizedLogTile, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, int i1)
	{
		if(polymerizedLogTile.getCachedState()!=null)
		{
			BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
			
			BlockState cachedState = polymerizedLogTile.getCachedState();
			
			IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(polymerizedLogTile.getCachedState());
			BlockPos pos = polymerizedLogTile.getPos();
			
			matrixStack.translate(pos.getX(),pos.getY(),pos.getZ());
			int skyLight = polymerizedLogTile.getWorld().getLightFor(LightType.SKY, pos);
			int blockLight = polymerizedLogTile.getWorld().getLightFor(LightType.BLOCK, pos);
			//matrixStack.(skyLight,blockLight);
			//dispatcher.getBlockModelRenderer().renderModelFlat(polymerizedLogTile.getWorld(), model, cachedState, polymerizedLogTile.getPos(), iRenderTypeBuffer, true, ClientSetup.rand,ClientSetup.rand.nextLong(),polymerizedLogTile.getModelData());
			matrixStack.translate(0,0,0);
	}
	}
}
