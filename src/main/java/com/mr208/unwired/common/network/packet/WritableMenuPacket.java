package com.mr208.unwired.common.network.packet;

import com.mr208.unwired.UnWIRED;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class WritableMenuPacket
{
	private final BlockPos packetPos;
	
	public WritableMenuPacket(BlockPos pos)
	{
		this.packetPos = pos;
	}
	
	public static void encode(WritableMenuPacket msg, PacketBuffer buf)
	{
		buf.writeBlockPos(msg.packetPos);
	}
	
	public static WritableMenuPacket decode(PacketBuffer buf)
	{
		return new WritableMenuPacket(buf.readBlockPos());
	}
	
	public static class Handler
	{
		public static void process(final WritableMenuPacket msg, Supplier<Context> ctx)
		{
			ctx.get().enqueueWork(() ->{
				UnWIRED.proxy.openMarkerScreen(msg.packetPos);
			});
			
			ctx.get().setPacketHandled(true);
		}
	}
}
