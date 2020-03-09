package com.mr208.unwired.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class RebreatherModel<T extends LivingEntity> extends BipedModel<T>
{
	public ModelRenderer base;
	public ModelRenderer rebreather;
	
	public RebreatherModel()
	{
		super(0.0f);
		
		this.textureWidth = 32;
		this.textureHeight = 32;
		
		base = new ModelRenderer(this,0,0);
		base.addBox(0f,0f,0f, 3,2,2);
		base.setRotationPoint(-1.5f,-2.5f,-5f);

		rebreather = new ModelRenderer(this, 0,16);
		rebreather.addBox(0,0,0,6,1,1);
		rebreather.setRotationPoint(-3f,-1.5f,-4.75f);
		setRotation(rebreather,.75f,0f,0f);
		
		bipedHead.addChild(rebreather);
		bipedHead.addChild(base);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
