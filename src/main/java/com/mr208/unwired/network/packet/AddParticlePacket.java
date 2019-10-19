package com.mr208.unwired.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AddParticlePacket
{
	private final ResourceLocation packetName;
	private final BlockPos packetPos;
	private float motionX;
	private float motionY;
	private float motionZ;
	
	public AddParticlePacket(ResourceLocation packetName, BlockPos packetPos, float motX, float motY, float motz)
	{
		this.packetName = packetName;
		this.packetPos = packetPos;
		this.motionX = motX;
		this.motionY = motY;
		this.motionZ = motz;
	}
	
	public static void encode(AddParticlePacket msg, PacketBuffer buf)
	{
		buf.writeResourceLocation(msg.packetName);
		buf.writeBlockPos(msg.packetPos);
		buf.writeFloat(msg.motionX);
		buf.writeFloat(msg.motionY);
		buf.writeFloat(msg.motionZ);
	}
	
	public static AddParticlePacket decode(PacketBuffer buf)
	{
		return new AddParticlePacket(buf.readResourceLocation(), buf.readBlockPos(), buf.readFloat(), buf.readFloat(), buf.readFloat());
	}
	
	public static class Handler
	{
		public static void process(final AddParticlePacket msg, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
			
			});
			ctx.get().setPacketHandled(true);
		}
	}
}


