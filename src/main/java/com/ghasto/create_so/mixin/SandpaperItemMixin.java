package com.ghasto.create_so.mixin;

import com.ghasto.create_so.ModDamageTypes;
import com.ghasto.create_so.ModItems;
import com.ghasto.create_so.util.SandpaperUtils;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(value = SandPaperItem.class)
public class SandpaperItemMixin extends Item {
	public SandpaperItemMixin(Properties properties) {
		super(properties);
	}

	@Inject(method = "use", at = @At("HEAD"))
	public void injenctedUse(Level worldIn, Player player, InteractionHand handIn, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
		if(!worldIn.isClientSide()) {
			HitResult raytraceresult = getPlayerPOVHitResult(worldIn, player, ClipContext.Fluid.NONE);
			BlockHitResult ray = (BlockHitResult) raytraceresult;
			Vec3 hitVec = ray.getLocation();

			AABB bb = new AABB(hitVec, hitVec).inflate(1f);
			List<LivingEntity> hitPlayer = worldIn.getEntitiesOfClass(LivingEntity.class, bb);
			if (!hitPlayer.isEmpty()) {
				float damageDoneToEntity = 1;
				ItemStack stack = player.getItemInHand(handIn);
				if (stack.getItem() == ModItems.IRON_SANDPAPER.get()) {
					damageDoneToEntity = 2;
				} else if
				(stack.getItem() == ModItems.DIAMOND_SANDPAPER.get()) {
					damageDoneToEntity = 3.5f;
				} else if
				(stack.getItem() == ModItems.OBSIDIAN_SANDPAPER.get()) {
					damageDoneToEntity = 4;
				}

				hitPlayer.get(0).hurt(ModDamageTypes.SANDPAPER.source(worldIn), damageDoneToEntity);
			}
		}
	}

	@Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
	public void injectedUseOn(UseOnContext ctx, CallbackInfoReturnable<InteractionResult> cir) {
			BlockState blockLookingAt = ctx.getLevel().getBlockState(ctx.getClickedPos());
			Arrays.stream(SandpaperUtils.Polishable.values()).toList().forEach(polishable -> {
				if (blockLookingAt.is(polishable.block)) {
					setBlockAndUpdateDurability(ctx, polishable.result, polishable.durabilityUsed, cir);
				} else if (blockLookingAt.is(polishable.variantInput("slab")) && polishable.variantResult("slab") instanceof SlabBlock) {
					setBlockAndUpdateDurability(ctx, polishable.variantResult("slab"), polishable.durabilityUsed, cir);
				} else if (blockLookingAt.is(polishable.variantInput("stairs")) && polishable.variantResult("stairs") instanceof StairBlock) {
					setBlockAndUpdateDurability(ctx, polishable.variantResult("stairs"), polishable.durabilityUsed, cir);
				} else if (blockLookingAt.is(polishable.variantInput("wall")) && polishable.variantResult("wall") instanceof WallBlock) {
					setBlockAndUpdateDurability(ctx, polishable.variantResult("wall"), polishable.durabilityUsed, cir);
				}
			});
	}
	public void setBlockAndUpdateDurability(UseOnContext ctx, Block block, int durabilityUsed, CallbackInfoReturnable<InteractionResult> cir) {
		BlockState blockLookingAt = ctx.getLevel().getBlockState(ctx.getClickedPos());
		ctx.getPlayer().swing(ctx.getHand());
		ctx.getLevel().addDestroyBlockEffect(ctx.getClickedPos(), ctx.getLevel().getBlockState(ctx.getClickedPos()));
		AllSoundEvents.SANDING_LONG.play(ctx.getLevel(), ctx.getPlayer(), ctx.getClickLocation(), 1, 1);
		if(!ctx.getLevel().isClientSide()) {
			if (blockLookingAt.getBlock() instanceof SlabBlock) {
				ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), block.defaultBlockState().setValue(SlabBlock.TYPE, blockLookingAt.getValue(SlabBlock.TYPE))
						.setValue(SlabBlock.WATERLOGGED, blockLookingAt.getValue(SlabBlock.WATERLOGGED)));
			} else if (blockLookingAt.getBlock() instanceof StairBlock) {
				ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), block.defaultBlockState()
						.setValue(StairBlock.FACING, blockLookingAt.getValue(StairBlock.FACING))
						.setValue(StairBlock.HALF, blockLookingAt.getValue(StairBlock.HALF))
						.setValue(StairBlock.SHAPE, blockLookingAt.getValue(StairBlock.SHAPE))
						.setValue(StairBlock.WATERLOGGED, blockLookingAt.getValue(StairBlock.WATERLOGGED)));
			} else if (blockLookingAt.getBlock() instanceof WallBlock) {
				ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), block.defaultBlockState()
						.setValue(WallBlock.NORTH_WALL, blockLookingAt.getValue(WallBlock.NORTH_WALL))
						.setValue(WallBlock.SOUTH_WALL, blockLookingAt.getValue(WallBlock.SOUTH_WALL))
						.setValue(WallBlock.WEST_WALL, blockLookingAt.getValue(WallBlock.WEST_WALL))
						.setValue(WallBlock.EAST_WALL, blockLookingAt.getValue(WallBlock.EAST_WALL))
						.setValue(WallBlock.UP, blockLookingAt.getValue(WallBlock.UP))
						.setValue(WallBlock.WATERLOGGED, blockLookingAt.getValue(WallBlock.WATERLOGGED)));

			} else {
				ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), block.defaultBlockState());
			}
			if (ctx.getItemInHand().getMaxDamage() - ctx.getItemInHand().getDamageValue() != 1) {
				ctx.getItemInHand().setDamageValue(ctx.getItemInHand().getDamageValue() + durabilityUsed);
			} else {
				ctx.getPlayer().setItemInHand(ctx.getHand(), ItemStack.EMPTY);
			}
		}
		cir.setReturnValue(InteractionResult.SUCCESS);
	}
}
