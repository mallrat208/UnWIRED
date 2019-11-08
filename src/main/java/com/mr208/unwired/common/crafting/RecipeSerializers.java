package com.mr208.unwired.common.crafting;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.crafting.recipes.DyeColorableRecipe;
import com.mr208.unwired.common.crafting.recipes.PolymerizerRecipe;
import com.mr208.unwired.common.crafting.recipes.ResequencerRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(bus = Bus.MOD)
public class RecipeSerializers
{
	public static final IRecipeSerializer<ResequencerRecipe> RESEQUENCER = new ResequencerRecipe.Serializer<>(ResequencerRecipe::new);
	public static final IRecipeSerializer<PolymerizerRecipe> POLYMERIZER = new PolymerizerRecipe.Serializer<>(PolymerizerRecipe::new);
	public static final SpecialRecipeSerializer<DyeColorableRecipe> DYE_COLORABLE = new SpecialRecipeSerializer<>(DyeColorableRecipe::new);

	@SubscribeEvent
	public static void onSerializerRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> event)
	{
		event.getRegistry().registerAll(
				RESEQUENCER.setRegistryName(new ResourceLocation(UnWIRED.MOD_ID, "resequencer")),
				DYE_COLORABLE.setRegistryName(new ResourceLocation(UnWIRED.MOD_ID,"dye_colorable")),
				POLYMERIZER.setRegistryName(new ResourceLocation(UnWIRED.MOD_ID, "polymerizer")));
	}
}
