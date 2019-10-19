package com.mr208.unwired.network;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.network.packet.ConversionParticlePacket;
import com.mr208.unwired.network.packet.RebreatherParticlePacket;
import com.mr208.unwired.network.packet.WritableColorPacket;
import com.mr208.unwired.network.packet.WritableMenuPacket;
import com.mr208.unwired.network.packet.WritableSyncPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler
{
	private static final String PROTOCOL_VERSION = "1";
	private static final SimpleChannel instance =NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(UnWIRED.MOD_ID, "main_channel"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();
	
	private static int id = 0;
	
	public static void onSetup()
	{
		instance.registerMessage(
				id++, RebreatherParticlePacket.class,
				RebreatherParticlePacket::encode,
				RebreatherParticlePacket::decode,
				RebreatherParticlePacket.Handler::process);
		
		instance.registerMessage(
				id++, ConversionParticlePacket.class,
				ConversionParticlePacket::encode,
				ConversionParticlePacket::decode,
				ConversionParticlePacket.Handler::process);
		
		instance.registerMessage(
				id++, WritableSyncPacket.class,
				WritableSyncPacket::encode,
				WritableSyncPacket::decode,
				WritableSyncPacket.Handler::process);
		
		instance.registerMessage(
				id++, WritableMenuPacket.class,
				WritableMenuPacket::encode,
				WritableMenuPacket::decode,
				WritableMenuPacket.Handler::process);
		
		instance.registerMessage(
				id++, WritableColorPacket.class,
				WritableColorPacket::encode,
				WritableColorPacket::decode,
				WritableColorPacket.Handler::process);
	}
	
	public static void sendToPlayer(PlayerEntity playerEntity, Object msg)
	{
		instance.send(PacketDistributor.PLAYER.with(()-> (ServerPlayerEntity)playerEntity), msg);
	}
	
	public static void sendToServer(Object msg)
	{
		instance.sendToServer(msg);
	}
	
	public static void sendToNearbyPlayers(PlayerEntity originating, int range, DimensionType dimensionType, Object msg)
	{
		instance.send(
				PacketDistributor.NEAR.with(() -> new TargetPoint(
					null,
					originating.posX,
					originating.posY,
					originating.posZ,
					range,
					dimensionType)),
					msg);
	}
}
