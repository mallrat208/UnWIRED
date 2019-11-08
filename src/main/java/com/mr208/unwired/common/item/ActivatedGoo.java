package com.mr208.unwired.common.item;

import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.item.base.UWBase;
import com.mr208.unwired.common.tile.PolymerizedLogTile;
import com.mr208.unwired.libs.TagLib;
import com.mr208.unwired.network.NetworkHandler;
import com.mr208.unwired.network.packet.ConversionParticlePacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ActivatedGoo extends UWBase
{
	public ActivatedGoo()
	{
		super("active_goo");
	}
	
	@Override
	public Rarity getRarity(ItemStack stack)
	{
		return Rarity.UNCOMMON;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return stack.getItem() == this;
	}
	
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
	{
		if(!context.getPlayer().canPlayerEdit(context.getPos(), context.getFace(), context.getItem()))
		{
			return ActionResultType.FAIL;
		}
		else if(context.isPlacerSneaking()){
			return ActionResultType.PASS;
		}
		else
		{
			PlayerEntity player = context.getPlayer();
			World world = context.getWorld();
			
			Block targetBlock = world.getBlockState(context.getPos()).getBlock();
			
			if(targetBlock.getTags().contains(TagLib.BLOCK_LOGS) && !targetBlock.getTags().contains(TagLib.BLACKLIST_GOO))
			{
				
				player.setActiveHand(context.getHand());
				
				world.playSound(null, context.getPos(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 1f, 1f);
				NetworkHandler.sendToNearbyPlayers(player, 32, world.getDimension().getType(), new ConversionParticlePacket(context.getPos()));
				
				BlockState stateIn = world.getBlockState(context.getPos());
				
				world.setBlockState(context.getPos(), ModBlocks.polymerized_log.getDefaultState(),10);
				
				TileEntity te = world.getTileEntity(context.getPos());
				
				if(te != null && te instanceof PolymerizedLogTile)
				{
					((PolymerizedLogTile)te).setCachedState(stateIn);
					te.markDirty();
				}
				
				if(!player.isCreative())
					stack.shrink(1);
				
				return ActionResultType.SUCCESS;
			}

			/*
			if(GooConversion.hasConversion(targetBlock))
			{
				Object object = GooConversion.getConversionOutput(targetBlock);
				
				player.setActiveHand(context.getHand());
				
				world.playSound(null, context.getPos(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 1f, 1f);
				NetworkHandler.sendToNearbyPlayers(player, 32, world.getDimension().getType(), new ConversionParticlePacket(context.getPos()));
				
				if(object instanceof Block)
				{
					world.setBlockState(context.getPos(), ((Block)object).getDefaultState(),10);
				}
				else if(object instanceof ItemStack || object instanceof Item)
				{
					ItemStack dropStack = object instanceof ItemStack ? ((ItemStack)object).copy(): new ItemStack((Item)object);
					
					ItemEntity item = new ItemEntity(world, context.getPos().getX() + 0.5D, context.getPos().getY() + 0.5D, context.getPos().getZ() + 0.5D, dropStack);
					world.removeBlock(context.getPos(),false);
					world.addEntity(item);
				}
				
				if(!player.isCreative())
				{
					stack.shrink(1);
				}
				
				return ActionResultType.SUCCESS;
			}
			*/
		}
		
		return super.onItemUseFirst(stack, context);
	}
}
