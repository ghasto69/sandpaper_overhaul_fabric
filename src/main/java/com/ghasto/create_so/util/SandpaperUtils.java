package com.ghasto.create_so.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class SandpaperUtils {
	public enum Polishable {
		/*ROSE_QUARTZ(AllBlocks.ROSE_QUARTZ_BLOCK.get(), null),
		LAPIS(ModBlocks.UNREFINED_LAPIS_BLOCK.get(), Blocks.LAPIS_BLOCK),*/
		SMOOTH_STONE(Blocks.STONE, Blocks.SMOOTH_STONE),
		SMOOTH_SANDSTONE(Blocks.SANDSTONE, Blocks.SMOOTH_SANDSTONE)
		;
		public final Block block;
		public final Block result;
		public final int durabilityUsed;

		Polishable(Block polishable, Block polished) {
			this.block = polishable;
			this.result = polished;
			this.durabilityUsed = 1;
		}
		Polishable(Block polishable, Block polished, int durabilityConsumed) {
			this.block = polishable;
			this.result = polished;
			this.durabilityUsed = durabilityConsumed;
		}
	}
}

