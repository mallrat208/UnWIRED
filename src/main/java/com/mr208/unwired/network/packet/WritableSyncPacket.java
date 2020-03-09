package com.mr208.unwired.network.packet;

import com.mr208.unwired.common.tile.UWInterfaces.IWritable;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class WritableSyncPacket
{
	private final BlockPos packetPos;
	private final int lineCount;
	private final ITextComponent[] lines;
	
	public WritableSyncPacket(BlockPos pos, ITextComponent... textComponents)
	{
		this.packetPos = pos;
		this.lines = new ITextComponent[textComponents.length];
		this.lineCount = textComponents.length;
		for(int i = 0; i < textComponents.length; i++)
			this.lines[i] = textComponents[i];
	}
	
	public static void encode(WritableSyncPacket msg, PacketBuffer buf)
	{
		buf.writeBlockPos(msg.packetPos);
		buf.writeInt(msg.lineCount);
		
		for(int i = 0; i < msg.lineCount; i++)
			buf.writeTextComponent(msg.lines[i]);
	}
	
	public static WritableSyncPacket decode(PacketBuffer buf)
	{
		BlockPos pos = buf.readBlockPos();
		int count = buf.readInt();
		
		ITextComponent[] lines = new ITextComponent[count];
		
		for(int i = 0; i < count; i++)
			lines[i] = buf.readTextComponent();
		
		return new WritableSyncPacket(pos, lines);
	}
	
	public static class Handler
	{
		public static void process(final WritableSyncPacket msg, Supplier<Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				ServerWorld serverworld = ctx.get().getSender().getServerWorld();
				BlockPos blockpos = msg.packetPos;
				if (serverworld.isBlockLoaded(blockpos)) {
					BlockState blockstate = serverworld.getBlockState(blockpos);
					TileEntity tileentity = serverworld.getTileEntity(blockpos);
					if (!(tileentity instanceof IWritable)) {
						return;
					}
					
					IWritable writableEntity = (IWritable)tileentity;
					
					for(int i = 0; i < msg.lines.length; ++i) {
						writableEntity.setText(i, msg.lines[i]);
					}
					
					writableEntity.getTile().markDirty();
					serverworld.notifyBlockUpdate(blockpos, blockstate, blockstate, BlockFlags.DEFAULT);
				}
			});
		}
	}
}
