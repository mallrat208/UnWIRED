package com.mr208.unwired.common.block;

import com.mr208.unwired.common.block.base.UWBlock;
import com.mr208.unwired.common.content.ModBlocks;
import com.mr208.unwired.common.tile.PolymerizedLogTile;
import com.mr208.unwired.libs.TagLib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants.BlockFlags;

import javax.annotation.Nullable;
import java.util.Random;

public class PolymerizedLog extends UWBlock implements ITileEntityProvider
{
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_3;
	
	public PolymerizedLog()
	{
		super("polymerized_log", Block.Properties.create(Material.WOOD).harvestTool(ToolType.AXE).tickRandomly());
		this.setDefaultState(this.stateContainer.getBaseState().with(AGE, 0));
	}
	
	@Override
	public int tickRate(IWorldReader worldIn)
	{
		return 10;
	}
	
	@Override
	public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		
		if(tile!=null && tile instanceof PolymerizedLogTile)
		{
			return ((PolymerizedLogTile)tile).getCachedState().getBlockHardness(worldIn, pos);
		}
		
		return super.getBlockHardness(blockState, worldIn, pos);
	}
	
	@Override
	public void randomTick(BlockState state, World worldIn, BlockPos pos, Random random)
	{
		if(!worldIn.isAreaLoaded(pos,1))
			return;
		
		int age = state.get(AGE);
		
		if(age < 2)
		{
			worldIn.setBlockState(pos, state.with(AGE, age+1),10);
		}
		else if(age == 2)
		{
			PolymerizedLogTile te = (PolymerizedLogTile)worldIn.getTileEntity(pos);
			BlockState cached = te!= null && te.getCachedState() != null ? te.getCachedState() : Blocks.AIR.getDefaultState();
			
			if(cached.getProperties().contains(BlockStateProperties.AXIS))
			{
				worldIn.setBlockState(pos, ModBlocks.brittle_log.getDefaultState().with(BlockStateProperties.AXIS,cached.get(BlockStateProperties.AXIS)), BlockFlags.BLOCK_UPDATE + BlockFlags.NOTIFY_NEIGHBORS);
			}
			else
			{
				worldIn.setBlockState(pos, ModBlocks.brittle_log.getDefaultState(), BlockFlags.BLOCK_UPDATE + BlockFlags.NOTIFY_NEIGHBORS);
			}
			worldIn.playSound(null, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			
			
			for(Direction direction: Direction.values())
			{
				BlockPos offset = pos.add(direction.getXOffset(),direction.getYOffset(), direction.getZOffset());
				BlockState stateIn = worldIn.getBlockState(offset);
				
				if(stateIn.getBlock().getTags().contains(TagLib.BLOCK_LOGS) && !stateIn.getBlock().getTags().contains(TagLib.BLACKLIST_GOO))
				{
					worldIn.setBlockState(offset, getDefaultState());
					TileEntity teNew = worldIn.getTileEntity(offset);
					if(teNew!= null && teNew instanceof PolymerizedLogTile)
					{
						((PolymerizedLogTile)teNew).setCachedState(stateIn);
						te.markDirty();
					}
					
					worldIn.notifyBlockUpdate(offset, getDefaultState(), getDefaultState(), 10 );
				}
			}
		}
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			
			if(tileEntity!= null && tileEntity instanceof PolymerizedLogTile)
			{
				if(state.get(AGE)<2)
				{
					Block.spawnDrops(((PolymerizedLogTile)tileEntity).getCachedState(), worldIn, pos);
				}
			}
		}
	}
	
	
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		builder.add(AGE);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return createNewTileEntity(world);
	}
	
	@Override
	public boolean hasTileEntity()
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader iBlockReader)
	{
		return new PolymerizedLogTile();
	}
}
