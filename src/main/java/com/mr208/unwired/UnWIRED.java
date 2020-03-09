package com.mr208.unwired;

import com.mr208.unwired.client.ClientProxy;
import com.mr208.unwired.client.ClientSetup;
import com.mr208.unwired.common.Recipes;
import com.mr208.unwired.common.entity.EntityHelper;
import com.mr208.unwired.network.NetworkHandler;
import com.mr208.unwired.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("unwired")
public class UnWIRED
{
	public static final String MOD_ID = "unwired";
	private static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new CommonProxy());
	
	public UnWIRED()
	{
		ModLoadingContext.get().registerConfig(Type.CLIENT, Config.CLIENT);
		ModLoadingContext.get().registerConfig(Type.COMMON, Config.COMMON);
		
		Config.loadConfig(Config.CLIENT, FMLPaths.CONFIGDIR.get().resolve("unwired-client.toml"));
		Config.loadConfig(Config.COMMON, FMLPaths.CONFIGDIR.get().resolve("unwired-common.toml"));
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::outgoingIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStarting);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	
	private void setup(final FMLCommonSetupEvent event)
	{
		NetworkHandler.onSetup();
	}
	
	private void clientSetup(FMLClientSetupEvent event)
	{
		ClientSetup.onSetup();
	}
	
	private void loadComplete(final FMLLoadCompleteEvent event)
	{
		EntityHelper.loadComplete();
		Recipes.loadComplete();
	}
	
	private void outgoingIMC(final InterModEnqueueEvent event)
	{
	
	}
	
	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event)
	{
		Recipes.serverStarting();
	}
	
	public static Logger getLogger()
	{
		return LOGGER;
	}
}
