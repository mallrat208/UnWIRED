package com.mr208.unwired;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;
import java.util.List;

@EventBusSubscriber
public class Config
{
	public static final String CATEGORY_GENERAL = "general";
	public static final String CATEGORY_POWER = "power";
	public static final String CATEGORY_ENTITIES = "Entity";
	public static final String SUBCATEGORY_GREY_GOO = "GreyGoo";
	
	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new Builder();
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new Builder();
	
	public static ForgeConfigSpec COMMON;
	public static ForgeConfigSpec CLIENT;
	
	public static ForgeConfigSpec.ConfigValue<List<Integer>> GREY_GOO_DIMENSION_WHITELIST;
	
	static
	{
		COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.comment("Power").push(CATEGORY_POWER);
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.push(CATEGORY_ENTITIES);
		COMMON_BUILDER.push(SUBCATEGORY_GREY_GOO);
		GREY_GOO_DIMENSION_WHITELIST = COMMON_BUILDER.define("GreyGoo Dimension Whitelist", Lists.newArrayList(0));
		COMMON_BUILDER.pop();
		COMMON_BUILDER.pop();
		
		COMMON = COMMON_BUILDER.build();
		CLIENT = CLIENT_BUILDER.build();
	}
	
	public static void loadConfig(ForgeConfigSpec spec, Path path)
	{
		final CommentedFileConfig configData = CommentedFileConfig.builder(path)
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();
		
		configData.load();
		spec.setConfig(configData);
	}
	
	@SubscribeEvent
	public static void onConfigLoad(final ModConfig.Loading event)
	{
	
	}
	
	@SubscribeEvent
	public static void onConfigReload(final ModConfig.ConfigReloading event)
	{
	
	}
	
}
