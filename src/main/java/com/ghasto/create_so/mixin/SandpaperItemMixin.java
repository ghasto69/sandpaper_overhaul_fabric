package com.ghasto.create_so.mixin;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ghasto.create_so.ModDamageTypes;
import com.ghasto.create_so.ModItems;
import com.ghasto.create_so.util.SandpaperUtils;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.foundation.item.TooltipHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@Mixin(value = SandPaperItem.class)
public class SandpaperItemMixin extends Item {
	public SandpaperItemMixin(Properties properties) {
		super(properties);
	}

	@Inject(method = "use", at = @At("HEAD"))
	public void injenctedUse(Level worldIn, Player player, InteractionHand handIn, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
	HitResult raytraceresult = getPlayerPOVHitResult(worldIn, player, ClipContext.Fluid.NONE);
	BlockHitResult ray = (BlockHitResult) raytraceresult;
	Vec3 hitVec = ray.getLocation();

	AABB bb = new AABB(hitVec, hitVec).inflate(1f);
	List<LivingEntity> hitPlayer = worldIn.getEntitiesOfClass(LivingEntity.class, bb);
	if(!hitPlayer.isEmpty()) {
		float damageDoneToEntity = 1;
		ItemStack stack = player.getItemInHand(handIn);
		if(stack.getItem() == ModItems.IRON_SANDPAPER.get()) {damageDoneToEntity = 2;} else if
		(stack.getItem() == ModItems.DIAMOND_SANDPAPER.get()) {damageDoneToEntity = 3.5f;} else if
		(stack.getItem() == ModItems.OBSIDIAN_SANDPAPER.get()) {damageDoneToEntity = 4;}

		hitPlayer.get(0).hurt(ModDamageTypes.SANDPAPER.source(worldIn), damageDoneToEntity);
	}
	}

	@Inject(method = "useOn", at = @At("HEAD"))
	public void injectedUseOn(UseOnContext ctx, CallbackInfoReturnable<InteractionResult> cir){
		BlockState blockLookingAt = ctx.getLevel().getBlockState(ctx.getClickedPos());
		Arrays.stream(SandpaperUtils.Polishable.values()).toList().forEach(polishable -> {
			if(blockLookingAt.is(polishable.block)) {
				ctx.getLevel().addDestroyBlockEffect(ctx.getClickedPos(), ctx.getLevel().getBlockState(ctx.getClickedPos()));
				AllSoundEvents.SANDING_LONG.play(ctx.getLevel(), ctx.getPlayer(), ctx.getClickLocation(), 1, 1);
				ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), polishable.result.defaultBlockState());
				if(ctx.getItemInHand().getMaxDamage() - ctx.getItemInHand().getDamageValue() != 1) {
					ctx.getItemInHand().setDamageValue(ctx.getItemInHand().getDamageValue() + polishable.durabilityUsed);
				} else {
					ctx.getPlayer().setItemInHand(ctx.getHand(), ItemStack.EMPTY);
				}
				ctx.getPlayer().swing(ctx.getHand());
			}
		});
	}
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
	if(Screen.hasAltDown()){
		tooltipComponents.add(Component.literal("Hold ")
				.withStyle(ChatFormatting.DARK_GRAY)
				.append(Component.literal("[").withStyle(ChatFormatting.DARK_GRAY)
						.append(Component.literal("Alt").withStyle(ChatFormatting.WHITE))
						.append(Component.literal("]").withStyle(ChatFormatting.DARK_GRAY))
						.append(Component.literal(" for Durability").withStyle(ChatFormatting.DARK_GRAY))
				));
		tooltipComponents.add(Component.literal("")
						.append("Durability: ").withStyle(TooltipHelper.Palette.STANDARD_CREATE.primary())
				.append(Component.literal(String.valueOf(stack.getMaxDamage() - stack.getDamageValue())).withStyle(ChatFormatting.BOLD).withStyle(TooltipHelper.Palette.STANDARD_CREATE.highlight()))
						.append(Component.literal("/").withStyle(TooltipHelper.Palette.STANDARD_CREATE.highlight()).withStyle(ChatFormatting.BOLD))
				.append(Component.literal(String.valueOf(stack.getMaxDamage())).withStyle(TooltipHelper.Palette.STANDARD_CREATE.highlight()).withStyle(ChatFormatting.BOLD)));
	} else {
		tooltipComponents.add(Component.literal("Hold ")
				.withStyle(ChatFormatting.DARK_GRAY)
						.append(Component.literal("[").withStyle(ChatFormatting.DARK_GRAY)
						.append(Component.literal("Alt").withStyle(ChatFormatting.GRAY))
						.append(Component.literal("]").withStyle(ChatFormatting.DARK_GRAY))
								.append(Component.literal(" for Durability").withStyle(ChatFormatting.DARK_GRAY))
				));
	}
	}
}
