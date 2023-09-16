package com.ghasto.create_so.mixin;

import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(SandPaperItem.class)
public class SandpaperItemMixinClient extends Item {
    public SandpaperItemMixinClient(Properties properties) {
        super(properties);
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
