package com.ghasto.create_so.compat.emi;

import com.ghasto.create_so.content.polishing_wheel.PolishingRecipe;
import com.simibubi.create.compat.emi.recipes.CreateEmiRecipe;
import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;

public class PolishingEmiRecipe extends CreateEmiRecipe<AbstractCrushingRecipe> {

	public PolishingEmiRecipe(PolishingRecipe recipe) {
		super(ModEMICompat.POLISHING, recipe, 134, 110);
		ResourceLocation rid = recipe.getId();
		this.id = new ResourceLocation("emi", "create_so/polishing/" + rid.getNamespace() + "/" + rid.getPath());
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		addTexture(widgets, AllGuiTextures.JEI_DOWN_ARROW, 51, 11);

		addSlot(widgets, input.get(0), 29, 6);

		int xOff = -output.size() * 19 / 2;
		for (int i = 0; i < output.size(); i++) {
			addSlot(widgets, output.get(i), 67 + xOff + 19 * i, 82).recipeContext(this);
		}

		ModEmiAnimations.addPolishingWheels(widgets, 41, 63);
	}
}
