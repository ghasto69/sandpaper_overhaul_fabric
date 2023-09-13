package com.ghasto.create_so;

import com.simibubi.create.foundation.damageTypes.DamageTypeData;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {
	public static final DamageTypeData SANDPAPER = DamageTypeData.builder()
			.simpleId(CreateSandpaperOverhaul.id("sandpaper"))
			.tag(DamageTypeTags.BYPASSES_ARMOR)
			.exhaustion(2f)
			.build();
	public static void bootstrap(BootstapContext<DamageType> ctx) {
		DamageTypeData.allInNamespace(CreateSandpaperOverhaul.ID).forEach(data -> data.register(ctx));
	}
}
