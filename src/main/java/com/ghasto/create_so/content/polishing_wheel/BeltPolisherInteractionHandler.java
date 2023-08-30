package com.ghasto.create_so.content.polishing_wheel;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ghasto.create_so.mixin.BeltInventoryAccess;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.transport.BeltInventory;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BeltPolisherInteractionHandler {

        public static void checkForPolishers(BeltInventory beltInventory, TransportedItemStack currentItem,
											   float nextOffset, CallbackInfoReturnable<Boolean> cir) {
            boolean beltMovementPositive = ((BeltInventoryAccess)beltInventory).getBeltMovementPositive();
            int firstUpcomingSegment = (int) Math.floor(currentItem.beltPosition);
            int step = beltMovementPositive ? 1 : -1;
            firstUpcomingSegment = Mth.clamp(firstUpcomingSegment, 0, ((BeltInventoryAccess)beltInventory).getBelt().beltLength - 1);

            for (int segment = firstUpcomingSegment; beltMovementPositive ? segment <= nextOffset
                    : segment + 1 >= nextOffset; segment += step) {
                BlockPos crusherPos = BeltHelper.getPositionForOffset(((BeltInventoryAccess)beltInventory).getBelt(), segment)
                        .above();
                Level world = ((BeltInventoryAccess)beltInventory).getBelt().getLevel();
                BlockState crusherState = world.getBlockState(crusherPos);
                if (!(crusherState.getBlock() instanceof PolishingWheelControllerBlock))
                    continue;
                Direction crusherFacing = crusherState.getValue(PolishingWheelControllerBlock.FACING);
                Direction movementFacing = ((BeltInventoryAccess)beltInventory).getBelt().getMovementFacing();
                if (crusherFacing != movementFacing)
                    continue;

                float crusherEntry = segment + .5f;
                crusherEntry += .399f * (beltMovementPositive ? -1 : 1);
                float postCrusherEntry = crusherEntry + .799f * (!beltMovementPositive ? -1 : 1);

                boolean hasCrossed = nextOffset > crusherEntry && nextOffset < postCrusherEntry && beltMovementPositive
                        || nextOffset < crusherEntry && nextOffset > postCrusherEntry && !beltMovementPositive;
                currentItem.beltPosition = crusherEntry;

                BlockEntity be = world.getBlockEntity(crusherPos);
                if (!(be instanceof PolishingWheelControllerBlockEntity))
                    cir.setReturnValue(true);

                PolishingWheelControllerBlockEntity crusherBE = (PolishingWheelControllerBlockEntity) be;

                ItemStack toInsert = currentItem.stack.copy();
                try (Transaction t = TransferUtil.getTransaction()) {
                    long inserted = crusherBE.inventory.insert(ItemVariant.of(toInsert), toInsert.getCount(), t);
                    t.commit();
                    ItemStack remainder = ItemHandlerHelper.copyStackWithSize(toInsert, toInsert.getCount() - (int) inserted);
                    if (ItemStack.matches(toInsert, remainder))
                        cir.setReturnValue(true);

                    int notFilled = currentItem.stack.getCount() - toInsert.getCount();
                    if (!remainder.isEmpty()) {
                        remainder.grow(notFilled);
                    } else if (notFilled > 0)
                        remainder = ItemHandlerHelper.copyStackWithSize(currentItem.stack, notFilled);

                    currentItem.stack = remainder;
                    ((BeltInventoryAccess)beltInventory).getBelt().sendData();
                    cir.setReturnValue(true);
                }
            }
        }
}
