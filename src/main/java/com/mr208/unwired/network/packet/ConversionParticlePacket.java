package com.mr208.unwired.network.packet;

import com.mr208.unwired.UnWIRED;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class ConversionParticlePacket
{
	private final BlockPos packetPos;
	
	public ConversionParticlePacket(BlockPos pos)
	{
		this.packetPos = pos;
	}
	
	public static void encode(ConversionParticlePacket msg, PacketBuffer buf)
	{
		buf.writeBlockPos(msg.packetPos);
	}
	
	public static ConversionParticlePacket decode(PacketBuffer buf)
	{
		return new ConversionParticlePacket(buf.readBlockPos());
	}
	
	public static class Handler
	{
		public static void process(final ConversionParticlePacket msg, Supplier<Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				UnWIRED.proxy.spawnConversionParticles(msg.packetPos);
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
