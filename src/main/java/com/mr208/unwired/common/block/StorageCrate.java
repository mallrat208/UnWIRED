package com.mr208.unwired.common.block;

import com.mr208.unwired.common.block.base.UWBlock;
import com.mr208.unwired.common.tile.StorageCrateTile;
import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.item.CrateItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.Direction.Plane;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class StorageCrate extends UWBlock implements IWaterLoggable, ITileEntityProvider
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	private final Crate crateType;
	
	protected static final VoxelShape SHAPE;
	
	public StorageCrate(Crate type)
	{
		super("crate_"+type.getName(), Block.Properties.create(ModBlocks.Materials.MACHINE).hardnessAndResistance(2.5f).harvestTool(ToolType.PICKAXE));
		this.crateType = type;
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
		if(worldIn.isRemote())
		{
			return true;
		}
		else
		{
				INamedContainerProvider containerProvider=this.getContainer(state, worldIn, pos);
				if(containerProvider!=null)
				{
					worldIn.playSound(null, pos, SoundEvents.ENTITY_SHULKER_CLOSE, SoundCategory.BLOCKS, 1f,1f);
					player.openContainer(containerProvider);
				}
				
			return true;
		}
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			TileEntity tile = worldIn.getTileEntity(pos);
			if(tile instanceof IInventory)
			{
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tile);
				worldIn.updateComparatorOutputLevel(pos,this);
			}
			
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
	
	@Nullable
	@Override
	public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		
		if(tile instanceof StorageCrateTile)
			return (INamedContainerProvider)tile;
		
		return null;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		builder.add(FACING,WATERLOGGED,COLOR);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand)
	{
		if(Plane.HORIZONTAL.test(facing))
		{
			return this.getDefaultState().with(FACING, facing);
		}
		
		return super.getStateForPlacement(state, facing, state2, world, pos1, pos2, hand);
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
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}
	
	//todo: This definitely needs a tile entity to work
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return createNewTileEntity(world);
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader iBlockReader)
	{
		return new StorageCrateTile(this.crateType);
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		return CrateItem.getCrateStackForColor(state.get(COLOR));
	}
	
	public enum Crate implements IStringSerializable
	{
		POLYMER(36,4),
		CARBON(45,5),
		PLASTEEL(54,6);
		
		private final int slotCount;
		private final int rows;
		
		Crate(int slotCount, int rows)
		{
			this.slotCount = slotCount;
			this.rows = rows;
		}
		
		@Override
		public String getName()
		{
			return this.toString().toLowerCase();
		}
		
		public int getSlotCount()
		{
			return this.slotCount;
		}
		
		public int getRows()
		{
			return this.rows;
		}
	}
	
	static
	{
		SHAPE = Block.makeCuboidShape(1,0,1,15,15,15);
	}
}
