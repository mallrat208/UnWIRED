package com.mr208.unwired.common.network.packet;

import com.mr208.unwired.UnWIRED;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class RebreatherParticlePacket
{
	private final BlockPos packetPos;
	private final float eyeHeight;
	private final float pitch;
	private final float yaw;
	
	public RebreatherParticlePacket(BlockPos packetPos, float eyeHeight, float pitch, float yaw)
	{
		this.packetPos = packetPos;
		this.eyeHeight = eyeHeight;
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	public static void encode(RebreatherParticlePacket msg, PacketBuffer buf)
	{
		buf.writeBlockPos(msg.packetPos);
		buf.writeFloat(msg.eyeHeight);
		buf.writeFloat(msg.pitch);
		buf.writeFloat(msg.yaw);
	}
	
	public static RebreatherParticlePacket decode(PacketBuffer buf)
	{
		return new RebreatherParticlePacket(buf.readBlockPos(), buf.readFloat(), buf.readFloat(), buf.readFloat());
	}
	
	public static class Handler
	{
		public static void process(final RebreatherParticlePacket msg, Supplier<Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				UnWIRED.proxy.spawnRebreatherParticle(msg.packetPos, msg.eyeHeight, msg.pitch, msg.yaw);
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
