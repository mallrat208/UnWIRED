package tabsapi;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractTab extends Button
{
	private static Minecraft mc = Minecraft.getInstance();
	
	ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	ItemStack renderStack;
	public int potionOffsetLast;
	public int id = 0;
	protected ItemRenderer itemRenderer;
	
	public AbstractTab(ItemStack renderStack)
	{
		super(0, 0, 28, 32, "", Button::onPress);
		
		this.renderStack = renderStack;
		this.itemRenderer = Minecraft.getInstance().getItemRenderer();
	}
	
	@Override
	public void renderButton(int mouseX, int mouseY, float partial)
	{
		
		int newPotionOffset = 0;
		//TODO: REI/JEI/NEI OFFSET
		//int newPotionOffset = TabRegistry.getPotionOffsetNEI();
		
		Screen screen = Minecraft.getInstance().currentScreen;
		
		if(screen instanceof InventoryScreen)
		{
			newPotionOffset += TabRegistry.getRecipeBookOffset((InventoryScreen) screen) - TabRegistry.recipeBookOffset;
		}
		if(newPotionOffset != this.potionOffsetLast)
		{
			this.x += newPotionOffset - this.potionOffsetLast;
			this.potionOffsetLast = newPotionOffset;
		}
		
		if(this.visible)
		{
			GlStateManager.color4f(1f,1f,1f,1f);
			
			int yTexPos = this.active ? 3 : 32;
			int ySize = this.active ? 25 : 32;
			int xOffset = this.id == 2 ? 0 : 1;
			int yPos = this.y + (this.active ? 3 : 0);

			mc.textureManager.bindTexture(this.texture);
			this.blit(this.x, yPos, xOffset * 28, yTexPos, 28, ySize);
			
			RenderHelper.enableGUIStandardItemLighting();
			this.blitOffset = 100;
			this.itemRenderer.zLevel = 100.0F;
			GlStateManager.enableLighting();
			GlStateManager.enableRescaleNormal();
			this.itemRenderer.renderItemAndEffectIntoGUI(this.renderStack, this.x + 6, this.y + 8);
			this.itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, this.renderStack, this.x + 6, this.y + 8, null);
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			this.itemRenderer.zLevel = 0.0F;
			this.blitOffset = 0;
			RenderHelper.disableStandardItemLighting();
			
		}
		
	}
	
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int id)
	{
		boolean inWindow = this.active && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
		
		if(inWindow)
			onTabClicked();
		
		return inWindow;
	}
	
	@Override
	public void onPress()
	{
		onTabClicked();
	}
	
	public abstract void onTabClicked();
	
	public boolean shouldAddToList()
	{
		return true;
	}
}
