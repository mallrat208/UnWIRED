package com.mr208.unwired.common.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mr208.unwired.UnWIRED;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.RedstoneSide;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SmartglassBlock extends AbstractUnWIREDGlass
{
	public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);
	
	private static final Direction[] facingsHorizontal = {Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH};
	private static final Direction[] facingsVertical = {Direction.DOWN,Direction.UP};
	private static final Direction[] facings = ArrayUtils.addAll(facingsVertical, facingsHorizontal);
	
	private List<BlockPos> turnOff = Lists.newArrayList();
	private List<BlockPos> turnOn = Lists.newArrayList();
	private final Set<BlockPos> updatedGlass = Sets.newLinkedHashSet();
	
	private static final Vec3i[] surroundingBlocksOffset;
	static {
		Set<Vec3i> set = Sets.newLinkedHashSet();
		for (Direction facing : facings) {
			set.add(facing.getDirectionVec());
		}
		for (Direction facing1 : facings) {
			Vec3i v1 = facing1.getDirectionVec();
			for (Direction facing2 : facings) {
				Vec3i v2 = facing2.getDirectionVec();
				set.add(new Vec3i(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ()));
			}
		}
		set.remove(new Vec3i(0, 0, 0));
		surroundingBlocksOffset = set.toArray(new Vec3i[set.size()]);
	}
	
	private boolean canProvidePower = false;
	
	public SmartglassBlock()
	{
		setRegistryName(UnWIRED.MOD_ID,"smartglass");
		createItemBlock();
		setDefaultState(getDefaultState().with(POWER,0));
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState blockState, IBlockReader blockReader, BlockPos blockPos)
	{
		return blockState.get(POWER)> 0;
	}
	
	@Override
	public boolean isVariableOpacity()
	{
		return true;
	}
	
	@Override
	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return state.get(POWER) == 0 ? 0 : 255;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		builder.add(POWER);
	}
	
	@Override
	public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return !this.canProvidePower?0:blockState.getWeakPower(blockAccess, pos, side);
	}
	
	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		if(!world.isRemote())
		{
			if(state.isValidPosition(world, pos))
			{
				this.updateSurroundingGlass(world, pos);
			}
			else
			{
				spawnDrops(state, world, pos);
				world.removeBlock(pos, false);
			}
		}
	}
	
	@Override
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		if(!this.canProvidePower) {
			return 0;
		} else {
			// Changed implementation to use getSidesToPower() to avoid duplicate implementation
			if (side == Direction.UP || getSidesToPower((World)blockAccess, pos).contains(side) && ((World)blockAccess).getBlockState(pos.offset(side.getOpposite())).getBlock() == this) {
				return blockState.get(POWER);
			} else {
				return 0;
			}
		}	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		if(!worldIn.isRemote)
		{
			this.updateSurroundingGlass(worldIn, pos);
			for(Vec3i vec: surroundingBlocksOffset)
				worldIn.notifyNeighborsOfStateChange(pos.add(vec), this);
		}
	}
	
	@Override
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
	{
		return blockState.get(POWER);
	}
	
	private boolean isPowerSourceAt(IWorldReader worldIn, BlockPos pos, Direction side) {
		BlockPos blockpos = pos.offset(side);
		BlockState iblockstate = worldIn.getBlockState(blockpos);
		boolean flag = iblockstate.isNormalCube(worldIn, blockpos);
		boolean flag1 = worldIn.getBlockState(pos.up()).isNormalCube(worldIn, pos.up());
		return !flag1&&flag&&canConnectUpwardsTo(worldIn, blockpos.up())||(canConnectTo(iblockstate, side)||(iblockstate.getBlock()==Blocks.REPEATER&&iblockstate.get(RepeaterBlock.HORIZONTAL_FACING)==side||!flag&&canConnectUpwardsTo(worldIn, blockpos.down())));
	}
	
	private int getMaxCurrentStrength(IWorldReader worldIn, BlockPos pos, int strength) {
		if(!(worldIn.getBlockState(pos).getBlock() instanceof SmartglassBlock)) {
			return strength;
		} else {
			int i = worldIn.getBlockState(pos).get(POWER);
			return i > strength?i:strength;
		}
	}
	
	private void updateSurroundingGlass(IWorldReader worldIn, BlockPos pos) {
		
		calculateCurrentChanges(worldIn, pos);
		Set<BlockPos> blocksNeedingUpdate = Sets.newLinkedHashSet();
		
		for (BlockPos posi : updatedGlass) {
			addBlocksNeedingUpdate(worldIn, posi, blocksNeedingUpdate);
		}
		
		Iterator<BlockPos> it = Lists.newLinkedList(updatedGlass).descendingIterator();
		while (it.hasNext()) {
			addAllSurroundingBlocks(it.next(), blocksNeedingUpdate);
		}
		
		blocksNeedingUpdate.removeAll(updatedGlass);
		updatedGlass.clear();
		
		for (BlockPos posi : blocksNeedingUpdate) {
			((World)worldIn).notifyNeighborsOfStateChange(posi,this);
		}
	}
	
	private void calculateCurrentChanges(IWorldReader worldIn, BlockPos position)
	{
		if (worldIn.getBlockState(position).getBlock() instanceof SmartglassBlock) {
			turnOff.add(position);
		} else {
			checkSurroundingGlass(worldIn, position);
		}
		
		while (!turnOff.isEmpty()) {
			BlockPos pos = turnOff.remove(0);
			BlockState state = worldIn.getBlockState(pos);
			int oldPower = state.get(POWER);
			this.canProvidePower = false;
			int blockPower = ((World)worldIn).getRedstonePowerFromNeighbors(pos);
			this.canProvidePower = true;
			int wirePower = getSurroundingGlassPower(worldIn, pos);
			
			wirePower--;
			int newPower = Math.max(blockPower, wirePower);
			
			if (newPower < oldPower) {
				
				if (blockPower > 0 && !turnOn.contains(pos)) {
					turnOn.add(pos);
				}
				
				setGlassState(worldIn, pos, state, 0);
				
			} else if (newPower > oldPower) {
				
				setGlassState(worldIn, pos, state, newPower);
			}
			checkSurroundingGlass(worldIn, pos);
			//worldIn.notifyLightSet(pos);
		}
		
		while (!turnOn.isEmpty()) {
			BlockPos pos = turnOn.remove(0);
			BlockState state = worldIn.getBlockState(pos);
			int oldPower = state.get(POWER);
			this.canProvidePower = false;
			int blockPower = ((World)worldIn).getRedstonePowerFromNeighbors(pos);
			this.canProvidePower = true;
			int wirePower = getSurroundingGlassPower(worldIn, pos);
			
			wirePower--;
			int newPower = Math.max(blockPower, wirePower);
			
			if (newPower > oldPower) {
				setGlassState(worldIn, pos, state, newPower);
			}
			checkSurroundingGlass(worldIn, pos);
			//worldIn.notifyLightSet(pos);
		}
		turnOff.clear();
		turnOn.clear();
	}
	
	private void addGlassToList(IWorldReader worldIn, BlockPos pos, int otherPower)
	{
		BlockState state = worldIn.getBlockState(pos);
		if (state.getBlock() instanceof SmartglassBlock)
		{
			int power = state.get(POWER);
			if (power < (otherPower-1) && !turnOn.contains(pos))
			{
				turnOn.add(pos);
			}
			
			if (power > otherPower && !turnOff.contains(pos))
			{
				turnOff.add(pos);
			}
		}
	}
	
	private void checkSurroundingGlass(IWorldReader worldIn, BlockPos pos)
	{
		BlockState state = worldIn.getBlockState(pos);
		int ownPower = 0;
		if (state.getBlock() instanceof SmartglassBlock)
		{
			ownPower = state.get(POWER);
		}
		for (Direction facing : facingsHorizontal)
		{
			BlockPos offsetPos = pos.offset(facing);
			if (facing.getAxis().isHorizontal())
			{
				addGlassToList(worldIn, offsetPos, ownPower);
			}
		}
		for (Direction facingVertical : facingsVertical)
		{
			BlockPos offsetPos = pos.offset(facingVertical);
			for (Direction facingHorizontal : facingsHorizontal)
			{
				addGlassToList(worldIn, offsetPos.offset(facingHorizontal), ownPower);
			}
			
			addGlassToList(worldIn, offsetPos, ownPower);
		}
		
	}
	
	private int getSurroundingGlassPower(IWorldReader worldIn, BlockPos pos) {
		int glassPower = 0;
		for(Direction enumfacing : Direction.Plane.HORIZONTAL)
		{
			BlockPos offsetPos = pos.offset(enumfacing);
			
			glassPower = this.getMaxCurrentStrength(worldIn, offsetPos, glassPower);
			
			glassPower = this.getMaxCurrentStrength(worldIn, offsetPos.up(), glassPower);
			
			glassPower = this.getMaxCurrentStrength(worldIn, offsetPos.down(), glassPower);
		}
		
		glassPower = this.getMaxCurrentStrength(worldIn, pos.up(), glassPower);
		
		glassPower = this.getMaxCurrentStrength(worldIn, pos.down(), glassPower);
		
		return glassPower;
	}
	
	private void addBlocksNeedingUpdate(IWorldReader worldIn, BlockPos pos, Set<BlockPos> set)
	{
		List<Direction> connectedSides = getSidesToPower(worldIn, pos);
		
		for (Direction facing : facings) {
			BlockPos offsetPos = pos.offset(facing);
			
			if (connectedSides.contains(facing.getOpposite()) || facing == Direction.DOWN || (facing.getAxis().isHorizontal() && canConnectTo(worldIn.getBlockState(offsetPos), facing))) {
				if (canBlockBePoweredFromSide(worldIn.getBlockState(offsetPos), facing, true)) set.add(offsetPos);
			}
		}
		
		for (Direction facing : facings) {
			BlockPos offsetPos = pos.offset(facing);
			if (connectedSides.contains(facing.getOpposite()) || facing == Direction.DOWN) {
				if (worldIn.getBlockState(offsetPos).isNormalCube(worldIn, offsetPos)) {
					for (Direction facing1 : facings) {
						if (canBlockBePoweredFromSide(worldIn.getBlockState(offsetPos.offset(facing1)), facing1, false)) set.add(offsetPos.offset(facing1));
					}
				}
			}
		}
	}
	
	private boolean canBlockBePoweredFromSide(BlockState state, Direction side, boolean isWire)
	{
		/* if (state.getBlock() instanceof RedstoneDiodeBlock&& state.get(HorizontalBlock.HORIZONTAL_FACING) != side.getOpposite())
		{
			return isWire&&state.getBlock() instanceof ComparatorBlock &&state.get(HorizontalBlock.HORIZONTAL_FACING).getAxis()!=side.getAxis()&&side.getAxis().isHorizontal();
		}
		if (state.getBlock() instanceof RedstoneTorchBlock) {
			return !isWire&&state.get(HorizontalBlock.HORIZONTAL_FACING)==side;
		}
		if (state.getBlock() instanceof SmartglassBlock)
			return true; */
		
		return (state.getBlock() instanceof RedstoneWireBlock && side !=Direction.DOWN);
		
		//return true;
	}
	
	private List<Direction> getSidesToPower(IWorldReader worldIn, BlockPos pos)
	{
		List<Direction> retval = new ArrayList<>();
		for (Direction facing : facingsHorizontal) {
			if (isPowerSourceAt(worldIn, pos, facing)) retval.add(facing);
		}
		if (retval.isEmpty()) return Lists.newArrayList(facingsHorizontal);
		boolean northsouth = retval.contains(Direction.NORTH) || retval.contains(Direction.SOUTH);
		boolean eastwest = retval.contains(Direction.EAST) || retval.contains(Direction.WEST);
		if (northsouth) {
			retval.remove(Direction.EAST);
			retval.remove(Direction.WEST);
		}
		if (eastwest) {
			retval.remove(Direction.NORTH);
			retval.remove(Direction.SOUTH);
		}
		return retval;
	}
	
	private void addAllSurroundingBlocks(BlockPos pos, Set<BlockPos> set)
	{
		for (Vec3i vect : surroundingBlocksOffset) {
			set.add(pos.add(vect));
		}
	}
	
	private void setGlassState(IWorldReader worldIn, BlockPos pos, BlockState state, int power)
	{
		state = state.with(POWER, power);
		((World)worldIn).setBlockState(pos, state, 2);
		updatedGlass.add(pos);
	}
	
	private static boolean canConnectUpwardsTo(IWorldReader worldIn, BlockPos pos) {
		return canConnectUpwardsTo(worldIn.getBlockState(pos));
	}
	
	private static boolean canConnectUpwardsTo(BlockState state) {
		return canConnectTo(state, null);
	}
	
	private static boolean canConnectTo(BlockState blockState, @Nullable Direction side) {
		Block block = blockState.getBlock();
		if(block instanceof SmartglassBlock)
		{
			return true;
		} else if(block == Blocks.REDSTONE_WIRE) {
			return side!=Direction.DOWN;
		} else if(blockState.getBlock() == Blocks.REPEATER) {
			Direction enumfacing =blockState.get(RepeaterBlock.HORIZONTAL_FACING);
			return enumfacing == side || enumfacing.getOpposite() == side;
		} else if(Blocks.OBSERVER == blockState.getBlock()) {
			return side == blockState.get(ObserverBlock.FACING);
		} else {
			return blockState.canProvidePower() && side != null;
		}
	}
}
