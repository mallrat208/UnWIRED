package com.mr208.unwired.common.crafting;

import com.mr208.unwired.UnWIRED;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(bus = Bus.MOD)
public class RecipeSerializers
{
	public static final IRecipeSerializer<ResequencerRecipe> RESEQUENCER = new ResequencerRecipe.Serializer<>(ResequencerRecipe::new);

	@SubscribeEvent
	public static void onSerializerRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> event)
	{
		event.getRegistry().register(RESEQUENCER.setRegistryName(new ResourceLocation(UnWIRED.MOD_ID, "resequencer")));
	}
}
