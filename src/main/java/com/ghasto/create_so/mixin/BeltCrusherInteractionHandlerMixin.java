package com.ghasto.create_so.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ghasto.create_so.content.polishing_wheel.BeltPolisherInteractionHandler;
import com.simibubi.create.content.kinetics.belt.transport.BeltCrusherInteractionHandler;
import com.simibubi.create.content.kinetics.belt.transport.BeltInventory;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;

@Mixin(value = BeltCrusherInteractionHandler.class, remap = false)
public class BeltCrusherInteractionHandlerMixin {
	@Inject(method = "checkForCrushers", at = @At("TAIL"), cancellable = true)
	private static void injected(BeltInventory beltInventory, TransportedItemStack currentItem, float nextOffset, CallbackInfoReturnable<Boolean> cir) {
		BeltPolisherInteractionHandler.checkForPolishers(beltInventory, currentItem, nextOffset, cir);
	}
}
