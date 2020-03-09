package com.mr208.unwired.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

import java.awt.*;

public class FlippersModel<T extends LivingEntity> extends BipedModel<T>
{
	public ModelRenderer base_left;
	public ModelRenderer base_right;
	public ModelRenderer flipper_left;
	public ModelRenderer flipper_right;
	
	public float[] colorArray;
	
	public FlippersModel()
	{
		super(0.0f);
		
		this.textureHeight = 32;
		this.textureWidth = 32;
		
		base_left = new ModelRenderer(this,0,0);
		base_left.addBox(0f,0f,0f, 5,3,5);
		base_left.setRotationPoint(-2.5f, 9.0f, -2.5f);
		setRotation(base_left, 0,0,0);
		
		flipper_left = new ModelRenderer(this,0,16);
		flipper_left.addBox(0,0,0,5,1,6);
		flipper_left.setRotationPoint(-2.5f,10.75f,-8f);
		setRotation(flipper_left,0,0,0);
		
		base_right = new ModelRenderer(this,0,0);
		base_right.mirror = true;
		base_right.addBox(0f,0f,0f, 5,3,5);
		base_right.setRotationPoint(-2.5f,9.0f,-2.5f);
		setRotation(base_right,0,0,0);
		
		flipper_right = new ModelRenderer(this,0,16);
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
	protected Iterable<ModelRenderer> getHeadParts()
	{
		return ImmutableList.of();
	}
	
	@Override
	protected Iterable<ModelRenderer> getBodyParts()
	{
		return ImmutableList.of(this.base_left, this.base_right, this.flipper_left, this.flipper_right);
	}
	
	public void setColorArray(int color)
	{
		this.colorArray= new Color(color).getRGBColorComponents(null);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
