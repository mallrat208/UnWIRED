package com.mr208.unwired.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class VisorModel<T extends LivingEntity> extends BipedModel<T>
{
	private ModelRenderer lens;
	private ModelRenderer back_frame;
	private ModelRenderer arm_1;
	private ModelRenderer arm_2;
	private ModelRenderer lens_mount;
	
	private float[] colorArray;
	
	public void setColorArray(int color)
	{
		this.colorArray= new Color(color).getRGBColorComponents(null);
	}
	
	public VisorModel()
	{
		
		super(0.0f);
		
		this.textureWidth = 32;
		this.textureHeight = 32;
	
		this.back_frame = new ModelRenderer(this, 0, 0);
		this.back_frame.setRotationPoint(-4.5F, -5.0F, 3.5F);
		this.back_frame.addBox(0.0F, 0.0F, 0.0F, 9, 1, 1, 0.0F);
		this.setRotation(back_frame, 0,0,0);
		
		this.arm_1 = new ModelRenderer(this, 12, 4);
		this.arm_1.setRotationPoint(3.5F, -5.0F, -1.5F);
		this.arm_1.addBox(0.0F, 0.0F, 0.0F, 1, 1, 5, 0.0F);
		this.setRotation(arm_1, 0,0,0);
		
		this.arm_2 = new ModelRenderer(this, 1, 3);
		this.arm_2.setRotationPoint(-4.5F, -5.0F, -4.5F);
		this.arm_2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 8, 0.0F);
		this.setRotation(arm_2, 0,0,0);
		
		this.lens_mount = new ModelRenderer(this, 12, 4);
		this.lens_mount.setRotationPoint(-4.5F, -5.0F, -5.0F);
		this.lens_mount.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.setRotation(lens_mount, 0,0,0);
		
		this.lens = new ModelRenderer(this, 0, 3);
		this.lens.setRotationPoint(-3.5F, -5.0F, -5.0F);
		this.lens.addBox(0.0F, 0.0F, 0.5F, 3, 3, 1, 0.0F);
		this.setRotation(lens, 0,0,0);
		
		this.bipedHead.addChild(back_frame);
		this.bipedHead.addChild(arm_1);
		this.bipedHead.addChild(arm_2);
		this.bipedHead.addChild(lens_mount);
		this.bipedHead.addChild(lens);
	}
	
	@Override
	public void render(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
	{
		lens.showModel = true;
		arm_1.showModel = false;
		arm_2.showModel = false;
		lens_mount.showModel = false;
		back_frame.showModel = false;
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color4f( colorArray[0], colorArray[1], colorArray[2], 0.4F);
		
		super.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
		
		lens.showModel = false;
		arm_1.showModel = true;
		arm_2.showModel = true;
		lens_mount.showModel = true;
		back_frame.showModel = true;
		
		GlStateManager.color4f(1F,1F,1F,1F);
		GlStateManager.disableBlend();
		
		super.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
		
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
