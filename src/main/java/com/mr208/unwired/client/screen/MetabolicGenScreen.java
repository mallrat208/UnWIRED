package com.mr208.unwired.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.inventory.MetabolicGenContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MetabolicGenScreen extends ContainerScreen<MetabolicGenContainer>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(UnWIRED.MOD_ID,"textures/gui/container/generic_screen.png");
	
	public MetabolicGenScreen(MetabolicGenContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
		
		
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString(this.title.getFormattedText(),9f,8f,1);
		//this.font.drawString("Energy: " + container.getEnergy(), 10, 10, 0xffffff);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
	}
	

}
