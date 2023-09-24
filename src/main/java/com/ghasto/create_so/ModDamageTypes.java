package com.ghasto.create_so;

import io.github.fabricators_of_create.porting_lib.util.DamageSourceHelper;
import net.minecraft.world.damagesource.DamageSource;

public class ModDamageTypes {
	public static final DamageSource SANDPAPER = DamageSourceHelper.port_lib$createDamageSource("sandpaper");
	public static final DamageSource POLISH = DamageSourceHelper.port_lib$createArmorBypassingDamageSource("polish");
//	public static final DamageTypeData SANDPAPER = DamageTypeData.builder()
//			.simpleId(CreateSandpaperOverhaul.id("sandpaper"))
//			.tag(DamageTypeTags.BYPASSES_ARMOR)
//			.exhaustion(2f)
//			.build();
//	public static void bootstrap(BootstapContext<DamageType> ctx) {
//		DamageTypeData.allInNamespace(CreateSandpaperOverhaul.ID).forEach(data -> data.register(ctx));
//	}
}
