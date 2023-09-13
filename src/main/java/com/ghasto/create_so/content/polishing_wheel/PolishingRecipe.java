package com.ghasto.create_so.content.polishing_wheel;

import com.ghasto.create_so.ModRecipeTypes;
import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;

import net.minecraft.world.Container;
import net.minecraft.world.level.Level;

public class PolishingRecipe extends AbstractCrushingRecipe {

	public PolishingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
		super(ModRecipeTypes.POLISHING, params);
	}

	@Override
	public boolean matches(Container inv, Level worldIn) {
		if (inv.isEmpty())
			return false;
		return ingredients.get(0)
				.test(inv.getItem(0));
	}

	@Override
	protected int getMaxOutputCount() {
		return 7;
	}

}

