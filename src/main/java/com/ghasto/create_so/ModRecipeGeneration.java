package com.ghasto.create_so;

import com.ghasto.create_so.content.polishing_wheel.PolishingRecipe;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

public class ModRecipeGeneration {
	public static void generateAll(RegistrateRecipeProvider p) {
		ShapelessRecipeBuilder.shapeless(ModItems.IRON_SANDPAPER.get())
				.requires(AllItems.IRON_SHEET.get())
				.requires(ModItems.CRUSHED_IRON.get())
				.unlockedBy("has_iron_sheet", RegistrateRecipeProvider.has(AllItems.IRON_SHEET.get()))
				.save(p);
		ShapelessRecipeBuilder.shapeless(ModItems.DIAMOND_SANDPAPER.get())
				.requires(AllItems.IRON_SHEET.get())
				.requires(ModItems.CRUSHED_DIAMONDS.get())
				.unlockedBy("has_iron_sheet", RegistrateRecipeProvider.has(AllItems.IRON_SHEET.get()))
				.save(p);
		ShapelessRecipeBuilder.shapeless(ModItems.OBSIDIAN_SANDPAPER.get())
				.requires(AllItems.STURDY_SHEET.get())
				.requires(AllItems.POWDERED_OBSIDIAN.get())
				.unlockedBy("has_sturdy_sheet", RegistrateRecipeProvider.has(AllItems.STURDY_SHEET.get()))
				.save(p);
		getCrushingBuilder("crushed_iron").require(Items.IRON_INGOT).output(ModItems.CRUSHED_IRON.get()).duration(100).build(p);
		getCrushingBuilder("crushed_diamonds").require(Items.DIAMOND).output(ModItems.CRUSHED_DIAMONDS.get()).duration(100).build(p);
		getPolishingBuilder("lapis_lazuli").require(ModItems.UNREFINED_LAPIS_LAZULI.get())
				.output(Items.LAPIS_LAZULI).duration(200).output(0.5f, Items.LAPIS_LAZULI).build(p);
		getPolishingBuilder("polished_rose_quartz").require(AllItems.ROSE_QUARTZ.get())
				.output(AllItems.POLISHED_ROSE_QUARTZ.get()).duration(200).output(0.5f, AllItems.POLISHED_ROSE_QUARTZ.get()).build(p);
		new SequencedAssemblyRecipeBuilder(CreateSandpaperOverhaul.id("unrefined_lapis_lazuli"))
				.require(Items.QUARTZ)
				.transitionTo(ModItems.UNFINISHED_UNREFINED_LAPIS_LAZULI.get())
				.loops(1)
				.addOutput(ModItems.UNREFINED_LAPIS_LAZULI.get(), 1)
				.addStep(FillingRecipe::new, rb -> rb.require(Fluids.LAVA, FluidConstants.fromBucketFraction(1, 4)))
				.addStep(FillingRecipe::new, rb -> rb.require(Fluids.WATER, FluidConstants.fromBucketFraction(1, 4)))
				.addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.BLUE_DYE).toolNotConsumed())
				.build(p);
		new SequencedAssemblyRecipeBuilder(CreateSandpaperOverhaul.id("rose_quartz"))
				.require(Items.QUARTZ)
				.transitionTo(ModItems.UNFINISHED_ROSE_QUARTZ.get())
				.loops(1)
				.addOutput(AllItems.ROSE_QUARTZ.get(), 1)
				.addStep(FillingRecipe::new, rb -> rb.require(Fluids.WATER, FluidConstants.fromBucketFraction(1, 4)))
				.addStep(FillingRecipe::new, rb -> rb.require(Fluids.LAVA, FluidConstants.fromBucketFraction(1, 4)))
				.addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.RED_DYE).toolNotConsumed())
				.build(p);
		new SequencedAssemblyRecipeBuilder(CreateSandpaperOverhaul.id("ink_sac"))
				.require(Items.PAPER)
				.transitionTo(Items.PAPER)
				.loops(1)
				.addOutput(Items.INK_SAC, 1)
				.addStep(FillingRecipe::new, rb -> rb.require(Fluids.WATER, FluidConstants.fromBucketFraction(1, 10)))
				.addStep(DeployerApplicationRecipe::new, rb -> rb.require(ItemTags.COALS).toolNotConsumed())
				.build(p);
	}
	public static ProcessingRecipeBuilder<CrushingRecipe> getCrushingBuilder(String id) {
		AllRecipeTypes crushing = AllRecipeTypes.CRUSHING;
		ProcessingRecipeSerializer<CrushingRecipe> serializer = crushing.getSerializer();
		return new ProcessingRecipeBuilder<CrushingRecipe>
				(serializer.getFactory(), CreateSandpaperOverhaul.id(id));
	}
	public static ProcessingRecipeBuilder<PolishingRecipe> getPolishingBuilder(String id) {
		ModRecipeTypes polishing = ModRecipeTypes.POLISHING;
		ProcessingRecipeSerializer<PolishingRecipe> serializer = polishing.getSerializer();
		return new ProcessingRecipeBuilder<PolishingRecipe>
				(serializer.getFactory(), CreateSandpaperOverhaul.id(id));
	}
	public static ProcessingRecipeBuilder<FillingRecipe> getFillingBuilder(String id) {
		AllRecipeTypes filling = AllRecipeTypes.FILLING;
		ProcessingRecipeSerializer<FillingRecipe> serializer = filling.getSerializer();
		return new ProcessingRecipeBuilder<FillingRecipe>
				(serializer.getFactory(), CreateSandpaperOverhaul.id(id));
	}
}
