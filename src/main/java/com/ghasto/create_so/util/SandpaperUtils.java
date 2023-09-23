package com.ghasto.create_so.util;

import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class SandpaperUtils {
	public enum Polishable {
		//Resource Polishing
		/*ROSE_QUARTZ(AllBlocks.ROSE_QUARTZ_BLOCK.get(), null),
		LAPIS(ModBlocks.UNREFINED_LAPIS_BLOCK.get(), Blocks.LAPIS_BLOCK),*/

		//Vanilla Blocks
		SMOOTH_STONE(Blocks.STONE, Blocks.SMOOTH_STONE),
		SMOOTH_SANDSTONE(Blocks.SANDSTONE, Blocks.SMOOTH_SANDSTONE),
		SMOOTH_RED_SANDSTONE(Blocks.RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE),
		POLISHED_DEEPSLATE(Blocks.DEEPSLATE, Blocks.POLISHED_DEEPSLATE),
		SMOOTH_QUARTZ(Blocks.QUARTZ_BLOCK, Blocks.SMOOTH_QUARTZ),
		POLISHED_BLACKSTONE(Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE),
		POLISHED_BASALT(Blocks.BASALT, Blocks.POLISHED_BASALT),
		POLISHED_ANDESITE(Blocks.ANDESITE, Blocks.POLISHED_ANDESITE),
		POLISHED_GRANITE(Blocks.GRANITE, Blocks.POLISHED_GRANITE),
		POLISHED_DIORITE(Blocks.DIORITE, Blocks.POLISHED_DIORITE),


		//Create Blocks
		POLISHED_CUT_GRANITE(stoneType(AllPaletteStoneTypes.GRANITE, 0), stoneType(AllPaletteStoneTypes.GRANITE, 1), "create"),
		POLISHED_CUT_DIORITE(stoneType(AllPaletteStoneTypes.DIORITE, 0), stoneType(AllPaletteStoneTypes.DIORITE, 1), "create"),
		POLISHED_CUT_ANDESITE(stoneType(AllPaletteStoneTypes.ANDESITE, 0), stoneType(AllPaletteStoneTypes.ANDESITE, 1), "create"),
		POLISHED_CUT_CALCITE(stoneType(AllPaletteStoneTypes.CALCITE, 0), stoneType(AllPaletteStoneTypes.CALCITE, 1), "create"),
		POLISHED_CUT_DRIPSTONE(stoneType(AllPaletteStoneTypes.DRIPSTONE, 0), stoneType(AllPaletteStoneTypes.DRIPSTONE, 1), "create"),
		POLISHED_CUT_DEEPSLATE(stoneType(AllPaletteStoneTypes.DEEPSLATE, 0), stoneType(AllPaletteStoneTypes.DEEPSLATE, 1), "create"),
		POLISHED_CUT_TUFF(stoneType(AllPaletteStoneTypes.TUFF, 0), stoneType(AllPaletteStoneTypes.TUFF, 1), "create"),
		POLISHED_CUT_ASURINE(stoneType(AllPaletteStoneTypes.ASURINE, 0), stoneType(AllPaletteStoneTypes.ASURINE, 1), "create"),
		POLISHED_CUT_CRIMSITE(stoneType(AllPaletteStoneTypes.CRIMSITE, 0), stoneType(AllPaletteStoneTypes.CRIMSITE, 1), "create"),
		POLISHED_CUT_LIMESTONE(stoneType(AllPaletteStoneTypes.LIMESTONE, 0), stoneType(AllPaletteStoneTypes.LIMESTONE, 1), "create"),
		POLISHED_CUT_OCHRUM(stoneType(AllPaletteStoneTypes.OCHRUM, 0), stoneType(AllPaletteStoneTypes.OCHRUM, 1), "create"),
		POLISHED_CUT_SCORIA(stoneType(AllPaletteStoneTypes.SCORIA, 0), stoneType(AllPaletteStoneTypes.SCORIA, 1), "create"),
		POLISHED_CUT_SCORCHIA(stoneType(AllPaletteStoneTypes.SCORCHIA, 0), stoneType(AllPaletteStoneTypes.SCORCHIA, 1), "create"),
		POLISHED_CUT_VERIDIUM(stoneType(AllPaletteStoneTypes.VERIDIUM, 0), stoneType(AllPaletteStoneTypes.VERIDIUM, 1), "create"),

		//Minecraft names things weirdly :(
		DEEPSLATE_SLAB(Blocks.COBBLED_DEEPSLATE_SLAB, Blocks.POLISHED_DEEPSLATE_SLAB),
		DEEPSLATE_STAIRS(Blocks.COBBLED_DEEPSLATE_STAIRS, Blocks.POLISHED_DEEPSLATE_STAIRS),
		DEEPSLATE_WALL(Blocks.COBBLED_DEEPSLATE_WALL, Blocks.POLISHED_DEEPSLATE_WALL)
		;
		public final Block block;
		public final Block result;
		public final int durabilityUsed;
		public final String id;

		Polishable(Block polishable, Block polished) {
			this.block = polishable;
			this.result = polished;
			this.durabilityUsed = 1;
			this.id = "minecraft";
		}
		Polishable(Block polishable, Block polished, int durabilityConsumed) {
			this.block = polishable;
			this.result = polished;
			this.durabilityUsed = durabilityConsumed;
			this.id = "minecraft";
		}
		Polishable(Block polishable, Block polished, String modid) {
			this.block = polishable;
			this.result = polished;
			this.durabilityUsed = 1;
			this.id = modid;
		}
		Polishable(Block polishable, Block polished, int durabilityConsumed, String modid) {
			this.block = polishable;
			this.result = polished;
			this.durabilityUsed = durabilityConsumed;
			this.id = modid;
		}
		public Block variantResult(String variantID) {
			String blockid = String.valueOf(this.result).substring(7 + id.length()).replace("}", "").replace("_" + variantID, "");
			return Registry.BLOCK.get(
					new ResourceLocation(this.id, blockid + "_" + variantID));
		}
		public Block variantInput(String variantID) {
			String blockid = String.valueOf(this.block).substring(7 + id.length()).replace("}", "").replace("_" + variantID, "");
			return Registry.BLOCK.get(
					new ResourceLocation(this.id, blockid + "_" + variantID));
		}
	}

	//create made their system do complicated
	public static Block stoneType(AllPaletteStoneTypes stoneType, int i) {
		return stoneType.getVariants().registeredBlocks.get(i).get();
	}
}

