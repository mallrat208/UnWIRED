package com.mr208.unwired.common.util.ingredient;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IngredientUtils
{
	public static class TagListSized implements Ingredient.IItemList
	{
		private final Tag<Item> tag;
		private final int amount;
		
		public TagListSized(Tag<Item> tagIn, int amountIn)
		{
			this.tag = tagIn;
			this.amount = amountIn;
		}
		
		@Override
		public Collection<ItemStack> getStacks()
		{
			List<ItemStack> list =Lists.newArrayList();
			Iterator itr = this.tag.getAllElements().iterator();
			
			while(itr.hasNext())
			{
				Item item =(Item)itr.next();
				list.add(new ItemStack(item, amount));
			}
			
			if(list.size() == 0 && !ForgeConfig.SERVER.treatEmptyTagsAsAir.get())
			{
				list.add(new ItemStack(Blocks.BARRIER).setDisplayName(new StringTextComponent("Empty Tag: " + this.tag.getId().toString())));
			}
			
			return list;
		}
		
		@Override
		public JsonObject serialize()
		{
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("tag", this.tag.getId().toString());
			jsonObject.addProperty("count", this.amount);
			
			return jsonObject;
		}
	}
	
	public static Ingredient deserialize(@Nullable JsonElement json) {
		if (json != null && !json.isJsonNull()) {
			if (json.isJsonObject()) {
				return Ingredient.fromItemListStream(Stream.of(deserializeItemList(json.getAsJsonObject())));
			} else if (json.isJsonArray()) {
				JsonArray jsonarray = json.getAsJsonArray();
				if (jsonarray.size() == 0) {
					throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
				} else {
					return Ingredient.fromItemListStream(StreamSupport.stream(jsonarray.spliterator(), false).map((p_209355_0_) -> {
						return deserializeItemList(JSONUtils.getJsonObject(p_209355_0_, "item"));
					}));
				}
			} else {
				throw new JsonSyntaxException("Expected item to be object or array of objects");
			}
		} else {
			throw new JsonSyntaxException("Item cannot be null");
		}
	}
	
	public static Ingredient.IItemList deserializeItemList(JsonObject json) {
		if (json.has("item") && json.has("tag")) {
			throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
		} else {
			int amount = 1;
			ResourceLocation resourcelocation;
			if(json.has("count")) {
				amount = JSONUtils.getInt(json,"count");
			}
			
			if (json.has("item")) {
				resourcelocation = new ResourceLocation(JSONUtils.getString(json, "item"));
				Item item = (Item)Registry.ITEM.getValue(resourcelocation).orElseThrow(() -> {
					return new JsonSyntaxException("Unknown item '" + resourcelocation + "'");
				});
				return new Ingredient.SingleItemList(new ItemStack(item,amount));
			} else if (json.has("tag")) {
				resourcelocation = new ResourceLocation(JSONUtils.getString(json, "tag"));
				Tag<Item> tag = ItemTags.getCollection().get(resourcelocation);
				if (tag == null) {
					throw new JsonSyntaxException("Unknown item tag '" + resourcelocation + "'");
				} else {
					return new TagListSized(tag,amount);
				}
			} else {
				throw new JsonParseException("An ingredient entry needs either a tag or an item");
			}
		}
	}
}
