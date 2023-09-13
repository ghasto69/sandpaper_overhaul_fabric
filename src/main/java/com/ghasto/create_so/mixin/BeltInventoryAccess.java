package com.ghasto.create_so.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.transport.BeltInventory;

@Mixin(value = BeltInventory.class, remap = false)
public interface BeltInventoryAccess {
    @Accessor
    public BeltBlockEntity getBelt();
    @Accessor
    public boolean getBeltMovementPositive();
}
