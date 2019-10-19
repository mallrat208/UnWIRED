package com.mr208.unwired.common.content;

import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.block.MetabolicGenerator;
import com.mr208.unwired.common.block.PlexiglassBlock;
import com.mr208.unwired.common.block.Resequencer;
import com.mr208.unwired.common.block.SmartglassBlock;
import com.mr208.unwired.common.block.SoyCrop;
import com.mr208.unwired.common.block.StorageCrate;
import com.mr208.unwired.common.block.StorageCrate.Crate;
import com.mr208.unwired.common.block.base.UWBlock;
import com.mr208.unwired.common.block.base.UWFluidBlock;
import com.mr208.unwired.common.block.tile.MetabolicGenTile;
import com.mr208.unwired.common.block.tile.StorageCrateTile;
import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.content.ModEntities;
import com.mr208.unwired.common.content.ModFluids;
import com.mr208.unwired.common.content.ModItems;
import com.mr208.unwired.common.entity.GreyGooEntity;
import com.mr208.unwired.common.fluid.NanoFluid;
import com.mr208.unwired.common.inventory.ResequencerContainer;
import com.mr208.unwired.common.item.ActivatedGoo;
import com.mr208.unwired.common.item.CrateItem;
import com.mr208.unwired.common.item.LabelMarker;
import com.mr208.unwired.common.item.SoybeanItem;
import com.mr208.unwired.common.item.base.UWBase;
import com.mr208.unwired.common.item.base.UWBlockItem;
import com.mr208.unwired.common.item.base.UWBucket;
import com.mr208.unwired.common.item.base.UWDirectionalBlockItem;
import com.mr208.unwired.common.item.base.UWSpawnItem;
import com.mr208.unwired.common.item.equipment.FlippersBoot;
import com.mr208.unwired.common.item.equipment.MoonBoot;
import com.mr208.unwired.common.item.equipment.RebreatherHelm;
import com.mr208.unwired.common.item.equipment.VisorHelm;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class ModGroups
{
	public static ItemGroup mainGroup = new ItemGroup(UnWIRED.MOD_ID) {
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ModItems.inert_goo);
		}
	};
}
