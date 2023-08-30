package com.ghasto.create_so;

import static com.ghasto.create_so.CreateSandpaperOverhaul.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

import com.ghasto.create_so.content.polishing_wheel.PolishingWheelBlock;
import com.ghasto.create_so.content.polishing_wheel.PolishingWheelControllerBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ModBlocks {
    public static final BlockEntry<PolishingWheelBlock> POLISHING_WHEEL =
            REGISTRATE.block("polishing_wheel", PolishingWheelBlock::new)
                    .properties(p -> p.mapColor(MapColor.METAL))
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, s -> AssetLookup.partialBaseModel(c, p)))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(BlockStressDefaults.setImpact(12.0))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<PolishingWheelControllerBlock> POLISHING_WHEEL_CONTROLLER =
            REGISTRATE.block("polishing_wheel_controller", PolishingWheelControllerBlock::new)
                    .properties(p -> p.mapColor(MapColor.STONE)
                            .noOcclusion()
                            .noLootTable()
                            .air()
                            .noCollission()
                            .pushReaction(PushReaction.BLOCK))
                    .blockstate((c, p) -> p.getVariantBuilder(c.get())
                            .forAllStatesExcept(BlockStateGen.mapToAir(p), PolishingWheelControllerBlock.FACING))
                    .register();
    public static void register() {}
}
