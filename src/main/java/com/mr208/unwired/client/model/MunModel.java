package com.mr208.unwired.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;

public class MunModel extends BipedModel
{
	public RendererModel base_left;
	public RendererModel base_right;
	
	public MunModel()
	{
		this.textureHeight = 32;
		this.textureWidth = 32;
		
		bipedHeadwear.cubeList.clear();
		bipedHead.cubeList.clear();
		bipedBody.cubeList.clear();
		bipedLeftArm.cubeList.clear();
		bipedLeftLeg.cubeList.clear();
		bipedRightArm.cubeList.clear();
		bipedRightLeg.cubeList.clear();
		
		base_left = new RendererModel(this,0,0);
		base_left.addBox(0f,0f,0f, 5,6,5);
		base_left.setRotationPoint(-2.5f, 6.5f, -2.5f);
		setRotation(base_left, 0,0,0);
		
		base_right = new RendererModel(this,0,0);
		base_right.mirror = true;
		base_right.addBox(0f,0f,0f, 5,6,5);
		base_right.setRotationPoint(-2.5f,6.5f,-2.5f);
		setRotation(base_right,0,0,0);
		
		this.bipedLeftLeg.addChild(base_left);
		this.bipedRightLeg.addChild(base_right);
	}
	
	private void setRotation(RendererModel model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
