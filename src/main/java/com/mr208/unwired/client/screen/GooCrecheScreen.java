package com.mr208.unwired.client.screen;

import com.google.common.collect.Lists;
import com.mr208.unwired.common.inventory.GooCrecheContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class GooCrecheScreen extends ContainerScreenBase<GooCrecheContainer>
{
	public GooCrecheScreen(GooCrecheContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY)
	{
		if(isCursorOnBar(9,10,mouseX, mouseY))
		{
			ITextComponent textComponent = new TranslationTextComponent("tooltip.unwired.energy", container.getEnergyStored(), container.getMaxEnergyStored());
			renderTooltip(textComponent.getFormattedText(), mouseX, mouseY);
		}
		else if(isCursorOnBar(149,10, mouseX, mouseY))
		{
			FluidStack fluidStack = container.getFluidTankStack(0);
			ITextComponent textComponent = new TranslationTextComponent("tooltip.unwired.fluid", fluidStack.getAmount(), container.getFluidTankCapacity(0));
			renderTooltip(Lists.newArrayList(fluidStack.isEmpty() ? new TranslationTextComponent("tooltip.unwired.fluid.empty").getFormattedText() : fluidStack.getDisplayName().getFormattedText(), textComponent.getFormattedText()), mouseX, mouseY);
		}
		
		super.renderHoveredToolTip(mouseX, mouseY);	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float v, int i, int i1)
	{
		super.drawGuiContainerBackgroundLayer(v, i, i1);
		
		drawEnergyBar(9,10, container.getEnergyPercentage());
		drawFluidTank(149, 10, container.getTileCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY),0);
	}
	
}
