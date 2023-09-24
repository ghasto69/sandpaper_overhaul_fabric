package com.ghasto.create_so.compat.rei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.ghasto.create_so.CreateSandpaperOverhaul;
import com.ghasto.create_so.ModBlocks;
import com.ghasto.create_so.ModRecipeTypes;
import com.ghasto.create_so.content.polishing_wheel.PolishingRecipe;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.compat.rei.CreateREI;
import com.simibubi.create.compat.rei.DoubleItemIcon;
import com.simibubi.create.compat.rei.EmptyBackground;
import com.simibubi.create.compat.rei.ItemIcon;
import com.simibubi.create.compat.rei.category.CreateRecipeCategory;
import com.simibubi.create.compat.rei.display.CreateDisplay;
import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;

import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

public class ModReiCompat implements REIClientPlugin {
	private static final ResourceLocation ID = CreateSandpaperOverhaul.id("rei_plugin");

	private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();
	private void loadCategories() {
		allCategories.clear();

		CreateRecipeCategory<?>
				polishing = builder(PolishingRecipe.class)
						.addTypedRecipes(ModRecipeTypes.POLISHING)
						.catalyst(() -> ModBlocks.POLISHING_WHEEL.get().asItem())
						.doubleItemIcon(ModBlocks.POLISHING_WHEEL.get(), AllItems.SAND_PAPER.get())
						.emptyBackground(177, 106)
						.build("polishing", PolishingCategory::new);

	}
	@Override
	public String getPluginProviderName() {
		return ID.toString();
	}
	@Override
	public void registerCategories(CategoryRegistry registry) {
		loadCategories();
		allCategories.forEach(category -> {
			registry.add(category);
			category.registerCatalysts(registry);
		});
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		allCategories.forEach(c -> c.registerRecipes(registry));
	}
	private <T extends Recipe<?>> CategoryBuilder<T> builder(Class<? extends T> recipeClass) {
		return new CategoryBuilder<>(recipeClass);
	}
	private class CategoryBuilder<T extends Recipe<?>> {
		private final Class<? extends T> recipeClass;
		private Predicate<CRecipes> predicate = cRecipes -> true;

		private Renderer background;
		private Renderer icon;

		private int width;
		private int height;

		private Function<T, ? extends CreateDisplay<T>> displayFactory;

		private final List<Consumer<List<T>>> recipeListConsumers = new ArrayList<>();
		private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();

		public CategoryBuilder(Class<? extends T> recipeClass) {
			this.recipeClass = recipeClass;
		}

		public CategoryBuilder<T> enableIf(Predicate<CRecipes> predicate) {
			this.predicate = predicate;
			return this;
		}

		public CategoryBuilder<T> enableWhen(Function<CRecipes, ConfigBase.ConfigBool> configValue) {
			predicate = c -> configValue.apply(c).get();
			return this;
		}

		public CategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
			recipeListConsumers.add(consumer);
			return this;
		}

		public CategoryBuilder<T> addRecipes(Supplier<Collection<? extends T>> collection) {
			return addRecipeListConsumer(recipes -> recipes.addAll(collection.get()));
		}

		public CategoryBuilder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
			return addTypedRecipes(recipeTypeEntry::getType);
		}

		public CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType) {
			return addRecipeListConsumer(recipes -> CreateREI.<T>consumeTypedRecipes(recipes::add, recipeType.get()));
		}

		public CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType, Function<Recipe<?>, T> converter) {
			return addRecipeListConsumer(recipes -> CreateREI.<T>consumeTypedRecipes(recipe -> recipes.add(converter.apply(recipe)), recipeType.get()));
		}

		public CategoryBuilder<T> addTypedRecipesIf(Supplier<RecipeType<? extends T>> recipeType, Predicate<Recipe<?>> pred) {
			return addRecipeListConsumer(recipes -> CreateREI.<T>consumeTypedRecipes(recipe -> {
				if (pred.test(recipe)) {
					recipes.add(recipe);
				}
			}, recipeType.get()));
		}


		public CategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
			catalysts.add(supplier);
			return this;
		}

		public CategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
			return catalystStack(() -> new ItemStack(supplier.get()
					.asItem()));
		}

		public CategoryBuilder<T> icon(Renderer icon) {
			this.icon = icon;
			return this;
		}

		public CategoryBuilder<T> itemIcon(ItemLike item) {
			icon(new ItemIcon(() -> new ItemStack(item)));
			return this;
		}

		public CategoryBuilder<T> doubleItemIcon(ItemLike item1, ItemLike item2) {
			icon(new DoubleItemIcon(() -> new ItemStack(item1), () -> new ItemStack(item2)));
			return this;
		}

		public CategoryBuilder<T> background(Renderer background) {
			this.background = background;
			return this;
		}

		public CategoryBuilder<T> emptyBackground(int width, int height) {
			background(new EmptyBackground(width, height));
			dimensions(width, height);
			return this;
		}

		public CategoryBuilder<T> width(int width) {
			this.width = width;
			return this;
		}

		public CategoryBuilder<T> height(int height) {
			this.height = height;
			return this;
		}

		public CategoryBuilder<T> dimensions(int width, int height) {
			width(width);
			height(height);
			return this;
		}

		public CategoryBuilder<T> displayFactory(Function<T, ? extends CreateDisplay<T>> factory) {
			this.displayFactory = factory;
			return this;
		}

		public CreateRecipeCategory<T> build(String name, CreateRecipeCategory.Factory<T> factory) {
			Supplier<List<T>> recipesSupplier;
			if (predicate.test(AllConfigs.server().recipes)) {
				recipesSupplier = () -> {
					List<T> recipes = new ArrayList<>();
					if (predicate.test(AllConfigs.server().recipes)) {
						for (Consumer<List<T>> consumer : recipeListConsumers)
							consumer.accept(recipes);
					}
					return recipes;
				};
			} else {
				recipesSupplier = () -> Collections.emptyList();
			}

			if (width <= 0 || height <= 0) {
				Create.LOGGER.warn("Create REI category [{}] has weird dimensions: {}x{}", name, width, height);
			}

			CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info<>(
					CategoryIdentifier.of(CreateSandpaperOverhaul.id(name)),
					new TranslatableComponent("rei.category.create_so." + name), background, icon, recipesSupplier, catalysts, width, height, displayFactory == null ? (recipe) -> new CreateDisplay<>(recipe, CategoryIdentifier.of(CreateSandpaperOverhaul.id(name))) : displayFactory);
			CreateRecipeCategory<T> category = factory.create(info);
			allCategories.add(category);
			return category;
		}
	}
}
