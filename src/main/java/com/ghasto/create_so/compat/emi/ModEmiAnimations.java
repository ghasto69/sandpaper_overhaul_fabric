package com.ghasto.create_so.compat.emi;

import static com.simibubi.create.compat.emi.CreateEmiAnimations.blockElement;
import static com.simibubi.create.compat.emi.CreateEmiAnimations.getCurrentAngle;

import com.ghasto.create_so.ModBlocks;
import com.mojang.math.Vector3f;

import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ModEmiAnimations {
	private static final BlockState WHEEL = ModBlocks.POLISHING_WHEEL.getDefaultState().setValue(BlockStateProperties.AXIS, Direction.Axis.X);
	public static void addPolishingWheels(WidgetHolder widgets, int x, int y) {
		widgets.addDrawable(x, y, 0, 0, (matrices, mouseX, mouseY, delta) -> {
			matrices.translate(0, 0, 100);
			matrices.mulPose(Vector3f.YP.rotationDegrees(-22.5f));
			int scale = 22;

			blockElement(WHEEL)
					.rotateBlock(0, 90, -getCurrentAngle())
					.scale(scale)
					.render(matrices);

			blockElement(WHEEL)
					.rotateBlock(0, 90, getCurrentAngle())
					.atLocal(2, 0, 0)
					.scale(scale)
					.render(matrices);
		});
	}
}
