package com.mr208.unwired.client.screen;

import com.google.common.collect.Lists;
import com.mr208.unwired.common.inventory.FluidDrumContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class FluidDrumScreen extends ContainerScreenBase<FluidDrumContainer>
{
	int tankX = 80;
	int tankY = 28;
	
	public FluidDrumScreen(FluidDrumContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY)
	{
		if(isCursorOnBar(tankX, tankY, mouseX, mouseY))
		{
			FluidStack fluidStack = container.getFluidTankStack(0);
			ITextComponent textComponent = new TranslationTextComponent("tooltip.unwired.fluid", fluidStack.getAmount(), container.getFluidTankCapacity(0));
			renderTooltip(Lists.newArrayList(fluidStack.isEmpty() ? new TranslationTextComponent("tooltip.unwired.fluid.empty").getFormattedText() : fluidStack.getDisplayName().getFormattedText(), textComponent.getFormattedText()), mouseX, mouseY);
		}
		
		super.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void renderContainerTitle()
	{
		this.font.drawString(this.title.getFormattedText(), 9f, 8f, 1);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float v, int i, int i1)
	{
		super.drawGuiContainerBackgroundLayer(v, i, i1);
		
		drawFluidTank(tankX,tankY, container.getTileCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY),0);
	}
}
