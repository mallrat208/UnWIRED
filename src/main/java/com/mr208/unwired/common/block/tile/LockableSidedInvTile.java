package com.mr208.unwired.common.block.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class LockableSidedInvTile extends LockableTileEntity implements ISidedInventory
{
	protected NonNullList<ItemStack> items;
	protected Map<Direction, Integer> sideAccessMap;
	private final LazyOptional<? extends IItemHandler>[] itemHandlers;
	
	
	protected LockableSidedInvTile(TileEntityType<?> typeIn)
	{
		super(typeIn);
		itemHandlers = getSidedInvWrapper();
		
		this.items = NonNullList.withSize(getItemsCount(), ItemStack.EMPTY);
	}
	
	@Override
	public int getSizeInventory()
	{
		return items.size();
	}
	
	@Override
	public boolean isEmpty()
	{
		for(ItemStack stack : items)
			if(!stack.isEmpty())
				return false;
		
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int itemIndex)
	{
		if(itemIndex < 0 || itemIndex >= items.size())
			return ItemStack.EMPTY;
		
		return items.get(itemIndex);
	}
	
	@Override
	public ItemStack decrStackSize(int itemIndex, int count)
	{
		return ItemStackHelper.getAndSplit(items, itemIndex, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int itemIndex)
	{
		return ItemStackHelper.getAndRemove(items, itemIndex);
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity playerEntity)
	{
		//TODO: Distance Check
		return world != null && world.getTileEntity(pos) == this;
	}
	
	@Override
	public void clear()
	{
		items.clear();
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		items = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, items);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		ItemStackHelper.saveAllItems(compound, items);
		
		return compound;
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT compound = getUpdateTag();
		ItemStackHelper.saveAllItems(compound, items);
		return new SUpdateTileEntityPacket(pos,1,compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		super.onDataPacket(net, pkt);
		ItemStackHelper.loadAllItems(pkt.getNbtCompound(), items);
	}
	
	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
	{
		if(!this.isRemoved() && side != null && sideAccessMap.containsKey(side) && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return itemHandlers[sideAccessMap.get(side)].cast();
		}
		
		return super.getCapability(cap, side);
	}
	
	@Override
	public void remove()
	{
		super.remove();
		for(LazyOptional<? extends IItemHandler> handler: itemHandlers)
			handler.invalidate();
	}
	
	/* Override to Change the Item Handler access points */
	public LazyOptional<? extends IItemHandler>[] getSidedInvWrapper()
	{
		sideAccessMap = new HashMap<>();
		
		sideAccessMap.put(Direction.UP, 0);
		sideAccessMap.put(Direction.DOWN, 1);
		sideAccessMap.put(Direction.NORTH, 2);
		
		return SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	}
	
	/* Number of Item Slots/Stacks that can be held */
	abstract int getItemsCount();
}
