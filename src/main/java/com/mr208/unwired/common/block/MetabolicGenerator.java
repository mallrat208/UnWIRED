package com.mr208.unwired.common.block;

import com.mr208.unwired.common.block.base.UWBlock;
import com.mr208.unwired.common.tile.MetabolicGenTile;
import com.mr208.unwired.common.content.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

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
