package com.mr208.unwired.network.packet;

import com.mr208.unwired.UnWIRED;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class SyncEnergyPacket
{
	private final BlockPos packetPos;
	private final int energy;
	
	public SyncEnergyPacket(BlockPos pos, int energy)
	{
		this.packetPos = pos;
		this.energy = energy;
	}
	
	public static void encode(SyncEnergyPacket msg, PacketBuffer buf)
	{
		buf.writeBlockPos(msg.packetPos);
		buf.writeInt(msg.energy);
	}
	
	public static SyncEnergyPacket decode(PacketBuffer buf)
	{
		return new SyncEnergyPacket(buf.readBlockPos(), buf.readInt());
	}
	
	public static class Handler
	{
		public static void process(final SyncEnergyPacket msg, Supplier<Context> ctx)
		{
			ctx.get().enqueueWork(() ->UnWIRED.proxy.syncEnergy(msg.packetPos, msg.energy));
			ctx.get().setPacketHandled(true);
		}
	}
}
