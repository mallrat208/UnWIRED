package com.mr208.unwired.common.crafting.recipes;

import com.google.gson.JsonObject;
import com.mr208.unwired.common.inventory.IFluidInventory;
import com.mr208.unwired.common.crafting.RecipeInterfaces.IFluidRecipe;
import com.mr208.unwired.common.crafting.RecipeSerializers;
import com.mr208.unwired.common.crafting.RecipeTypes;
import com.mr208.unwired.common.util.ingredient.FluidIngredient;
import com.mr208.unwired.common.util.ingredient.IngredientUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class PolymerizerRecipe implements IFluidRecipe<IFluidInventory>
{
	protected final FluidIngredient fluidIngredient;
	protected final Ingredient ingredient;
	protected final ItemStack result;
	private final IRecipeType<?> type;
	private final IRecipeSerializer<?> serializer;
	protected final ResourceLocation id;
	protected final String group;
	
	public PolymerizerRecipe(ResourceLocation id, String group, FluidIngredient fluidIngredient, Ingredient ingredient, ItemStack result)
	{
		this.type = RecipeTypes.POLYMERIZER;
		this.serializer = RecipeSerializers.POLYMERIZER;
		this.id = id;
		this.group = group;
		this.fluidIngredient = fluidIngredient;
		this.ingredient = ingredient;
		this.result = result;
	}
	
	@Override
	public boolean matches(IFluidInventory iFluidInventory, World world)
	{
		return this.ingredient.test(iFluidInventory.getStackInSlot(0)) && this.fluidIngredient.test(iFluidInventory.getFluidInTank(0));
	}
	
	@Override
	public ItemStack getCraftingResult(IFluidInventory iFluidInventory)
	{
		return this.result.copy();
	}
	
	@Override
	public boolean canFit(int i, int i1)
	{
		return true;
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return this.result;
	}
	
	@Override
	public ResourceLocation getId()
	{
		return this.id;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return this.serializer;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		NonNullList<Ingredient> list = NonNullList.create();
		list.add(this.ingredient);
		return list;
	}
	
	public NonNullList<FluidIngredient> getFluidIngredients()
	{
		NonNullList<FluidIngredient> list = NonNullList.create();
		list.add(this.fluidIngredient);
		return list;
	}
	
	@Override
	public IRecipeType<?> getType()
	{
		return this.type;
	}
	
	@Override
	public ItemStack getIcon()
	{
		//TODO: ICON
		return new ItemStack(Items.APPLE);
	}
	
	public static class Serializer<T extends  PolymerizerRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>
	{
		final IRecipeFactory<T> factory;
		
		public Serializer(IRecipeFactory<T> factory)
		{
			this.factory = factory;
		}
		
		@Override
		public T read(ResourceLocation recipeID, JsonObject json)
		{
			String sGroup =JSONUtils.getString(json, "group", "");
			FluidIngredient fluidIngredient;
			if(JSONUtils.isJsonArray(json,"liquid"))
				fluidIngredient = FluidIngredient.deserialize(JSONUtils.getJsonArray(json, "liquid"));
			else
				fluidIngredient = FluidIngredient.deserialize(JSONUtils.getJsonObject(json,"liquid"));
			Ingredient ingredient;
			if(JSONUtils.isJsonArray(json, "ingredient"))
				ingredient = IngredientUtils.deserialize(JSONUtils.getJsonArray(json, "ingredient"));
			else
				ingredient = IngredientUtils.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
			String sResult = JSONUtils.getString(json, "result");
			int count = JSONUtils.getInt(json, "count");
			ItemStack itemStack = new ItemStack(Registry.ITEM.getOrDefault(new ResourceLocation(sResult)), count);
			return this.factory.create(recipeID, sGroup, fluidIngredient,ingredient ,itemStack);
		}
		
		@Nullable
		@Override
		public T read(ResourceLocation recipeID, PacketBuffer buffer)
		{
			String s = buffer.readString(32767);
			Ingredient ingredient = Ingredient.read(buffer);
			FluidIngredient fluid = FluidIngredient.read(buffer);
			ItemStack output = buffer.readItemStack();
			
			return this.factory.create(recipeID, s, fluid, ingredient, output);
		}
		
		@Override
		public void write(PacketBuffer buffer, T recipe)
		{
			buffer.writeString(recipe.group);
			recipe.ingredient.write(buffer);
			recipe.fluidIngredient.write(buffer);
			buffer.writeItemStack(recipe.result);
		}
	}
	
	public interface IRecipeFactory<T extends PolymerizerRecipe>
	{
		T create(ResourceLocation id, String group, FluidIngredient fluidIngredient, Ingredient ingredient, ItemStack result);
	}
}
