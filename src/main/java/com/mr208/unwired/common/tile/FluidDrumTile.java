package com.mr208.unwired.common.tile;

import com.mr208.unwired.common.block.FluidDrum;
import com.mr208.unwired.common.block.FluidDrum.Drum;
import com.mr208.unwired.common.content.ModTileEntities;
import com.mr208.unwired.common.inventory.container.FluidDrumContainer;
import com.mr208.unwired.common.util.FluidUtils;
import com.mr208.unwired.common.util.UWInventory;
import com.mr208.unwired.common.util.UWInventoryHandler;
import com.mr208.unwired.network.NetworkHandler;
import com.mr208.unwired.network.packet.SyncFluidPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class FluidDrumTile extends UWBaseTileEntity implements UWInventory, ITickableTileEntity, UWInterfaces.IWritable, INamedContainerProvider
{
	private final int SLOT_INPUT_FULL = 0;
	private final int SLOT_OUTPUT_EMPTY = 1;
	private final int SLOT_INPUT_EMPTY = 2;
	private final int SLOT_OUTPUT_FULL = 3;
	
	private NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
	private FluidTank tank;
	private LazyOptional<IItemHandler> itemHandler = registerCapability(new UWInventoryHandler(inventory.size(), this));
	private LazyOptional<IFluidHandler> fluidHandler;
	
	//IWritable Stuff - Based on SignTileEntity
	public final ITextComponent[] labelText;
	private int maxRows;
	@OnlyIn(Dist.CLIENT)
	private boolean isEditing;
	private int lineBeingEdited = -1;
	private int field_214071_g = -1;
	private int charLinePos= -1;
	private boolean isEditable = true;
	private PlayerEntity player;
	private final String[] renderText;
	private DyeColor textColor;
	
	public FluidDrumTile(FluidDrum.Drum type)
	{
		super(ModTileEntities.fluid_drum);
		this.tank = new FluidTank(type.getCapacity()){
			@Override
			protected void onContentsChanged()
			{
				syncFluid();
			}
		};
		this.fluidHandler = registerCapability(tank);
		
		this.labelText = new ITextComponent[getLabelRows()];
		this.renderText = new String[getLabelRows()];
		this.textColor = DyeColor.BLACK;
		
		for(int i = 0; i < getLabelRows(); i++)
			labelText[i] = new StringTextComponent("");
	}
	
	@Override
	public void setStoredFluid(CompoundNBT tagIn)
	{
		tank.readFromNBT(tagIn);
	}
	
	public FluidDrumTile()
	{
		this(Drum.POLYMER);
	}
	
	@Override
	public int getCharWidth()
	{
		return 11;
	}
	
	@Override
	public TileEntity getTile()
	{
		return this;
	}
	
	@Override
	public DyeColor getTextColor()
	{
		return this.textColor;
	}
	
	@Override
	public boolean setTextColor(DyeColor dyeColor)
	{
		if(dyeColor != this.getTextColor())
		{
			this.textColor = dyeColor;
			this.markDirty();
			this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 11);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public int getLabelRows()
	{
		return 2;
	}
	
	@Override
	public void setText(int row, ITextComponent textComponent)
	{
		this.labelText[row] = textComponent;
		this.renderText[row] = null;
	}
	
	@Override
	public ITextComponent getText(int row)
	{
		return this.labelText[row];
	}
	
	@Override
	public ITextComponent[] getText()
	{
		return this.labelText;
	}
	
	@Override
	public void setWorkingData(int line, int i2, int i3, boolean isEditing)
	{
		this.lineBeingEdited = line;
		this.field_214071_g = i2;
		this.charLinePos= i3;
		this.isEditing= isEditing;
	}
	
	@Override
	public void clearWorkingData()
	{
		this.lineBeingEdited = -1;
		this.field_214071_g = -1;
		this.charLinePos= -1;
		this.isEditing= false;
	}
	
	public boolean isEditing() {
		return this.isEditing;
	}
	
	@Override
	public float getOffset()
	{
		return -0.15f;
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getLineBeingEdited() {
		return this.lineBeingEdited;
	}
	
	@OnlyIn(Dist.CLIENT)
	public int currentLength() {
		return this.field_214071_g;
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getCharLinePos() {
		return this.charLinePos;
	}
	
	@Override
	public void setEdit(boolean canEdit) {
		this.isEditable = canEdit;
		if (!canEdit) {
			this.player = null;
		}
	}
	
	@Override
	public boolean canEdit()
	{
		return this.isEditable;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public String getRenderText(int row, Function<ITextComponent, String> function)
	{
		if(this.renderText[row] == null && this.labelText[row] !=null)
		{
			this.renderText[row]= function.apply(this.labelText[row]);
		}
		
		return this.renderText[row];	}
	
	@Override
	public void setPlayer(PlayerEntity player)
	{
		this.player=player;
	}
	
	@Override
	public PlayerEntity getPlayer()
	{
		return this.player;
	}
	
	@Override
	public void readCustomNBT(CompoundNBT compound, boolean descPacket)
	{
		
		ItemStackHelper.loadAllItems(compound, inventory);
		tank.readFromNBT(compound);
		
		this.textColor = DyeColor.byTranslationKey(compound.getString("LabelColor"),DyeColor.BLACK);
		
		for(int i = 0; i < getLabelRows(); i ++)
		{
			String s = compound.getString("LabelText" + (i + 1));
			ITextComponent component =Serializer.fromJson(s.isEmpty() ? "\"\"" : s);
			
			this.labelText[i] = component;
			this.renderText[i] = null;
			
		}
	}
	
	public FluidTank getTank()
	{
		return tank;
	}
	
	@Override
	public void writeCustomNBT(CompoundNBT compound, boolean descPacket)
	{
		ItemStackHelper.saveAllItems(compound, inventory);
		tank.writeToNBT(compound);
		
		for(int i = 0; i < getLabelRows(); i ++)
		{
			String s = Serializer.toJson(this.labelText[i]);
			compound.putString("LabelText" + (i + 1), s);
		}
		
		compound.putString("LabelColor", this.textColor.getTranslationKey());
		
	}
	
	@Override
	public void tick()
	{
		if(!world.isRemote)
		{
			//Process Inventory Slots
			
			if(tank.getSpace()>0)
				tryFillTank();
			
			if(tank.getFluidAmount() > 0)
				tryDrainTank();
		}
	}
	
	private void tryFillTank()
	{
		if(!inventory.get(SLOT_INPUT_FULL).isEmpty() && (inventory.get(SLOT_OUTPUT_EMPTY).isEmpty() || (inventory.get(SLOT_OUTPUT_EMPTY).getCount()+1 <= inventory.get(SLOT_OUTPUT_EMPTY).getMaxStackSize())))
		{
			int prevAmount = tank.getFluidAmount();
			ItemStack tempStack = FluidUtils.drainFluidContainer(tank, inventory.get(SLOT_INPUT_FULL), inventory.get(SLOT_OUTPUT_EMPTY), null);
			
			if(prevAmount != tank.getFluidAmount())
			{
				if(!inventory.get(SLOT_OUTPUT_EMPTY).isEmpty() && ItemStack.areItemsEqual(tempStack, inventory.get(SLOT_OUTPUT_EMPTY)))
				{
					inventory.get(SLOT_OUTPUT_EMPTY).grow(tempStack.getCount());
				}
				else if(inventory.get(SLOT_OUTPUT_EMPTY).isEmpty())
				{
					inventory.set(SLOT_OUTPUT_EMPTY, tempStack.copy());
				}
				
				inventory.get(SLOT_INPUT_FULL).shrink(1);
				
				if(inventory.get(SLOT_INPUT_FULL).getCount() <=0)
				{
					inventory.set(SLOT_INPUT_FULL, ItemStack.EMPTY);
				}
				
				markDirty();
			}
		}
	}
	
	
	
	private void tryDrainTank()
	{
		if(!inventory.get(SLOT_INPUT_EMPTY).isEmpty() && (inventory.get(SLOT_OUTPUT_FULL).isEmpty() || inventory.get(SLOT_OUTPUT_FULL).getCount()+1 <= inventory.get(SLOT_OUTPUT_FULL).getMaxStackSize()))
		{
			ItemStack tempFilled=FluidUtils.fillFluidContainer(tank, inventory.get(SLOT_INPUT_EMPTY), inventory.get(SLOT_OUTPUT_FULL), null);
			if(!tempFilled.isEmpty())
			{
				if(inventory.get(SLOT_INPUT_EMPTY).getCount()==1&&!FluidUtils.isFluidContainerFull(tempFilled))
				{
					inventory.set(SLOT_INPUT_EMPTY, tempFilled.copy());
					markDirty();
				}
				else
				{
					if(!inventory.get(SLOT_OUTPUT_FULL).isEmpty()&&ItemStack.areItemsEqual(inventory.get(SLOT_OUTPUT_FULL), tempFilled))
					{
						inventory.get(SLOT_OUTPUT_FULL).grow(tempFilled.getCount());
					}
					else if(inventory.get(SLOT_OUTPUT_FULL).isEmpty())
					{
						inventory.set(SLOT_OUTPUT_FULL, tempFilled.copy());
					}
					inventory.get(SLOT_INPUT_EMPTY).shrink(tempFilled.getCount());
					markDirty();
				}
			}
		}
	}
	
	
	@Nullable
	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}
	
	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		switch(slot)
		{
			case SLOT_INPUT_FULL:
				return FluidUtil.getFluidHandler(stack).isPresent() && FluidUtil.getFluidContained(stack).map(fluidStack-> !fluidStack.isEmpty()).orElse(false);
			case SLOT_INPUT_EMPTY:
				return FluidUtil.getFluidHandler(stack).isPresent();
		}
		
		return false;
	}
	
	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		ITextComponent color = new TranslationTextComponent("color.minecraft." + this.world.getBlockState(this.pos).get(FluidDrum.COLOR).getTranslationKey());
		return new TranslationTextComponent("container.fluid_drum.polymer", color);
	}
	
	@Nullable
	@Override
	public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity)
	{
		return new FluidDrumContainer(i, playerInventory, this.pos);
	}
	
	@Override
	public void syncFluid()
	{
		NetworkHandler.sendToTrackingPlayers(this, new SyncFluidPacket(pos, tank.writeToNBT(new CompoundNBT())));
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap ==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemHandler.cast();
		else if(cap ==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return fluidHandler.cast();
		else
			return super.getCapability(cap, side);
	}
}
