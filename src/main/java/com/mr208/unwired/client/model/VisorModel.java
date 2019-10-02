package com.mr208.unwired.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;

public class VisorModel extends BipedModel
{
	private RendererModel lens;
	private RendererModel back_frame;
	private RendererModel arm_1;
	private RendererModel arm_2;
	private RendererModel lens_mount;
	
	public VisorModel()
	{
		this.textureWidth = 32;
		this.textureHeight = 32;
		
		bipedHeadwear.cubeList.clear();
		bipedHead.cubeList.clear();
		bipedBody.cubeList.clear();
		bipedLeftArm.cubeList.clear();
		bipedLeftLeg.cubeList.clear();
		bipedRightArm.cubeList.clear();
		bipedRightLeg.cubeList.clear();
		
		
		this.back_frame = new RendererModel(this, 0, 0);
		this.back_frame.setRotationPoint(-4.5F, -5.0F, 3.5F);
		this.back_frame.addBox(0.0F, 0.0F, 0.0F, 9, 1, 1, 0.0F);
		this.setRotation(back_frame, 0,0,0);
		
		this.arm_1 = new RendererModel(this, 12, 4);
		this.arm_1.setRotationPoint(3.5F, -5.0F, -1.5F);
		this.arm_1.addBox(0.0F, 0.0F, 0.0F, 1, 1, 5, 0.0F);
		this.setRotation(arm_1, 0,0,0);
		
		this.arm_2 = new RendererModel(this, 1, 3);
		this.arm_2.setRotationPoint(-4.5F, -5.0F, -4.5F);
		this.arm_2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 8, 0.0F);
		this.setRotation(arm_2, 0,0,0);
		
		this.lens_mount = new RendererModel(this, 12, 4);
		this.lens_mount.setRotationPoint(-4.5F, -5.0F, -5.0F);
		this.lens_mount.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.setRotation(lens_mount, 0,0,0);
		
		this.lens = new RendererModel(this, 0, 3);
		this.lens.setRotationPoint(-3.5F, -5.0F, -5.0F);
		this.lens.addBox(0.0F, 0.0F, 0.5F, 3, 3, 1, 0.0F);
		this.setRotation(lens, 0,0,0);
		
		bipedHead.addChild(back_frame);
		bipedHead.addChild(arm_1);
		bipedHead.addChild(arm_2);
		bipedHead.addChild(lens_mount);
		bipedHead.addChild(lens);
	}
	
	
	
	/*
	public void render(float scale, ItemStack itemStack)
	{
		float[] colors = new float[]{0,0,0};
		
		if(itemStack.getItem() instanceof IColorableEquipment)
		{
			colors = ((IColorableEquipment)itemStack.getItem()).getColorFloat(itemStack);
		}
		
		back_frame.render(scale);
		arm_1.render(scale);
		lens_mount.render(scale);
		arm_2.render(scale);
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color( colors[0], colors[1], colors[2], 0.40F);
		lens.render(scale);
		GlStateManager.color(1F,1F,1F,1F);
		
	}
	*/
	
	private void setRotation(RendererModel model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
