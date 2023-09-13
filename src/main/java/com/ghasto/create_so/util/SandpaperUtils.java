package com.ghasto.create_so.util;

import com.simibubi.create.AllBlocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class SandpaperUtils {
	public enum Polishable {
		ROSE_QUARTZ(AllBlocks.ROSE_QUARTZ_BLOCK.get(), Blocks.LAPIS_BLOCK);
		public final Block block;
		public final Block result;

		Polishable(Block polishable, Block polished) {
			this.block = polishable;
			this.result = polished;
		}
	}
}

