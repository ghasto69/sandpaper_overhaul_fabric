package com.ghasto.create_so.content.polishing_wheel;

import java.util.List;
import java.util.Optional;

import com.ghasto.create_so.ModRecipeTypes;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PolishingWheelControllerBlockEntity extends CrushingWheelControllerBlockEntity {
    public PolishingWheelControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Optional<ProcessingRecipe<Container>> findRecipe() {
		return ModRecipeTypes.POLISHING.find(inventory, level);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(new DirectBeltInputBehaviour(this).onlyInsertWhen(this::supportsDirectBeltInput));
	}
	private boolean supportsDirectBeltInput(Direction side) {
		BlockState blockState = getBlockState();
		if (blockState == null)
			return false;
		Direction direction = blockState.getValue(PolishingWheelControllerBlock.FACING);
		return direction == Direction.DOWN || direction == side;
	}
}
