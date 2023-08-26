package com.ghasto.create_so.content.polishing_wheel;

import java.util.Collection;
import java.util.List;

import com.simibubi.create.AllDamageTypes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PolishingWheelBlockEntity extends KineticBlockEntity {
    public PolishingWheelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(20);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        fixControllers();
    }

    public void fixControllers() {
        for (Direction d : Iterate.directions)
            ((PolishingWheelBlock) getBlockState().getBlock()).updateControllers(getBlockState(), getLevel(), getBlockPos(),
                    d);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).inflate(1);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        fixControllers();
    }

    public static int crushingIsFortunate(DamageSource source, LivingEntity target, int currentLevel, boolean recentlyHit) {
        if (!AllDamageTypes.CRUSH.is(source))
            return 0;
        return 2;
    }

    public static boolean handleCrushedMobDrops(LivingEntity target, DamageSource source, Collection<ItemEntity> drops, int lootingLevel, boolean recentlyHit) {
        if (!AllDamageTypes.CRUSH.is(source))
            return false;
        Vec3 outSpeed = Vec3.ZERO;
        for (ItemEntity outputItem : drops) {
            outputItem.setDeltaMovement(outSpeed);
        }
        return false;
    }

}

