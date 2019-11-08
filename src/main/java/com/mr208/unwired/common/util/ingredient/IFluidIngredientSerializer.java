package com.mr208.unwired.common.util.ingredient;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;

public interface IFluidIngredientSerializer<T extends FluidIngredient>
{
	T parse(PacketBuffer buf);
	T parse(JsonObject json);
	
	void write(PacketBuffer buf, T fluidIngredient);
}
