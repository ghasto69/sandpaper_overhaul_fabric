package com.ghasto.create_so.content.polishing_wheel;


import com.ghasto.create_so.ModBlockEntities;
import com.ghasto.create_so.ModBlocks;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlock;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class PolishingWheelControllerBlock extends CrushingWheelControllerBlock {

	public PolishingWheelControllerBlock(Properties p_i48440_1_) {
		super(p_i48440_1_);
	}

	@Override
	public void updateSpeed(BlockState state, LevelAccessor world, BlockPos pos) {
		withBlockEntityDo(world, pos, be -> {
			if (!state.getValue(VALID)) {
				if (be.crushingspeed != 0) {
					be.crushingspeed = 0;
					be.sendData();
				}
				return;
			}

			for (Direction d : Iterate.directions) {
				BlockState neighbour = world.getBlockState(pos.relative(d));
				if (!ModBlocks.POLISHING_WHEEL.has(neighbour))
					continue;
				if (neighbour.getValue(BlockStateProperties.AXIS) == d.getAxis())
					continue;
				BlockEntity adjBE = world.getBlockEntity(pos.relative(d));
				if (!(adjBE instanceof PolishingWheelBlockEntity cwbe))
					continue;
				be.crushingspeed = Math.abs(cwbe.getSpeed() / 50f);
				be.sendData();

				cwbe.award(AllAdvancements.CRUSHING_WHEEL);
				if (cwbe.getSpeed() > 255)
					cwbe.award(AllAdvancements.CRUSHER_MAXED);
				break;
			}
		});
	}
	@Override
	public BlockEntityType<? extends CrushingWheelControllerBlockEntity> getBlockEntityType() {
		return ModBlockEntities.POLISHING_WHEEL_CONTROLLER.get();
	}
}
