package com.mr208.unwired.common.item;

import com.mr208.unwired.common.content.ModGroups;
import com.mr208.unwired.common.item.base.UWBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FluidCanister extends UWBase
{
	private final CanisterType type;
	
	private static Set<FluidCanister> fluidCanisterSet = new HashSet<>();
	
	public FluidCanister(CanisterType type)
	{
		super("canister_" + type.getName(), new Item.Properties().group(ModGroups.mainGroup).maxStackSize(1).rarity(type.rarity));
		this.type = type;
		fluidCanisterSet.add(this);
		this.addPropertyOverride(new ResourceLocation("filled"), (stack, world, entity) ->FluidUtil.getFluidContained(stack).map(FluidStack::isEmpty).orElse(false) ? 1.0F : 0.0F);
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		return new ItemStack(this);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced)
	{
		if(world != null)
		{
			if(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent())
			{
				FluidStack fs=FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
				
				if(fs.isEmpty())
				{
					tooltip.add(new TranslationTextComponent("tooltip.unwired.fluid.empty"));
				} else
				{
					tooltip.add(new TranslationTextComponent("tooltip.unwired.canister.fluid", fs.getDisplayName()));
					tooltip.add(new TranslationTextComponent("tooltip.unwired.canister.amount", fs.getAmount(), this.type.capacity));
				}
			}
		}
		super.addInformation(stack, world, tooltip, advanced);
	}
	
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
	{
		if(!stack.isEmpty())
			return new FluidHandlerItemStackSimple(stack, type.capacity);
		
		return null;
	}
	
	public int getMaterialColor()
	{
		return type.getColor();
	}
	
	public static Collection<FluidCanister> getFluidCanisterItems()
	{
		return fluidCanisterSet;
	}
	
	public enum CanisterType implements IStringSerializable
	{
		POLYMER(Rarity.UNCOMMON,8000,13223093),
		CARBON(Rarity.RARE,16000,1),
		PLASTEEL(Rarity.EPIC,32000,1);
		
		private Rarity rarity;
		private int capacity;
		private int color;
		
		CanisterType(Rarity rarityIn, int capacityIn, int colorIn)
		{
			this.rarity = rarityIn;
			this.capacity = capacityIn;
			this.color = colorIn;
		}
		
		@Override
		public String getName()
		{
			return this.name().toLowerCase();
		}
		
		public int getColor()
		{
			return this.color;
		}
	}
}
