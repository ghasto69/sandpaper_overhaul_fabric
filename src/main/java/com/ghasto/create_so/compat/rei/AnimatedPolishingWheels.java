package com.ghasto.create_so.compat.rei;

import java.util.List;

import com.ghasto.create_so.ModBlocks;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.compat.rei.category.animations.AnimatedKinetics;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class AnimatedPolishingWheels extends AnimatedKinetics {

	private final BlockState wheel = ModBlocks.POLISHING_WHEEL.getDefaultState()
			.setValue(BlockStateProperties.AXIS, Direction.Axis.X);
@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float delta) {
		matrixStack.pushPose();
		matrixStack.translate(getPos().getX(), getPos().getY(), 100);
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(-22.5f));
		int scale = 22;

		blockElement(wheel)
				.rotateBlock(0, 90, -getCurrentAngle())
				.scale(scale)
				.render(matrixStack);

		blockElement(wheel)
				.rotateBlock(0, 90, getCurrentAngle())
				.atLocal(2, 0, 0)
				.scale(scale)
				.render(matrixStack);

		matrixStack.popPose();
	}

	@Override
	public List<? extends GuiEventListener> children() {
		return Lists.newArrayList();
	}

}
