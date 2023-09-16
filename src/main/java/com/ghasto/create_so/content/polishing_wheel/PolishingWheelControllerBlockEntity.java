package com.ghasto.create_so.content.polishing_wheel;

import com.ghasto.create_so.ModRecipeTypes;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class PolishingWheelControllerBlockEntity extends CrushingWheelControllerBlockEntity {

	public PolishingWheelControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public Optional<ProcessingRecipe<Container>> findRecipe() {
		return ModRecipeTypes.POLISHING.find(inventory, level);
	}
}
