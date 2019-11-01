package com.mr208.unwired.network.packet;

import com.mr208.unwired.UnWIRED;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class SyncFluidPacket
{
	private final BlockPos packetPos;
	private final CompoundNBT tankTag;
	
	public SyncFluidPacket(BlockPos pos, CompoundNBT tankData)
	{
		this.packetPos = pos;
		this.tankTag = tankData;
	}
	
	public static void encode(SyncFluidPacket msg, PacketBuffer buf)
	{
		buf.writeBlockPos(msg.packetPos);
		buf.writeCompoundTag(msg.tankTag);
	}
	
	public static SyncFluidPacket decode(PacketBuffer buf)
	{
		return new SyncFluidPacket(buf.readBlockPos(), buf.readCompoundTag());
	}
	
	public static class Handler
	{
		public static void process(final SyncFluidPacket msg, Supplier<Context> ctx)
		{
			ctx.get().enqueueWork(() ->UnWIRED.proxy.syncFluid(msg.packetPos, msg.tankTag));
			ctx.get().setPacketHandled(true);
		}
	}
}
