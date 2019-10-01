package com.mr208.unwired.common;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.Content.Items;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(bus = Bus.FORGE)
public class ForgeEvents
{
	
	
	/*	Quick and Easy way to add Soybean's to the drops from Grass. Technically wrong since it ignores the loot table
		for Grass blocks, however adding another LootPool would make my item far too common compared to Wheat Seeds		*/
	@SubscribeEvent
	@OnlyIn(Dist.DEDICATED_SERVER)
	public static void onBreakEvent(BreakEvent event)
	{
		if(!event.getPlayer().isCreative() && !(event.getPlayer().getHeldItemMainhand().getItem() instanceof ShearsItem))
		{
			if(event.getState().getBlock()== Blocks.GRASS||event.getState().getBlock()== Blocks.TALL_GRASS)
			{
				UnWIRED.getLogger().info("We broke grass!");
				if(event.getWorld().getRandom().nextFloat() <= 0.125f)
				{
					UnWIRED.getLogger().info("Time to drop some seeds!");
					event.getWorld().removeBlock(event.getPos(), true);
					
					event.getWorld().addEntity(new ItemEntity((World)event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.soybean)));
				}
			}
		}
	}
}
