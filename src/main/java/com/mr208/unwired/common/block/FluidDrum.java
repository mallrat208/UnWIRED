package com.mr208.unwired.common.block;

import com.mr208.unwired.common.block.base.UWBlock;
import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.content.ModBlocks.Materials;
import com.mr208.unwired.common.item.DrumItem;
import com.mr208.unwired.common.tile.FluidDrumTile;
import com.mr208.unwired.common.tile.StorageCrateTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class FluidDrum extends UWBlock implements IWaterLoggable, ITileEntityProvider
{
	public static final DirectionProperty FACING =BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	private final Drum drumType;
	
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(1,0,1,15,16,15);
	
	public FluidDrum(Drum type)
	{
		super("drum_" + type.getName(), Block.Properties.create(ModBlocks.Materials.MACHINE).hardnessAndResistance(2.5f).harvestTool(ToolType.PICKAXE));
		this.drumType = type;
		setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false).with(COLOR, DyeColor.LIGHT_GRAY));
	}
	
	@Override
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player)
	{
		if(!worldIn.isRemote)
		{
			
			DyeColor color=DyeColor.getColor(player.getHeldItemMainhand());
			if(color!=null&&state.get(COLOR)!=color)
			{
				worldIn.setBlockState(pos, state.with(COLOR, color));
				worldIn.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1f, 1f);
				
				if(!player.isCreative())
					player.getHeldItemMainhand().shrink(1);
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(!worldIn.isRemote)
		{
			TileEntity tile = worldIn.getTileEntity(pos);
			if(tile!= null && tile instanceof INamedContainerProvider)
			{
				NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)tile, tile.getPos());
			}
			
			return true;
		}
		
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}
	
	@Nullable
	@Override
	public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		
		if(tile instanceof FluidDrumTile)
			return (INamedContainerProvider)tile;
		
		return null;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		builder.add(FACING, WATERLOGGED,COLOR);
	}
	
	@Override
	public IFluidState getFluidState(BlockState state)
	{
		return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader iBlockReader)
	{
		return new FluidDrumTile(this.drumType);
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return createNewTileEntity(world);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		return DrumItem.getDrumStackForColor(state.get(COLOR));
	}
	
	public enum Drum implements IStringSerializable
	{
		POLYMER(16000),
		CARBON(32000),
		PLASTEEL(64000);
		
		private final int capacity;
		
		Drum(int capacity)
		{
			this.capacity = capacity;
		}
		
		@Override
		public String getName()
		{
			return this.toString().toLowerCase();
		}
		
		public int getCapacity()
		{
			return capacity;
		}
	}
}
