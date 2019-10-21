package com.mr208.unwired.common.inventory;

import com.mr208.unwired.common.inventory.UWSlot.Charge;
import com.mr208.unwired.common.inventory.UWSlot.Food;
import com.mr208.unwired.common.inventory.UWSlot.Output;
import com.mr208.unwired.common.tile.MetabolicGenTile;
import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.content.ModContainers;
import com.mr208.unwired.common.util.UWEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class MetabolicGenContainer extends AbstractContainerBase
{
	private MetabolicGenTile tile;
	private PlayerEntity player;
	private IItemHandler playerInv;
	
	public MetabolicGenContainer(int id, PlayerInventory playerInventory)
	{
		super(ModContainers.metabolic_generator,id);
	}
	
	public MetabolicGenContainer(int id, PlayerInventory playerInventory, BlockPos pos)
	{
		super(ModContainers.metabolic_generator,id);
		
		this.tile =(MetabolicGenTile)playerInventory.player.world.getTileEntity(pos);
		this.player = playerInventory.player;
		this.playerInv = new InvWrapper(playerInventory);
		
		tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
			addSlot(new Charge(this,handler,0, 10,61));
			addSlot(new Food(this,handler,1, 82,36));
			addSlot(new Output(this,handler,2, 82,58));
		});
		
		addPlayerSlots(playerInv, 8, 84);
		
		trackInt(new IntReferenceHolder()
		{
			@Override
			public int get()
			{
				return getEnergy();
			}
			
			@Override
			public void set(int i)
			{
				tile.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler -> ((UWEnergyStorage)handler).setEnergy(i));
			}
		});
	}
	
	public int getEnergy()
	{
		return tile.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerEntity)
	{
		return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()),playerEntity, ModBlocks.generator_metabolic);
	}
}
