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
	public static final String CATEGORY_ACCESSIBILITY = "Accessibility";
	public static final String CATEGORY_GENERAL = "General";
	public static final String CATEGORY_POWER = "Power";
	public static final String SUBCATEGORY_CELLS = "Cells";
	public static final String SUBCATEGORY_GENERATOR = "Generators";
	public static final String CATEGORY_ENTITIES = "Entity";
	public static final String SUBCATEGORY_GREY_GOO = "GreyGoo";
	
	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new Builder();
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new Builder();
	
	public static ForgeConfigSpec COMMON;
	public static ForgeConfigSpec CLIENT;
	
	public static ForgeConfigSpec.ConfigValue<List<Integer>> GREY_GOO_DIMENSION_WHITELIST;
	
	public static ForgeConfigSpec.BooleanValue ENERGY_CELLS_USE_DURABILITY_BAR;
	public static ForgeConfigSpec.BooleanValue USE_GRAY_BACKGROUND;
	
	public static ForgeConfigSpec.IntValue METABOLIC_GENERATOR_CAPACITY;
	public static ForgeConfigSpec.IntValue METABOLIC_GENERATOR_RATE;
	
	public static ForgeConfigSpec.IntValue TIER_1_CELL_CAPACITY;
	public static ForgeConfigSpec.IntValue TIER_2_CELL_CAPACITY;
	public static ForgeConfigSpec.IntValue TIER_3_CELL_CAPACITY;
	
	static
	{
		COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.comment("Power Generation and Storage").push(CATEGORY_POWER);
		COMMON_BUILDER.push(SUBCATEGORY_CELLS);
		TIER_1_CELL_CAPACITY = COMMON_BUILDER.defineInRange("Bio Cell Capacity", 40000, 0, Integer.MAX_VALUE);
		TIER_2_CELL_CAPACITY = COMMON_BUILDER.defineInRange("Dense Cell Capacity", 40000, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();
		COMMON_BUILDER.push(SUBCATEGORY_GENERATOR);
		METABOLIC_GENERATOR_CAPACITY = COMMON_BUILDER.defineInRange("Metabolic Generator Capacity", 120000,0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.push(CATEGORY_ENTITIES);
		COMMON_BUILDER.push(SUBCATEGORY_GREY_GOO);
		GREY_GOO_DIMENSION_WHITELIST = COMMON_BUILDER.define("GreyGoo Dimension Whitelist", Lists.newArrayList(0));
		COMMON_BUILDER.pop(2);
		
		CLIENT_BUILDER.push(CATEGORY_ACCESSIBILITY);
		USE_GRAY_BACKGROUND = CLIENT_BUILDER.define("Use Vanilla-like UI's", false);
		ENERGY_CELLS_USE_DURABILITY_BAR = CLIENT_BUILDER.define("Energy Cells Use Durability Bar", false);
		CLIENT_BUILDER.pop();
		
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
	
}
