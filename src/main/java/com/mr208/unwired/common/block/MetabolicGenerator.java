package com.mr208.unwired.common.block;

import com.mr208.unwired.common.block.base.UWBlock;
import com.mr208.unwired.common.block.tile.MetabolicGenTile;
import com.mr208.unwired.common.content.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class MetabolicGenerator extends UWBlock implements IWaterLoggable, ITileEntityProvider
{
	public static final DirectionProperty FACING =BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty PROCESSING = BlockStateProperties.LIT;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public MetabolicGenerator()
	{
		super("generator_metabolic", Block.Properties.create(ModBlocks.Materials.MACHINE).hardnessAndResistance(2f));
		this.setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(PROCESSING, false).with(WATERLOGGED, false));
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> p_206840_1_)
	{
		p_206840_1_.add(FACING, PROCESSING,WATERLOGGED);
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader iBlockReader)
	{
		return new MetabolicGenTile();
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return createNewTileEntity(world);
	}
}
