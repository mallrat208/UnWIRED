package com.mr208.unwired.common.block;

import com.mr208.unwired.common.block.base.UWBlock;
import com.mr208.unwired.common.inventory.ResequencerContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Plane;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Resequencer extends UWBlock
{
	public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
	
	protected static final VoxelShape VERT_SLAB_NORTH;
	protected static final VoxelShape VERT_SLAB_SOUTH;
	protected static final VoxelShape VERT_SLAB_EAST;
	protected static final VoxelShape VERT_SLAB_WEST;
	
	public Resequencer()
	{
		super("resequencer", Block.Properties.create(Material.IRON));
		setDefaultState(this.getDefaultState().with(DIRECTION, Direction.WEST));
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		builder.add(DIRECTION);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		player.openContainer(state.getContainer(worldIn, pos));
		return true;
	}
	
	@Nullable
	@Override
	public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos)
	{
		return new SimpleNamedContainerProvider((id, playerInventory, player) -> new ResequencerContainer(id, playerInventory, IWorldPosCallable.of(worldIn, pos)), new TranslationTextComponent("container.unwired.resequencer"));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand)
	{
		if(Plane.HORIZONTAL.test(facing))
		{
			return this.getDefaultState().with(DIRECTION, facing);
		}
		
		return super.getStateForPlacement(state, facing, state2, world, pos1, pos2, hand);
	}
	
	@Override
	public VoxelShape getShape(BlockState stateIn, IBlockReader blockReader, BlockPos posIn, ISelectionContext context)
	{
		switch(stateIn.get(DIRECTION))
		{
			case NORTH:
				return VERT_SLAB_NORTH;
			case SOUTH:
				return VERT_SLAB_SOUTH;
			case EAST:
				return VERT_SLAB_EAST;
			default:
				return VERT_SLAB_WEST;
		}
	}
	
	static
	{
		VERT_SLAB_NORTH = Block.makeCuboidShape(0,0,0, 16,16,8);
		VERT_SLAB_SOUTH = Block.makeCuboidShape(0,0,8, 16,16,16);
		VERT_SLAB_EAST = Block.makeCuboidShape(8,0,0,16,16,16);
		VERT_SLAB_WEST = Block.makeCuboidShape(0,0,0,8,16,16);
	}
}
