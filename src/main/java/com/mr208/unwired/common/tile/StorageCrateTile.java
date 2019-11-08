package com.mr208.unwired.common.tile;

import com.mr208.unwired.common.content.ModTileEntities;
import com.mr208.unwired.common.block.StorageCrate;
import com.mr208.unwired.common.block.StorageCrate.Crate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.function.Function;

public class StorageCrateTile extends LockableLootTileEntity implements ITickableTileEntity, UWInterfaces.IWritable
{
	
	private int invSize;
	private int invRows;
	private NonNullList<ItemStack> inventory;
	private LazyOptional<IItemHandlerModifiable> itemHandler;
	
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
	
	public StorageCrateTile(StorageCrate.Crate crate)
	{
		super(ModTileEntities.storage_crate);
		this.invSize = crate.getSlotCount();
		this.invRows = crate.getRows();
		this.inventory = NonNullList.withSize(crate.getSlotCount(), ItemStack.EMPTY);
		
		this.labelText = new ITextComponent[getLabelRows()];
		this.renderText = new String[getLabelRows()];
		this.textColor = DyeColor.BLACK;
		
		for(int i = 0; i < getLabelRows(); i++)
			labelText[i] = new StringTextComponent("");
	}
	
	public int getInvRows()
	{
		return invRows;
	}
	
	public StorageCrateTile()
	{
		this(Crate.POLYMER);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		this.isEditable = false;
		super.read(compound);
		this.invSize = compound.getInt("InvSize");
		this.invRows = compound.getInt("InvRows");
		this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		if(!this.checkLootAndRead(compound))
			ItemStackHelper.loadAllItems(compound,this.inventory);
		
		this.textColor = DyeColor.byTranslationKey(compound.getString("LabelColor"),DyeColor.BLACK);
		
		for(int i = 0; i < getLabelRows(); i ++)
		{
			String s = compound.getString("LabelText" + (i + 1));
			ITextComponent component =Serializer.fromJson(s.isEmpty() ? "\"\"" : s);
			
			this.labelText[i] = component;
			this.renderText[i] = null;
			
		}
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 9, this.getUpdateTag());
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.write(super.getUpdateTag());
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		CompoundNBT tag = super.write(compound);
		tag.putInt("InvSize", this.invSize);
		tag.putInt("InvRows", this.invRows);
		if(!this.checkLootAndWrite(compound))
			ItemStackHelper.saveAllItems(compound, this.inventory);
		
		for(int i = 0; i < getLabelRows(); i ++)
		{
			String s = Serializer.toJson(this.labelText[i]);
			compound.putString("LabelText" + (i + 1), s);
		}
		
		compound.putString("LabelColor", this.textColor.getTranslationKey());
		
		return tag;
	}
	
	@Override
	protected NonNullList<ItemStack> getItems()
	{
		return this.inventory;
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> nonNullList)
	{
		this.inventory = nonNullList;
	}
	
	@Override
	protected ITextComponent getDefaultName()
	{
		ITextComponent color = new TranslationTextComponent("color.minecraft." + this.world.getBlockState(this.pos).get(StorageCrate.COLOR).getTranslationKey());
		return new TranslationTextComponent("container.storage_crate.polymer", color);
	}
	
	@Override
	protected Container createMenu(int i, PlayerInventory playerInventory)
	{
		ContainerType containerType = ContainerType.GENERIC_9X1;
		if(this.invSize == 36)
			containerType = ContainerType.GENERIC_9X4;
		else if(this.invSize == 45)
			containerType = ContainerType.GENERIC_9X5;
		else if(this.invSize == 54)
			containerType = ContainerType.GENERIC_9X6;
		
		return new ChestContainer(containerType, i, playerInventory, this,this.getInvRows());
	}
	
	@Override
	public int getSizeInventory()
	{
		return invSize;
	}
	
	@Override
	public boolean isEmpty()
	{
		Iterator crateItr = this.inventory.iterator();
		
		ItemStack testStack;
		do
		{
			if(!crateItr.hasNext())
				return true;
			
			testStack = (ItemStack)crateItr.next();
		}
		while(testStack.isEmpty());
		
		return false;
	}
	
	@Override
	public void tick()
	{
	
	}
	
	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
	{
		if(!this.removed && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if(this.itemHandler==null)
				this.itemHandler=LazyOptional.of(()->new InvWrapper(this));
			
			return this.itemHandler.cast();
		}
		
		return super.getCapability(cap, side);
	}
	
	@Override
	public void remove()
	{
		super.remove();
		if(this.itemHandler != null)
			this.itemHandler.invalidate();
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
		
		return this.renderText[row];
	}
	
	@Override
	public void setPlayer(PlayerEntity player)
	{
		this.player = player;
	}
	
	@Override
	public PlayerEntity getPlayer()
	{
		return this.player;
	}
}
