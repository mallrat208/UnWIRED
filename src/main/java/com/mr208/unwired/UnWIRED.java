package com.mr208.unwired;

import com.mr208.unwired.client.ClientProxy;
import com.mr208.unwired.common.entity.EntityHelper;
import com.mr208.unwired.setup.IProxy;
import com.mr208.unwired.common.CommonProxy;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("unwired")
public class UnWIRED
{
	public static final String MOD_ID = "unwired";
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new CommonProxy());
	
	public UnWIRED()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::outgoingIMC);
	}
	
	
	private void setup(final FMLCommonSetupEvent event)
	{
		proxy.init();
	}
	
	private void clientSetup(FMLClientSetupEvent event)
	{
	
	}
	
	private void loadComplete(final FMLLoadCompleteEvent event)
	{
		EntityHelper.loadComplete();
	}
	
	private void outgoingIMC(final InterModEnqueueEvent event)
	{
	
	}
	
	public static Logger getLogger()
	{
		return LOGGER;
	}
}
