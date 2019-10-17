package com.mr208.unwired.common.inventory;

import com.google.common.collect.Lists;
import com.mr208.unwired.libs.TagLib;
import com.mr208.unwired.common.Content;
import com.mr208.unwired.common.crafting.RecipeTypes;
import com.mr208.unwired.common.crafting.ResequencerRecipe;
import com.mr208.unwired.common.util.NBTHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class ResequencerContainer extends Container
{
	private final IWorldPosCallable iWorldPosCallable;
	private final IntReferenceHolder intReferenceHolder = IntReferenceHolder.single();
	private final World world;
	private List<ResequencerRecipe> recipes = Lists.newArrayList();
	private ItemStack result = ItemStack.EMPTY;
	private long timeElapsed;
	private final Slot inputSlot;
	private final Slot outputSlot;
	private Runnable inventoryUpdateListener = () -> {};
	private final IInventory inventory = new Inventory(1)
	{
		@Override
		public void markDirty()
		{
			super.markDirty();
			onCraftMatrixChanged(this);
			inventoryUpdateListener.run();
		}
	};
	
	private final CraftResultInventory resultInventory = new CraftResultInventory();

	public ResequencerContainer(int id, PlayerInventory playerInventory)
	{
		this(id, playerInventory, IWorldPosCallable.DUMMY);
	}
	
	public ResequencerContainer(int id, PlayerInventory playerInventory, final IWorldPosCallable iWorldPosCallable)
	{
		super(Content.Containers.resequencer, id);
		this.iWorldPosCallable = iWorldPosCallable;
		this.world = playerInventory.player.world;
		this.inputSlot = addSlot(new Slot(this.inventory, 0, 20, 33));
		this.outputSlot = addSlot(new Slot(this.resultInventory, 1, 143,33) {
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}
			
			@Override
			public boolean canTakeStack(PlayerEntity p_82869_1_)
			{
				if(intReferenceHolder.get() != -1  && intReferenceHolder.get() < getRecipeList().size())
				{
					if(inputSlot.getStack().getCount() < getRecipeList().get(intReferenceHolder.get()).getIngredientCount())
						return false;
				}
				
				
				return super.canTakeStack(p_82869_1_);
			}
			
			@Override
			public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack)
			{
				int count = recipes.get(intReferenceHolder.get()).getIngredientCount();
				ItemStack itemStack = inputSlot.decrStackSize(count);
				if(!itemStack.isEmpty())
					func_217082_1();
				stack.getItem().onCreated(stack,thePlayer.world, thePlayer);
				iWorldPosCallable.consume((world1, blockPos) -> {
					long l = world1.getGameTime();
					if(timeElapsed != l)
					{
						world1.playSound(null, blockPos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1f,1f);
						timeElapsed = l;
					}
				});
				return super.onTake(thePlayer, stack);
			}
		});
		for(int i = 0; i <3 ; i++)
			for(int j = 0; j<9; j++)
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
		
		for(int k = 0; k < 9; k++)
			addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		
		trackInt(this.intReferenceHolder);
	}
	
	public int func_217073_e()
	{
		return this.intReferenceHolder.get();
	}
	
	public List<ResequencerRecipe> getRecipeList()
	{
		return this.recipes;
	}
	
	public int getRecipeListSize()
	{
		return this.recipes.size();
	}
	
	public boolean func_217083_h()
	{
		return this.inputSlot.getHasStack() && !this.recipes.isEmpty();
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return this.iWorldPosCallable.applyOrElse((world, pos) -> playerIn.getDistanceSq((double) pos.getX() + 0.5d, (double) pos.getY() + 0.5d, (double) pos.getZ() + 0.5d) <= 64d, true);
	}
	
	@Override
	public boolean enchantItem(PlayerEntity playerIn, int id)
	{
		if(id >= 0 && id < this.recipes.size())
		{
			this.intReferenceHolder.set(id);
			func_217082_1();
		}
		return true;
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		ItemStack stack = this.inputSlot.getStack();
		
		if(stack.getItem() != this.result.getItem())
		{
			this.result=stack.copy();
			updateAvailableRecipes(inventoryIn, stack);
		}
		else
		{
			if(intReferenceHolder.get() != -1)
				func_217082_1();
		}
	}
	
	private void updateAvailableRecipes(IInventory inventoryIn, ItemStack stack)
	{
		this.recipes.clear();
		this.intReferenceHolder.set(-1);
		this.outputSlot.putStack(ItemStack.EMPTY);
		if(!stack.isEmpty())
			this.recipes = this.world.getRecipeManager().getRecipes(RecipeTypes.RESEQUENCER, inventoryIn, this.world);
	}
	
	private void func_217082_1()
	{
		
		if(!this.recipes.isEmpty())
		{
			ResequencerRecipe recipe = this.recipes.get(this.intReferenceHolder.get());
			ItemStack outputStack = recipe.getCraftingResult(this.inventory);
			
			if(inputSlot.getStack().getCount() < getRecipeList().get(intReferenceHolder.get()).getIngredientCount())
			{
				outputStack = ItemStack.EMPTY;
			}
			else if(this.inputSlot.getStack().getItem().getTags().contains(TagLib.ITEM_SOYBEAN))
			{
				outputStack.setDisplayName(new TranslationTextComponent("unwired.reseqeuencer.synthetic", outputStack.getDisplayName()));
				NBTHelper.addUnlocalizedLoreTag(outputStack, "resequencer.soymeat");
				//NBTHelper.setUnlocalizedNameTag(outputStack, outputStack.getTranslationKey() + ".synthetic");
			}
			else if(this.inputSlot.getStack().getItem() == Items.ROTTEN_FLESH)
			{
				outputStack.setDisplayName(new TranslationTextComponent("unwired.reseqeuencer.reclaimed", outputStack.getDisplayName()));
				NBTHelper.addUnlocalizedLoreTag(outputStack, "resequencer.fleshmeat");
				
			}
			
			this.outputSlot.putStack(outputStack.copy());
		}
		else
		{
			this.outputSlot.putStack(ItemStack.EMPTY);
		}
		detectAndSendChanges();
	}
	
	@Override
	public ContainerType<?> getType()
	{
		return Content.Containers.resequencer;
	}
	
	@OnlyIn(Dist.CLIENT)
	public void setInventoryUpdateListener(Runnable inventoryUpdateListener)
	{
		this.inventoryUpdateListener=inventoryUpdateListener;
	}
	
	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return false;
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			Item item = itemstack1.getItem();
			stack = itemstack1.copy();
			if (index == 1) {
				
				if(this.inputSlot.getStack().getCount() < getRecipeList().get(intReferenceHolder.get()).getIngredientCount())
					return ItemStack.EMPTY;
				
				item.onCreated(itemstack1, playerIn.world, playerIn);
				if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, stack);
			} else if (index == 0) {
				if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index < 29) {
				if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
				return ItemStack.EMPTY;
			}
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			slot.onSlotChanged();
			if (itemstack1.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, itemstack1);
			detectAndSendChanges();
		}
		
		return stack;
	}
	
	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		this.resultInventory.removeStackFromSlot(1);
		this.iWorldPosCallable.consume((world, pos) -> clearContainer(playerIn, world, this.inventory));
	}
}