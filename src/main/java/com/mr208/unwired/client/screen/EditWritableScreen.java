package com.mr208.unwired.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.tile.IWritable;
import com.mr208.unwired.network.NetworkHandler;
import com.mr208.unwired.network.packet.WritableSyncPacket;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.fonts.TextInputUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EditWritableScreen extends Screen
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(UnWIRED.MOD_ID,"textures/gui/labelbg.png");
	
	private final IWritable tileSurface;
	private int updateCounter;
	private int editLine;
	private TextInputUtil inputUtil;
	
	public EditWritableScreen(IWritable writingSurface)
	{
		super(new TranslationTextComponent("label.edit"));
		this.tileSurface=writingSurface;
	}
	
	@Override
	protected void init()
	{
		this.minecraft.keyboardListener.enableRepeatEvents(true);
		this.addButton(new Button(this.width/ 2 - 100, this.height/2 + 100, 200, 20, I18n.format("gui.done"), (p_214266_1_)->this.close()));
		this.tileSurface.setEdit(false);
		this.inputUtil=new TextInputUtil(this.minecraft, ()->this.tileSurface.getText(this.editLine).getString(), (String s1)->this.tileSurface.setText(this.editLine, new StringTextComponent(s1)), 90);
	}
	
	@Override
	public void renderBackground()
	{
		int i = this.width / 2;
		int j = this.height / 2;
		super.renderBackground();
		GlStateManager.color4f(1f, 1f, 1f, 1f);
		getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		this.blit(i - 101,j -101, 0, 0, 256, 256);
	}
	
	@Override
	public void removed()
	{
		this.minecraft.keyboardListener.enableRepeatEvents(false);
		
		NetworkHandler.sendToServer(new WritableSyncPacket(this.tileSurface.getTile().getPos(), this.tileSurface.getText()));
		
		this.tileSurface.setEdit(true);
	}
	
	private void close()
	{
		this.tileSurface.getTile().markDirty();
		this.minecraft.displayGuiScreen(null);
	}
	
	@Override
	public void onClose()
	{
		this.close();
	}
	
	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_)
	{
		if(p_keyPressed_1_==265)
		{
			this.editLine=this.editLine-1 & this.tileSurface.getLabelRows()-1;
			this.inputUtil.func_216899_b();
			return true;
		}
		else if(p_keyPressed_1_!=264&&p_keyPressed_1_!=257&&p_keyPressed_1_!=335)
		{
			return this.inputUtil.func_216897_a(p_keyPressed_1_)||super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		}
		else
		{
			this.editLine=this.editLine+1 & this.tileSurface.getLabelRows()-1;
			this.inputUtil.func_216899_b();
			return true;
		}
	}
	
	@Override
	public void tick()
	{
		++this.updateCounter;
		if(this.tileSurface.getTile().isRemoved())
			this.close();
	}
	
	@Override
	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_)
	{
		this.inputUtil.func_216894_a(p_charTyped_1_);
		return true;
	}
	
	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_)
	{
		this.renderBackground();
		this.drawCenteredString(this.font, this.title.getFormattedText(), this.width/2, (this.height/2) - 110, 16777215);
		//GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float)(this.width/2), (float)(this.height/2 -290), 50.0f);
		//float scale=-93.75F;
		float scale = -200.00f;
		GlStateManager.scalef(scale, scale, scale);
		
		GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
		
		BlockState state = this.tileSurface.getTile().getBlockState();
		float lvt_6_2_= state.get(BlockStateProperties.HORIZONTAL_FACING).getOpposite().getHorizontalAngle();
		
		GlStateManager.rotatef(lvt_6_2_, 0.0F, 1.0F, 0.0F);
		GlStateManager.translatef(0.0F, -1.0625F, 0.0F);
		this.tileSurface.setWorkingData(this.editLine, this.inputUtil.func_216896_c(), this.inputUtil.func_216898_d(), this.updateCounter/6%2==0);
		TileEntityRendererDispatcher.instance.render(this.tileSurface.getTile(), -0.5D, -0.75D + tileSurface.getOffset(), -0.5D, 0.0F);
		this.tileSurface.clearWorkingData();
		GlStateManager.popMatrix();
		
		super.render(p_render_1_, p_render_2_, p_render_3_);
	}
}