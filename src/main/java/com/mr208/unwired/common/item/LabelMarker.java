package com.mr208.unwired.common.item;

import com.mr208.unwired.common.Content;
import com.mr208.unwired.common.capability.IWritable;
import com.mr208.unwired.common.item.base.UWBase;
import com.mr208.unwired.common.network.NetworkHandler;
import com.mr208.unwired.common.network.packet.WritableColorPacket;
import com.mr208.unwired.common.network.packet.WritableMenuPacket;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LabelMarker extends UWBase
{
	public static ArrayList<LabelMarker> registeredMarkers = new ArrayList<>();
	
	private final DyeColor markerColor;
	protected String translationKey;
	protected ITextComponent colorKey;
	
	public LabelMarker(DyeColor color)
	{
		super("marker_" + color.getName().toLowerCase(), new Item.Properties().group(Content.itemGroup).maxStackSize(1));
		this.markerColor = color;
		registeredMarkers.add(this);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_)
	{
		p_77624_3_.add(new TranslationTextComponent( "tooltip.unwired.marker").setStyle(new Style().setColor(TextFormatting.DARK_GRAY)));
		super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
	}
	
	public DyeColor getMarkerColor()
	{
		return markerColor;
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player)
	{
		return false;
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		if(!context.getWorld().isRemote() && context.getPlayer() != null)
		{
			TileEntity tile = context.getWorld().getTileEntity(context.getPos());
			if(tile != null && tile instanceof IWritable)
			{
				((IWritable)tile).setEdit(true);
				((IWritable)tile).setTextColor(this.markerColor);
				((IWritable)tile).setPlayer(context.getPlayer());
				NetworkHandler.sendToNearbyPlayers(context.getPlayer(), 64, context.getPlayer().dimension, new WritableColorPacket(context.getPos(), this.markerColor.getTranslationKey()));
				NetworkHandler.sendToPlayer(context.getPlayer(), new WritableMenuPacket(context.getPos()));
			}
			else if(tile !=null && tile instanceof SignTileEntity)
			{
				((SignTileEntity)tile).isEditable = true;
				((SignTileEntity)tile).setTextColor(this.markerColor);
				NetworkHandler.sendToNearbyPlayers(context.getPlayer(), 64, context.getPlayer().dimension, new WritableColorPacket(context.getPos(), this.markerColor.getTranslationKey()));
				context.getPlayer().openSignEditor((SignTileEntity)tile);
			}
			
		}
		return super.onItemUse(context);
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if(this.markerColor == DyeColor.BLACK)
			super.fillItemGroup(group, items);
	}
}
