package com.ghasto.create_so;

import net.minecraft.world.damagesource.DamageSource;

public class ModDamageTypes {
	public static final DamageSource SANDPAPER = new DamageSource("sandpaper").bypassMagic().damageHelmet();
	public static final DamageSource POLISH = new DamageSource("polish").bypassArmor();
//	public static final DamageTypeData SANDPAPER = DamageTypeData.builder()
//			.simpleId(CreateSandpaperOverhaul.id("sandpaper"))
//			.tag(DamageTypeTags.BYPASSES_ARMOR)
//			.exhaustion(2f)
//			.build();
//	public static void bootstrap(BootstapContext<DamageType> ctx) {
//		DamageTypeData.allInNamespace(CreateSandpaperOverhaul.ID).forEach(data -> data.register(ctx));
//	}
}
