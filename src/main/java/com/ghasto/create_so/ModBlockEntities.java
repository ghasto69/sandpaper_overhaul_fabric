package com.ghasto.create_so;

import static com.ghasto.create_so.CreateSandpaperOverhaul.REGISTRATE;

import com.ghasto.create_so.content.polishing_wheel.PolishingWheelBlockEntity;
import com.ghasto.create_so.content.polishing_wheel.PolishingWheelControllerBlockEntity;
import com.simibubi.create.content.kinetics.base.CutoutRotatingInstance;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class ModBlockEntities {
    public static final BlockEntityEntry<PolishingWheelBlockEntity> POLISHING_WHEEL = REGISTRATE
            .blockEntity("polishing_wheel", PolishingWheelBlockEntity::new)
            .instance(() -> CutoutRotatingInstance::new, false)
            .validBlocks(ModBlocks.POLISHING_WHEEL)
            .renderer(() -> KineticBlockEntityRenderer::new)
            .register();

    public static final BlockEntityEntry<PolishingWheelControllerBlockEntity> POLISHING_WHEEL_CONTROLLER =
            REGISTRATE
                    .blockEntity("polishing_wheel_controller", PolishingWheelControllerBlockEntity::new)
                    .validBlocks(ModBlocks.POLISHING_WHEEL_CONTROLLER)
                    // .renderer(() -> renderer)
                    .register();
    public static void register() {}
}
