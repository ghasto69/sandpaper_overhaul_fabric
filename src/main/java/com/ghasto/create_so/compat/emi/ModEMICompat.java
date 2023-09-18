package com.ghasto.create_so.compat.emi;

import com.ghasto.create_so.CreateSandpaperOverhaul;
import com.ghasto.create_so.ModBlocks;
import com.ghasto.create_so.ModRecipeTypes;
import com.ghasto.create_so.content.polishing_wheel.PolishingRecipe;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.emi.DoubleItemIcon;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModEMICompat implements EmiPlugin {
	public static final Map<ResourceLocation, EmiRecipeCategory> ALL = new LinkedHashMap<>();
	public static final EmiRecipeCategory
			POLISHING = register("polishing", DoubleItemIcon.of(ModBlocks.POLISHING_WHEEL.get(), AllItems.SAND_PAPER.get()));
	@Override
	public void register(EmiRegistry registry) {
		ALL.forEach((id, category) -> registry.addCategory(category));
		registry.addWorkstation(POLISHING, EmiStack.of(ModBlocks.POLISHING_WHEEL.get()));
		RecipeManager manager = registry.getRecipeManager();
		List<PolishingRecipe> polishingRecipes = (List<PolishingRecipe>) (List) manager.getAllRecipesFor(ModRecipeTypes.POLISHING.getType());
		for (PolishingRecipe recipe : polishingRecipes) {
			registry.addRecipe(new PolishingEmiRecipe(recipe));
		}
	}
	private static EmiRecipeCategory register(String name, EmiRenderable icon) {
		ResourceLocation id = CreateSandpaperOverhaul.id(name);
		EmiRecipeCategory category = new EmiRecipeCategory(id, icon);
		ALL.put(id, category);
		return category;
	}
}
