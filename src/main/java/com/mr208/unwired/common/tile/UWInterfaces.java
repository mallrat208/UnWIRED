package com.mr208.unwired.common.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.function.Function;

public class UWInterfaces
{
	public static interface IWritable
	{
		boolean canEdit();
		void setEdit(boolean bool);
		
		int getCharWidth();
		
		int getLabelRows();
		
		void setPlayer(PlayerEntity player);
		PlayerEntity getPlayer();
		void setText(int row, ITextComponent textComponent);
		ITextComponent getText(int row);
		ITextComponent[] getText();
		@OnlyIn(Dist.CLIENT)
		void setWorkingData(int i1, int i2, int i3, boolean bool);
		@OnlyIn(Dist.CLIENT)
		void clearWorkingData();
		String getRenderText(int row, Function<ITextComponent, String> function);
		TileEntity getTile();
		DyeColor getTextColor();
		boolean setTextColor(DyeColor dyeColor);
		
		default float getOffset() {return 0f;}
	}
}
