package com.mr208.unwired.common.content;

import com.mr208.unwired.Config;
import com.mr208.unwired.UnWIRED;
import com.mr208.unwired.common.block.FluidDrum.Drum;
import com.mr208.unwired.common.block.StorageCrate.Crate;
import com.mr208.unwired.common.item.ActivatedGoo;
import com.mr208.unwired.common.item.CrateItem;
import com.mr208.unwired.common.item.DrumItem;
import com.mr208.unwired.common.item.EnergyItem;
import com.mr208.unwired.common.item.FluidCanister;
import com.mr208.unwired.common.item.FluidCanister.CanisterType;
import com.mr208.unwired.common.item.LabelMarker;
import com.mr208.unwired.common.item.SafteyShears;
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
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;
import java.util.List;

@ObjectHolder(UnWIRED.MOD_ID)
@EventBusSubscriber(bus = Bus.MOD)
public class ModItems
{
	public static final Item soybean = null;
	public static final Item dust_polymer = null;
	public static final Item ingot_polymer = null;
	public static final Item block_polymer = null;
	public static final Item plate_polymer = null;
	public static final Item inert_goo = null;
	public static final Item active_goo = null;
	public static final Item nano_fluid_bucket = null;
	public static final Item grey_goo_spawn_egg = null;
	public static final Item resequencer = null;
	public static final Item frame_plastic = null;
	public static final Item frame_carbon = null;
	public static final Item frame_plasteel = null;
	public static final Item helmet_rebreather = null;
	public static final Item helmet_visor = null;
	public static final Item boots_flippers = null;
	public static final Item boots_moon = null;
	public static final Item crate_polymer_light_gray = null;
	public static final Item drum_polymer_light_gray = null;
	public static final Item marker_black = null;
	public static final Item generator_metabolic = null;
	public static final Item cell_bio = null;
	public static final Item goo_creche = null;
	public static final Item canister_polymer = null;
	public static final Item saftey_shears = null;
	
	@SubscribeEvent
	public static void onItemRegistryEvent(final RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();
		
		registry.registerAll(
				new UWBase("ingot_polymer"),
				new UWBase("plate_polymer"),
				new UWBase("dust_polymer"),
				new UWBase("inert_goo"),
				new ActivatedGoo(),
				new UWBucket("nano_fluid", () -> ModFluids.nano_fluid_source),
				new UWSpawnItem(ModEntities.grey_goo, 0x616161, 0x343434,"grey_goo"),
				new UWBlockItem(ModBlocks.plexiglass),
				new UWBlockItem(ModBlocks.smartglass),
				new SoybeanItem(),
				new UWBlockItem(ModBlocks.frame_polymer),
				new UWBlockItem(ModBlocks.block_polymer),
				new UWDirectionalBlockItem(ModBlocks.resequencer),
				new RebreatherHelm(),
				new VisorHelm(),
				new FlippersBoot(),
				new MoonBoot(),
				new UWDirectionalBlockItem(ModBlocks.crate_polymer){
					@Override
					public void fillItemGroup(ItemGroup p_150895_1_, NonNullList<ItemStack> p_150895_2_)
					{
					
					}
				},
				new UWDirectionalBlockItem(ModBlocks.generator_metabolic),
				new UWDirectionalBlockItem(ModBlocks.goo_creche){
					@Override
					@OnlyIn(Dist.CLIENT)
					public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
					{
						tooltip.add(new TranslationTextComponent("tooltip.unwired.wip"));
						super.addInformation(stack, worldIn, tooltip, flagIn);
					}
				},
				new EnergyItem("cell_bio", Config.TIER_1_CELL_CAPACITY.get()),
				new FluidCanister(CanisterType.POLYMER)
				//new SafteyShears()
		);
		
		for(DyeColor color : DyeColor.values())
		{
			registry.register(new LabelMarker(color));
			registry.register(new CrateItem(ModBlocks.crate_polymer, Crate.POLYMER, color));
			registry.register(new DrumItem(ModBlocks.drum_polymer, Drum.POLYMER, color));
		}
	}
}
