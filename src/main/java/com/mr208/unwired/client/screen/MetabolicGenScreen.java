package com.mr208.unwired.client.screen;

import com.mr208.unwired.common.inventory.MetabolicGenContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;


public class MetabolicGenScreen extends ContainerScreenBase<MetabolicGenContainer>
{
	public MetabolicGenScreen(MetabolicGenContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		super.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY)
	{
		if(isCursorOnBar(9,10,mouseX, mouseY))
		{
			ITextComponent textComponent=new TranslationTextComponent("tooltip.unwired.energy", container.getEnergyStored(), container.getMaxEnergy());
			renderTooltip(textComponent.getFormattedText(), mouseX, mouseY);
		}
		else if(isCursorOnProgress(102,35, mouseX, mouseY))
		{
			ITextComponent textComponent = new TranslationTextComponent("tooltip.unwired.progress", container.getProgressPercentage());
			renderTooltip(textComponent.getFormattedText(), mouseX, mouseY);
		}
		
		super.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		drawEnergyBar(9,10,container.getEnergyPercentage());
		drawProgressBar(102,35, ((float)container.getProgressPercentage()/(float)100));
	}
}
