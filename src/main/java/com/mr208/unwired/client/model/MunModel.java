package com.mr208.unwired.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class MunModel<T extends LivingEntity> extends BipedModel<T>
{
	public ModelRenderer base_left;
	public ModelRenderer base_right;
	
	public MunModel()
	{
		super(0.0f);
		this.textureHeight = 32;
		this.textureWidth = 32;
		
		base_left = new ModelRenderer(this,0,0);
		base_left.addBox(0f,0f,0f, 5,6,5);
		base_left.setRotationPoint(-2.5f, 6.5f, -2.5f);
		setRotation(base_left, 0,0,0);
		
		base_right = new ModelRenderer(this,0,0);
		base_right.mirror = true;
		base_right.addBox(0f,0f,0f, 5,6,5);
		base_right.setRotationPoint(-2.5f,6.5f,-2.5f);
		setRotation(base_right,0,0,0);
		
		this.bipedLeftLeg.addChild(base_left);
		this.bipedRightLeg.addChild(base_right);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
