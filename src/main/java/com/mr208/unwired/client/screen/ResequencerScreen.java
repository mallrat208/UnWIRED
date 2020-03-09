package com.mr208.unwired.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.crafting.recipes.ResequencerRecipe;
import com.mr208.unwired.common.inventory.container.ResequencerContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class ResequencerScreen extends ContainerScreen<ResequencerContainer>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(UnWIRED.MOD_ID,"textures/gui/container/resequencer.png");
	private float sliderProgress;
	private boolean isRecipeClicked;
	private int recipeIndexOffset;
	private boolean hasInput;
	private int selectionBoxXOffset = 1;
	private int selectionBoxYOffset= 3;
	
	public ResequencerScreen(ResequencerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
		screenContainer.setInventoryUpdateListener(this::func_214145_d);
	}
	
	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_)
	{
		super.render(p_render_1_, p_render_2_, p_render_3_);
		this.renderHoveredToolTip(p_render_1_, p_render_2_);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.font.drawString(this.title.getFormattedText(), 9f, 8f, 1);
		//this.font.drawString(this.title.getFormattedText(), 9f, 8f, 4210752);
		//this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8f, (float) (this.ySize - 94), 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.renderBackground();
		GlStateManager.color4f(1f, 1f, 1f, 1f);
		getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
		int k = (int) (41.0F * this.sliderProgress);
		this.blit(i + 119, selectionBoxYOffset+ j + 15 + k, 176 + (this.canScroll() ? 0 : 12), 0, 12, 15);
		int l = this.guiLeft + 52 + selectionBoxXOffset;
		int i1 = this.guiTop + 14 +selectionBoxYOffset;
		int j1 = this.recipeIndexOffset + 12;
		this.func_214141_a(mouseX, mouseY, l, i1, j1);
		this.func_214142_b(l, i1, j1);
	}
	
	private void func_214141_a(int mouseX, int mouseY, int p_214141_3_, int p_214141_4_, int p_214141_5_) {
		for (int i = this.recipeIndexOffset; i < p_214141_5_ && i < this.container.getRecipeListSize(); ++i) {
			int j = i - this.recipeIndexOffset;
			int k = p_214141_3_ + j % 4 * 16;
			int l = j / 4;
			int i1 = p_214141_4_ + l * 18 + 2;
			int j1 = this.ySize;
			if (i == this.container.func_217073_e()) {
				j1 += 18;
			} else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
				j1 += 36;
			}
			
			this.blit(k, i1 - 1, 0, j1, 16, 18);
		}
	}
	
	private void func_214142_b(int p_214142_1_, int p_214142_2_, int p_214142_3_) {
		RenderHelper.enableGUIStandardItemLighting();
		List<ResequencerRecipe> list = this.container.getRecipeList();
		
		for (int i = this.recipeIndexOffset; i < p_214142_3_ && i < this.container.getRecipeListSize(); ++i) {
			int j = i - this.recipeIndexOffset;
			int k = p_214142_1_ + j % 4 * 16;
			int l = j / 4;
			int i1 = p_214142_2_ + l * 18 + 2;
			this.minecraft.getItemRenderer().renderItemAndEffectIntoGUI(list.get(i).getRecipeOutput(), k, i1);
			
			if(this.container.inventorySlots.get(0).getStack().getCount() < this.getContainer().getRecipeList().get(i).getIngredients().get(0).getMatchingStacks()[0].getCount())
			{
				this.minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(Items.BARRIER), k, i1);
			}
		}
		
		RenderHelper.disableStandardItemLighting();
	}
	
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		this.isRecipeClicked = false;
		if (this.hasInput) {
			int i = this.guiLeft + 52 + selectionBoxXOffset;
			int j = this.guiTop + 14 + selectionBoxYOffset;
			int k = this.recipeIndexOffset + 12;
			
			for (int l = this.recipeIndexOffset; l < k; ++l) {
				int i1 = l - this.recipeIndexOffset;
				double d0 = p_mouseClicked_1_ - (double) (i + i1 % 4 * 16);
				double d1 = p_mouseClicked_3_ - (double) (j + i1 / 4 * 18);
				if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.container.enchantItem(getMinecraft().player, l)) {
					
					Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
					getMinecraft().playerController.sendEnchantPacket((this.container).windowId, l);
					return true;
				}
			}
			
			i = this.guiLeft + 119;
			j = this.guiTop + 9;
			if (p_mouseClicked_1_ >= (double) i && p_mouseClicked_1_ < (double) (i + 12) && p_mouseClicked_3_ >= (double) j && p_mouseClicked_3_ < (double) (j + 54)) {
				this.isRecipeClicked = true;
			}
		}
		
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
	
	@Override
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		if (this.isRecipeClicked && this.canScroll()) {
			int i = this.guiTop + 14 + selectionBoxYOffset;
			int j = i + 54;
			this.sliderProgress = ((float) p_mouseDragged_3_ - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
			this.sliderProgress = MathHelper.clamp(this.sliderProgress, 0.0F, 1.0F);
			this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) this.getHiddenRows()) + 0.5D) * 4;
			return true;
		} else {
			return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
		}
	}
	
	@Override
	public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
		if (this.canScroll()) {
			int i = this.getHiddenRows();
			this.sliderProgress = (float) ((double) this.sliderProgress - p_mouseScrolled_5_ / (double) i);
			this.sliderProgress = MathHelper.clamp(this.sliderProgress, 0.0F, 1.0F);
			this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) i) + 0.5D) * 4;
		}
		
		return true;
	}
	
	private boolean canScroll() {
		return this.hasInput && this.container.getRecipeListSize() > 12;
	}
	
	private int getHiddenRows() {
		return (this.container.getRecipeListSize() + 4 - 1) / 4 - 3;
	}
	
	private void func_214145_d() {
		this.hasInput = this.container.func_217083_h();
		if (!this.hasInput) {
			this.sliderProgress = 0f;
			this.recipeIndexOffset = 0;
		}
	}
}
