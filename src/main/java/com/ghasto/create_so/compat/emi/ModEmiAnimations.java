package com.ghasto.create_so.compat.emi;

import com.ghasto.create_so.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static com.simibubi.create.compat.emi.CreateEmiAnimations.blockElement;
import static com.simibubi.create.compat.emi.CreateEmiAnimations.getCurrentAngle;

public class ModEmiAnimations {
	private static final BlockState WHEEL = ModBlocks.POLISHING_WHEEL.getDefaultState().setValue(BlockStateProperties.AXIS, Direction.Axis.X);
	public static void addPolishingWheels(WidgetHolder widgets, int x, int y) {
		widgets.addDrawable(x, y, 0, 0, (graphics, mouseX, mouseY, delta) -> {
			PoseStack matrices = graphics.pose();
			matrices.translate(0, 0, 100);
			matrices.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-22.5f));
			int scale = 22;

			blockElement(WHEEL)
					.rotateBlock(0, 90, -getCurrentAngle())
					.scale(scale)
					.render(graphics);

			blockElement(WHEEL)
					.rotateBlock(0, 90, getCurrentAngle())
					.atLocal(2, 0, 0)
					.scale(scale)
					.render(graphics);
		});
	}
}
