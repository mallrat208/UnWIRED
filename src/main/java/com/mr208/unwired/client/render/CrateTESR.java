package com.mr208.unwired.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.LogicOp;
import com.mr208.unwired.common.tile.StorageCrateTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.RenderComponentsUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class CrateTESR extends TileEntityRenderer<StorageCrateTile>
{
	public CrateTESR() {}
	
	@Override
	public void render(StorageCrateTile crateTile, double posX, double posY, double posZ, float partialTicks, int destroyCount)
	{
		BlockState blockState = crateTile.getBlockState();
		GlStateManager.pushMatrix();
		
		GlStateManager.translatef((float)posX + 0.5F, (float)posY + 0.5F, (float)posZ + 0.5F);
		GlStateManager.rotatef(-(blockState.get(BlockStateProperties.HORIZONTAL_FACING)).getOpposite().getHorizontalAngle(), 0.0F, 1.0F, 0F);
		GlStateManager.translatef(0.0F, -1.50f, .40f);
		
		float lvt_11_1_ = 0.6666667F;
		
		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();
		GlStateManager.scalef(0.6666667F, -0.6666667F, -0.6666667F);
		GlStateManager.popMatrix();
		FontRenderer fontRenderer = this.getFontRenderer();
		float scale = 0.010416667F;
		GlStateManager.translatef(0.0F, 1.33333334F, 0.046666667F);
		GlStateManager.scalef(scale, -scale, scale);
		GlStateManager.normal3f(0.0F, 0.0F, -0.010416667F);
		GlStateManager.depthMask(false);
		int fontColor = crateTile.getTextColor().getTextColor();
		if (destroyCount < 0) {
			for(int i = 0; i < crateTile.getLabelRows(); ++i) {
				String renderString = crateTile.getRenderText(i, (textComponent) -> {
					List<ITextComponent> textComponentList = RenderComponentsUtil.splitText(textComponent, 90, fontRenderer, false, true);
					return textComponentList.isEmpty() ? "" : textComponentList.get(0).getFormattedText();
				});
				if (renderString != null) {
					fontRenderer.drawString(renderString, (float)(-fontRenderer.getStringWidth(renderString) / 2), (float)(i * 10 - crateTile.labelText.length * 5), fontColor);
					if (i == crateTile.getLineBeingEdited() && crateTile.currentLength() >= 0) {
						int lvt_17_1_ = fontRenderer.getStringWidth(renderString.substring(0, Math.max(Math.min(crateTile.currentLength(), renderString.length()), 0)));
						int lvt_18_1_ = fontRenderer.getBidiFlag() ? -1 : 1;
						int textPosX = (lvt_17_1_ - fontRenderer.getStringWidth(renderString) / 2) * lvt_18_1_;
						int textPosY = i * 10 - crateTile.labelText.length * 5;
						int var10001;
						if (crateTile.isEditing()) {
							if (crateTile.currentLength() < renderString.length()) {
								var10001 = textPosY - 1;
								int var10002 = textPosX + 1;
								AbstractGui.fill(textPosX, var10001, var10002, textPosY + 9, -16777216 | fontColor);
							} else {
								fontRenderer.drawString("_", (float)textPosX, (float)textPosY, fontColor);
							}
						}
					}
				}
			}
		}
		
		GlStateManager.depthMask(true);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
	
	private void func_217657_a(int p_217657_1_, int p_217657_2_, int p_217657_3_, int p_217657_4_) {
		this.bindTexture(new ResourceLocation("unwired","textures/block/frame_polymer.png"));
		Tessellator lvt_5_1_ = Tessellator.getInstance();
		BufferBuilder lvt_6_1_ = lvt_5_1_.getBuffer();
		GlStateManager.color4f(0.0F, 0.0F, 255.0F, 255.0F);
		GlStateManager.disableTexture();
		GlStateManager.enableColorLogicOp();
		GlStateManager.logicOp(LogicOp.OR_REVERSE);
		lvt_6_1_.begin(7, DefaultVertexFormats.POSITION);
		lvt_6_1_.pos((double)p_217657_1_, (double)p_217657_4_, 0.0D).endVertex();
		lvt_6_1_.pos((double)p_217657_3_, (double)p_217657_4_, 0.0D).endVertex();
		lvt_6_1_.pos((double)p_217657_3_, (double)p_217657_2_, 0.0D).endVertex();
		lvt_6_1_.pos((double)p_217657_1_, (double)p_217657_2_, 0.0D).endVertex();
		lvt_5_1_.draw();
		GlStateManager.disableColorLogicOp();
		GlStateManager.enableTexture();
	}
}
