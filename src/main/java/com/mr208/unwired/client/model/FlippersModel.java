package com.mr208.unwired.client.model;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class FlippersModel extends BipedModel
{
	public RendererModel base_left;
	public RendererModel base_right;
	public RendererModel flipper_left;
	public RendererModel flipper_right;
	
	public float[] colorArray;
	
	public FlippersModel()
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
		base_left.addBox(0f,0f,0f, 5,3,5);
		base_left.setRotationPoint(-2.5f, 9.0f, -2.5f);
		setRotation(base_left, 0,0,0);
		
		flipper_left = new RendererModel(this,0,16);
		flipper_left.addBox(0,0,0,5,1,6);
		flipper_left.setRotationPoint(-2.5f,10.75f,-8f);
		setRotation(flipper_left,0,0,0);
		
		base_right = new RendererModel(this,0,0);
		base_right.mirror = true;
		base_right.addBox(0f,0f,0f, 5,3,5);
		base_right.setRotationPoint(-2.5f,9.0f,-2.5f);
		setRotation(base_right,0,0,0);
		
		flipper_right = new RendererModel(this,0,16);
		flipper_right.mirror = true;
		flipper_right.addBox(0,0,0,5,1,6);
		flipper_right.setRotationPoint(-2.5f,10.75f,-8f);
		setRotation(flipper_right,0,0,0);
		
		this.bipedLeftLeg.addChild(base_left);
		this.bipedLeftLeg.addChild(flipper_left);
		this.bipedRightLeg.addChild(base_right);
		this.bipedRightLeg.addChild(flipper_right);
		
	}
	
	@Override
	public void render(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color4f( colorArray[0], colorArray[1], colorArray[2], 1f);
		
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		
		GlStateManager.color4f(1F,1F,1F,1F);
		GlStateManager.disableBlend();
	}
	
	public void setColorArray(int color)
	{
		this.colorArray= new Color(color).getRGBColorComponents(null);
	}
	
	private void setRotation(RendererModel model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
