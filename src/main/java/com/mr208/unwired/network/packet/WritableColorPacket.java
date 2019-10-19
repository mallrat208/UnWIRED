package com.mr208.unwired.network.packet;

import com.mr208.unwired.UnWIRED;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class WritableColorPacket
{
	private final BlockPos packetPos;
	private final String packetColor;
	
	public WritableColorPacket(BlockPos pos, String color)
	{
		this.packetPos = pos;
		this.packetColor = color;
	}
	
	public static void encode(WritableColorPacket msg, PacketBuffer buf)
	{
		buf.writeBlockPos(msg.packetPos);
		buf.writeString(msg.packetColor);
	}
	
	public static WritableColorPacket decode(PacketBuffer buf)
	{
		return new WritableColorPacket(buf.readBlockPos(), buf.readString());
	}
	
	public static class Handler
	{
		public static void process(WritableColorPacket msg, Supplier<Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				UnWIRED.proxy.setWritableSignColor(msg.packetPos, msg.packetColor);
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
