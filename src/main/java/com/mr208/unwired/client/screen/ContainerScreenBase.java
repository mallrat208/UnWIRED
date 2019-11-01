package com.mr208.unwired.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mr208.unwired.Config;
import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.inventory.UWSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public abstract class ContainerScreenBase<T extends Container> extends ContainerScreen<T>
{
	private static final ResourceLocation BACKGROUND_TECH=new ResourceLocation(UnWIRED.MOD_ID, "textures/gui/container/generic_screen.png");
	private static final ResourceLocation BACKGROUND_GRAY= new ResourceLocation(UnWIRED.MOD_ID, "textures/gui/container/generic_screen_grey.png");
	
	public ContainerScreenBase(T screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	protected ResourceLocation getBackgroundTexture()
	{
		return Config.USE_GRAY_BACKGROUND.get() ? BACKGROUND_GRAY: BACKGROUND_TECH;
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
		if(hoveredSlot instanceof UWSlot.Food&&!hoveredSlot.getHasStack())
		{
			ITextComponent textComponent=new TranslationTextComponent("tooltip.unwired.slot.food");
			renderTooltip(textComponent.getFormattedText(), mouseX, mouseY);
		} else if(hoveredSlot instanceof UWSlot.Charge&&!hoveredSlot.getHasStack())
		{
			ITextComponent textComponent=new TranslationTextComponent("tooltip.unwired.slot.charge");
			renderTooltip(textComponent.getFormattedText(), mouseX, mouseY);
		} else if(hoveredSlot instanceof UWSlot.Output&&!hoveredSlot.getHasStack())
		{
			ITextComponent textComponent=new TranslationTextComponent("tooltip.unwired.slot.output");
			renderTooltip(textComponent.getFormattedText(), mouseX, mouseY);
		} else if(hoveredSlot instanceof UWSlot.Fluid&&!hoveredSlot.getHasStack())
		{
			ITextComponent textComponent = new TranslationTextComponent("tooltip.unwired.slot.fluid");
			renderTooltip(textComponent.getFormattedText(), mouseX,mouseY);
		}
		
		super.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		renderContainerTitle();
	}
	
	protected void renderContainerTitle()
	{
		this.font.drawString(this.title.getFormattedText(), 45f, 10f, 1);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float v, int i, int i1)
	{
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(getBackgroundTexture());
		int relX=(this.width-this.xSize)/2;
		int relY=(this.height-this.ySize)/2;
		this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
		
		for(Slot slot : container.inventorySlots)
		{
			if(slot.isEnabled()&&slot instanceof UWSlot)
				if(slot instanceof UWSlot.Charge)
					blit(relX+slot.xPos-1, relY+slot.yPos-1, 18, 166, 18, 18);
				else if(slot instanceof UWSlot.Food)
					blit(relX+slot.xPos-1, relY+slot.yPos-1, 36, 166, 18, 18);
				else if(slot instanceof UWSlot.Fluid)
					blit(relX+slot.xPos-1, relY+slot.yPos-1, 54, 166, 18, 18);
				else
					blit(relX+slot.xPos-1, relY+slot.yPos-1, 0, 166, 18, 18);
		}
	}
	
	protected boolean isCursorOnBar(int posX, int posY, int mouseX, int mouseY)
	{
		return isPointInRegion((posX), (posY), 18, 48, mouseX, mouseY);
	}
	
	protected boolean isCursorOnProgress(int posX, int posY, int mouseX, int mouseY)
	{
		return isPointInRegion(posX, posY, 5,18, mouseX, mouseY);
	}
	
	protected void drawEnergyBar(int posX, int posY, float percentage)
	{
		int relX=(this.width-this.xSize)/2;
		int relY=(this.height-this.ySize)/2;
		
		this.blit(relX+posX, relY+posY, 176, 0, 18, 48);
		this.blit(relX+posX, relY+posY+(int)(48-(48*percentage)), 194, 0, 18, (int)(48*percentage));
	}
	
	protected void drawProgressBar(int posX, int posY, float percentage)
	{
		int relX = (this.width-this.xSize)/2;
		int relY = (this.height-this.ySize)/2;
		
		this.blit(relX + posX, relY+posY, 176,48, 5, 18);
		this.blit(relX+posX, relY+posY+(int)(18-(18*percentage)), 181,48, 5,(int)(18*percentage));
	}
	
	protected void drawFluidTank(int posX, int posY, LazyOptional<IFluidHandler> handler, int tank)
	{
		int relX=(this.width-this.xSize)/2;
		int relY=(this.height-this.ySize)/2;
		
		FluidStack fluidTank = handler.map(iFluidHandler -> iFluidHandler.getFluidInTank(tank)).orElse(FluidStack.EMPTY);
		
		float percentage=(float)fluidTank.getAmount()/(float)handler.map(iFluidHandler->iFluidHandler.getTankCapacity(tank)).orElse(0);
		this.blit(relX+posX, relY+posY, 176, 0, 18, 48);
		
		if(!fluidTank.isEmpty())
		{
			TextureAtlasSprite sprite=Minecraft.getInstance().getTextureMap().getSprite(fluidTank.getFluid().getAttributes().getStillTexture());
			
			int col=fluidTank.getFluid().getAttributes().getColor(fluidTank);
			
			GlStateManager.pushMatrix();
			Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.color3f((col >> 16&255)/255.0f, (col >> 8&255)/255.0f, (col&255)/255.0f);
			//TODO: Redo to tile the sprite to remove stretching
			blit(relX+posX+1, relY+posY+1+(int)(46-(46*percentage)), 0, sprite.getWidth(), (int)(46*percentage), sprite);
			GlStateManager.popMatrix();
			GlStateManager.color3f(1f, 1f, 1f);
			
			Minecraft.getInstance().textureManager.bindTexture(getBackgroundTexture());
		}
		this.blit(relX+posX, relY+posY, 212, 0, 18, 48);
	}
}
