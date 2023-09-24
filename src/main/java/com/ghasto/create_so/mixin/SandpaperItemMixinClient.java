package com.ghasto.create_so.mixin;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.foundation.item.TooltipHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

@Mixin(SandPaperItem.class)
public class SandpaperItemMixinClient extends Item {
    public SandpaperItemMixinClient(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if(Screen.hasAltDown()){
            tooltipComponents.add(new TextComponent("Hold ")
                    .withStyle(ChatFormatting.DARK_GRAY)
                    .append(new TextComponent("[").withStyle(ChatFormatting.DARK_GRAY)
                            .append(new TextComponent("Alt").withStyle(ChatFormatting.WHITE))
                            .append(new TextComponent("]").withStyle(ChatFormatting.DARK_GRAY))
                            .append(new TextComponent(" for Durability").withStyle(ChatFormatting.DARK_GRAY))
                    ));
            tooltipComponents.add(new TextComponent("")
                    .append("Durability: ").withStyle(TooltipHelper.Palette.STANDARD_CREATE.primary())
                    .append(new TextComponent(String.valueOf(stack.getMaxDamage() - stack.getDamageValue())).withStyle(ChatFormatting.BOLD).withStyle(TooltipHelper.Palette.STANDARD_CREATE.highlight()))
                    .append(new TextComponent("/").withStyle(TooltipHelper.Palette.STANDARD_CREATE.highlight()).withStyle(ChatFormatting.BOLD))
                    .append(new TextComponent(String.valueOf(stack.getMaxDamage())).withStyle(TooltipHelper.Palette.STANDARD_CREATE.highlight()).withStyle(ChatFormatting.BOLD)));
        } else {
            tooltipComponents.add(new TextComponent("Hold ")
                    .withStyle(ChatFormatting.DARK_GRAY)
                    .append(new TextComponent("[").withStyle(ChatFormatting.DARK_GRAY)
                            .append(new TextComponent("Alt").withStyle(ChatFormatting.GRAY))
                            .append(new TextComponent("]").withStyle(ChatFormatting.DARK_GRAY))
                            .append(new TextComponent(" for Durability").withStyle(ChatFormatting.DARK_GRAY))
                    ));
        }
    }
}
