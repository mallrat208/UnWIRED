package com.mr208.unwired.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.inventory.UWSlot;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class ContainerScreenBase<T extends Container> extends ContainerScreen<T>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(UnWIRED.MOD_ID,"textures/gui/container/generic_screen.png");
	
	
	
	public ContainerScreenBase(T screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY)
	{
		if(hoveredSlot instanceof UWSlot.Food && !hoveredSlot.getHasStack())
		{
			ITextComponent textComponent = new TranslationTextComponent("tooltip.unwired.slot.food");
			renderTooltip(textComponent.getFormattedText(), mouseX, mouseY);
		}
		else if(hoveredSlot instanceof UWSlot.Charge && !hoveredSlot.getHasStack())
		{
			ITextComponent textComponent = new TranslationTextComponent("tooltip.unwired.slot.charge");
			renderTooltip(textComponent.getFormattedText(), mouseX, mouseY);
		}
		else if(hoveredSlot instanceof UWSlot.Output && !hoveredSlot.getHasStack())
		{
			ITextComponent textComponent = new TranslationTextComponent("tooltip.unwired.slot.output");
			renderTooltip(textComponent.getFormattedText(), mouseX, mouseY);
		}
		
		super.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.font.drawString(this.title.getFormattedText(),45f,10f,1);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float v, int i, int i1)
	{
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
		
		for(Slot slot:container.inventorySlots)
		{
			if(slot.isEnabled() && slot instanceof UWSlot)
				if(slot instanceof UWSlot.Charge)
					blit(relX + slot.xPos-1, relY+ slot.yPos-1, 18,166,18,18);
				else if(slot instanceof UWSlot.Food)
					blit(relX + slot.xPos-1, relY+ slot.yPos-1, 36,166,18,18);
				else
					blit(relX + slot.xPos-1, relY+ slot.yPos-1, 0,166,18,18);
		}
	}
	
	protected boolean isCursorOnEnergyBar(int posX, int posY, int mouseX, int mouseY)
	{
		return isPointInRegion((posX), (posY), 18, 49, mouseX, mouseY);
	}
	
	protected void drawEnergyBar(int posX, int posY, float percentage)
	{
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		
		this.blit(relX + posX, relY + posY,176,0,18, 49);
		this.blit(relX + posX, relY + posY + (int)(49 - (49 * percentage)),194,0,18, (int)(49 * percentage));
	}
}
