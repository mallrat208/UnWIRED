package com.mr208.unwired.common.block;

import com.mr208.unwired.common.block.base.UWBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Rotation;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BrittleLog extends UWBlock
{
	public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
	
	public BrittleLog()
	{
		super("brittle_log", Block.Properties.create(Material.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(1));
		this.setDefaultState(this.getDefaultState().with(AXIS, Axis.Y));
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		builder.add(AXIS);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(AXIS, context.getFace().getAxis());
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		switch(rot) {
			case COUNTERCLOCKWISE_90:
			case CLOCKWISE_90:
				switch(state.get(AXIS)) {
					case X:
						return state.with(AXIS, Axis.Z);
					case Z:
						return state.with(AXIS, Axis.X);
					default:
						return state;
				}
			default:
				return state;
		}
	}
}
