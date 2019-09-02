package com.mr208.unwired;

import com.mr208.unwired.client.ClientProxy;
import com.mr208.unwired.setup.IProxy;
import com.mr208.unwired.common.CommonProxy;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
	}
	
	
	private void setup(final FMLCommonSetupEvent event)
	{
		proxy.init();
	}
	
	public Logger getLogger()
	{
		return LOGGER;
	}
}
