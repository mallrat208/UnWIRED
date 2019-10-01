package com.mr208.unwired.common.crafting;

import com.google.gson.JsonObject;
import com.mr208.unwired.common.Content.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class ResequencerRecipe implements IRecipe<IInventory>
{
	protected final Ingredient ingredient;
	protected final int ingredientCount;
	protected final ItemStack result;
	private final IRecipeType<?> type;
	private final IRecipeSerializer<?> serializer;
	protected final ResourceLocation id;
	protected final String group;
	
	public ResequencerRecipe(ResourceLocation id, String group, Ingredient ingredient, int count, ItemStack result)
	{
		this.type = RecipeTypes.RESEQUENCER;
		this.serializer = RecipeSerializers.RESEQUENCER;
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.ingredientCount = count;
		this.result = result;
	}
	
	@Override
	public boolean matches(IInventory inv, World worldIn)
	{
		return this.ingredient.test(inv.getStackInSlot(0));
	}
	
	public int getIngredientCount()
	{
		return this.ingredientCount;
	}
	
	@Override
	public ItemStack getCraftingResult(IInventory inv)
	{
		return this.result.copy();
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return this.result;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		NonNullList<Ingredient> list = NonNullList.create();
		list.add(this.ingredient);
		return list;
	}
	
	@Override
	public boolean canFit(int width, int height)
	{
		return true;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public String getGroup()
	{
		return this.group;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return this.serializer;
	}
	
	@Override
	public IRecipeType<?> getType()
	{
		return this.type;
	}
	
	@Override
	//TODO: Replace with Resequencer Block
	public ItemStack getIcon()
	{
		return new ItemStack(Blocks.block_polymer);
	}
	
	@Override
	public ResourceLocation getId()
	{
		return this.id;
	}
	
	public static class Serializer<T extends  ResequencerRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>
	{
		final IRecipeFactory<T> factory;
		
		public Serializer(IRecipeFactory<T> factory)
		{
			this.factory = factory;
		}
		
		@Override
		public T read(ResourceLocation recipeId, JsonObject json)
		{
			String sGroup = JSONUtils.getString(json, "group", "");
			Ingredient ingredient;
			if(JSONUtils.isJsonArray(json, "ingredient"))
				ingredient = Ingredient.deserialize(JSONUtils.getJsonArray(json, "ingredient"));
			else
				ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
			int ingredientCount = JSONUtils.hasField(json, "in-count") ? JSONUtils.getInt(json,"in-count") : 1;
			String sResult = JSONUtils.getString(json, "result");
			int count = JSONUtils.getInt(json,"count");
			ItemStack itemStack = new ItemStack(Registry.ITEM.getOrDefault(new ResourceLocation(sResult)),count);
			
			return this.factory.create(recipeId, sGroup, ingredient, ingredientCount, itemStack);
		}
		
		@Nullable
		@Override
		public T read(ResourceLocation recipeId, PacketBuffer buffer)
		{
			String s = buffer.readString(32767);
			Ingredient ingredient = Ingredient.read(buffer);
			int count = buffer.readInt();
			ItemStack itemStack = buffer.readItemStack();
			return this.factory.create(recipeId, s, ingredient, count, itemStack);
		}
		
		@Override
		public void write(PacketBuffer buffer, T recipe)
		{
			buffer.writeString(recipe.group);
			recipe.ingredient.write(buffer);
			buffer.writeInt(recipe.ingredientCount);
			buffer.writeItemStack(recipe.result);
		}
	}
	
	public interface IRecipeFactory<T extends ResequencerRecipe>
	{
		T create(ResourceLocation id, String group, Ingredient ingredient, int count, ItemStack result);
	}
}
